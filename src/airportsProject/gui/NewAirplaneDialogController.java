package airportsProject.gui;

import airportsProject.Airline;
import airportsProject.Airplane;
import airportsProject.Airport;
import airportsProject.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import libs.SeparateChainingHashST;

import static airportsProject.Utils.isNumeric;

public class NewAirplaneDialogController{
    @FXML
    private TextField airplaneName;
    @FXML
    private TextField airplaneModel;
    @FXML
    private TextField airplaneCruiseSpeed;
    @FXML
    private TextField airplaneCruiseAltitude;
    @FXML
    private TextField airplaneMaxRange;
    @FXML
    private TextField airplanePassengers;
    @FXML
    private TextField airplaneFuel;
    @FXML
    private ComboBox<String> airplaneAirline;
    @FXML
    private ComboBox<String> airplaneAirport;
    @FXML
    private Label warning;

    private boolean isEdit = false;
    private Airplane airplane;
    private Utils utils = Utils.getInstance();
    private SeparateChainingHashST<String, Airline> airlines = utils.getAirlines();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();

    public void initialize(){
        ObservableList<String> airlinesList = FXCollections.observableArrayList();
        for(String name : airlines.keys()){
            airlinesList.add(name + " (" + airlines.get(name).getNationality() + ")");
        }
        ObservableList<String> airportsList = FXCollections.observableArrayList();
        for (int i = 0; i < utils.getSymbolGraph().G().V(); i++) {
            Airport airport = airports.get(utils.getSymbolGraph().nameOf(i));
            airportsList.add(airport.getCode() + " - " + airport.getName() + " (" + airport.getCountry() + ")");
        }
        airplaneAirline.setItems(airlinesList);
        airplaneAirline.getItems().add(0, "Select Airline");
        airplaneAirline.getSelectionModel().selectFirst();
        airplaneAirline.setFocusTraversable(false);
        airplaneAirport.setItems(airportsList);
        airplaneAirport.getItems().add(0, "Select Airport");
        airplaneAirport.getSelectionModel().selectFirst();
        airplaneAirport.setFocusTraversable(false);

        if(isEdit){
            airplaneName.setText(airplane.getName());
            airplaneModel.setText(airplane.getModel());
            airplaneCruiseAltitude.setText(String.valueOf(airplane.getCruiseAltitude()));
            airplaneCruiseSpeed.setText(String.valueOf(airplane.getCruiseSpeed()));
            airplaneFuel.setText(String.valueOf(airplane.getFuelCapacity()));
            airplanePassengers.setText(String.valueOf(airplane.getPassengersCapacity()));
            airplaneMaxRange.setText(String.valueOf(airplane.getMaxRange()));
            airplaneAirline.getSelectionModel().select(airplane.getAirline().getName());
            airplaneAirport.getSelectionModel().select(airplane.getAirportCode() + " - " + airports.get(airplane.getAirportCode()).getName()
                                                        + " (" + airports.get(airplane.getAirportCode()).getCountry() + ")");
            airplaneAirline.setDisable(true);
            airplaneAirport.setDisable(true);
        }
        warning.setStyle("-fx-opacity: 0");
    }

    @FXML
    void getNewAirplaneInput(ActionEvent event) {
        if(isEdit){ // editing an airplane
            if(airplaneName.getText().trim().length() == 0 || airplaneModel.getText().trim().length() == 0 ||
                    !isNumeric(airplaneCruiseSpeed.getText().replaceAll(" ", "")) || !isNumeric(airplaneMaxRange.getText().replaceAll(" ", "")) ||
                    !isNumeric(airplanePassengers.getText().replaceAll(" ", "")) || !isNumeric(airplaneFuel.getText().replaceAll(" ", "")) ||
                    !isNumeric(airplaneCruiseAltitude.getText().replaceAll(" ", ""))){
                warning.setStyle("-fx-opacity: 1");
            }else{
                warning.setStyle("-fx-opacity: 0");
                utils.editAirplane(airplane.getId(), airplaneModel.getText(), airplaneName.getText(),
                        Float.valueOf(airplaneCruiseSpeed.getText()), Float.valueOf(airplaneCruiseAltitude.getText()),
                        Float.valueOf(airplaneMaxRange.getText()), Integer.valueOf(airplanePassengers.getText()),
                        Integer.valueOf(airplaneFuel.getText()));
                // close the dialog window
                airplaneName.getParent().getParent().getScene().getWindow().hide();
            }
        }else{ // new airplane
            if(airplaneName.getText().trim().length() == 0 || airplaneModel.getText().trim().length() == 0 ||
                    !isNumeric(airplaneCruiseSpeed.getText()) || !isNumeric(airplaneMaxRange.getText()) ||
                    !isNumeric(airplanePassengers.getText()) || !isNumeric(airplaneFuel.getText()) ||
                    !isNumeric(airplaneCruiseAltitude.getText()) || airplaneAirline.getSelectionModel().getSelectedIndex() == 0 ||
                    airplaneAirport.getSelectionModel().getSelectedIndex() == 0){
                warning.setStyle("-fx-opacity: 1");
            }else{
                warning.setStyle("-fx-opacity: 0");
                String airlineName = "";
                int count = 1;
                for (String name : airlines.keys()){
                    if(count == airplaneAirline.getSelectionModel().getSelectedIndex()){
                        airlineName = name;
                    }
                    count++;
                }
                String airportCode = "";
                count = 1;
                for (int i = 0; i < utils.getSymbolGraph().G().V(); i++) {
                    if(count == airplaneAirport.getSelectionModel().getSelectedIndex()){
                        airportCode = utils.getSymbolGraph().nameOf(i);
                    }
                    count++;
                }
                if(!utils.addAirplane(airplaneModel.getText(), airplaneName.getText(), airlineName,
                        (float)Math.round(Float.valueOf(airplaneCruiseSpeed.getText())*10)/10f,
                        (float)Math.round(Float.valueOf(airplaneCruiseAltitude.getText())*10)/10f,
                        (float)Math.round(Float.valueOf(airplaneMaxRange.getText())*10)/10f,
                        airportCode, Integer.valueOf(airplanePassengers.getText()), Integer.valueOf(airplaneFuel.getText()))){
                    warning.setStyle("-fx-opacity: 1");
                }else{
                    // close the dialog window
                    airplaneName.getParent().getParent().getScene().getWindow().hide();
                }
            }
        }
    }

    public void setEdit(Airplane airplane){
        this.airplane = airplane;
        this.isEdit = true;
    }
}
