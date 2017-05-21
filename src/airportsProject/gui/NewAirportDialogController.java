package airportsProject.gui;

import airportsProject.Airport;
import airportsProject.Coordinates;
import airportsProject.Utils;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Dialog window to insert OR edit an airport
 */
public class NewAirportDialogController {
    @FXML
    private TextField airportName;
    @FXML
    private TextField airportCode;
    @FXML
    private TextField airportCity;
    @FXML
    private TextField airportCountry;
    @FXML
    private TextField airportContinent;
    @FXML
    private Slider airportRating;
    @FXML
    private Label warning;
    @FXML
    private Label showRating;

    private boolean isEdit = false;
    private boolean invalidLocation = false;
    private ArrayList<Coordinates> coordinates = null;
    private double latitude = -1;
    private double longitude = -1;
    private Airport airport;

    NumberFormat formatter = new DecimalFormat("#0.#");

    public void initialize(){
        if(isEdit){
            airportName.setText(airport.getName());
            airportRating.setValue(airport.getRating());
            showRating.setText(String.valueOf(airport.getRating()));
            airportCode.setText(airport.getCode());
            airportCity.setText(airport.getCity());
            airportCountry.setText(airport.getCountry());
            airportContinent.setText(airport.getContinent());
            airportCode.setDisable(true);
            airportCountry.setDisable(true);
            airportContinent.setDisable(true);
            airportCity.setDisable(true);
        }
        warning.setStyle("-fx-opacity: 0");
        airportRating.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                showRating.setText(formatter.format(newValue));
            }
        });
    }

    @FXML
    void getNewAirportInput(ActionEvent event) {
        if(isEdit){ // editing an airport
            if(airportName.getText().length() == 0){
                warning.setStyle("-fx-opacity: 1");
            }else{
                warning.setStyle("-fx-opacity: 0");
                Utils.getInstance().editAirport(airport.getCode(), airportName.getText(), (float) Math.round(airportRating.getValue()*10)/10f);
                // close the dialog window
                airportName.getParent().getParent().getScene().getWindow().hide();
            }
        }else{ // new airport
            if(airportName.getText().length() == 0 || airportCode.getText().length() != 3 || airportCity.getText().length() == 0 ||
                    airportCountry.getText().length() == 0 || airportContinent.getText().length() == 0 || Utils.getInstance().getAirports().contains(airportCode.getText().toUpperCase())){
                warning.setStyle("-fx-opacity: 1");
            }else{
                warning.setStyle("-fx-opacity: 0");

                final Geocoder geocoder = new Geocoder();
                String location = airportCity.getText() + ", " + airportCountry.getText();
                GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("en").getGeocoderRequest();
                try {
                    GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
                    if(geocoderResponse.getResults().isEmpty()){
                        System.out.println(airportCode.getText() + ": Invalid Location / Not strong enough connection, try again");
                        invalidLocation = true;
                        return;
                    }
                    latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
                    longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
                    latitude  = (Utils.mapHeight) * (90 - latitude) / 180;
                    longitude = (Utils.mapWidth) * (180 + longitude) / 360;
                    Airport airport = new Airport(airportName.getText(), airportCode.getText().toUpperCase(), airportCity.getText(),
                            airportCountry.getText(), airportContinent.getText(), (float) Math.round(airportRating.getValue()*10)/10f, latitude, longitude);
                    Utils.getInstance().newAirport(airport);
                }catch (IOException e){
                    warning.setText("Maps Coordinates: Internet connection required.");
                    warning.setStyle("-fx-opacity: 1");
                }
                if(!invalidLocation){ // the location passed was validated and the airport created
                    if(this.latitude != -1 && this.longitude != -1){
                        // gets the coordinates from the file to update it
                        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(".//data//coordinates.bin"))) {
                            coordinates = (ArrayList<Coordinates>) oos.readObject();
                            oos.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        // updates the file with the coordinates of the new airport
                        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(".//data//coordinates.bin"))) {
                            coordinates.add(new Coordinates(airportCode.getText().toUpperCase(), this.longitude, this.latitude));
                            oos.writeObject(coordinates);
                            oos.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        AirportNetworkController.setUpdate(); // tells the airport network page controller that the user added a new airport
                        // close the dialog window
                        airportName.getParent().getParent().getScene().getWindow().hide();
                    }
                }
            }
        }
    }

    public void setEdit(Airport airport){
        this.airport = airport;
        this.isEdit = true;
    }
}
