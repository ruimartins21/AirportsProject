package airportsProject.gui;

import airportsProject.Airplane;
import airportsProject.Airport;
import airportsProject.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class AirplaneDetailsController {
    @FXML
    private VBox containAirplaneDetails;
    @FXML
    private Pane editAirplane;
    @FXML
    private Pane removeAirplane;
    @FXML
    private Label airplaneName;
    @FXML
    private Label airplaneModel;
    @FXML
    private Label airplaneAirline;
    @FXML
    private Label airplaneCruiseSpeed;
    @FXML
    private Label airplaneCruiseAltitude;
    @FXML
    private Label airplaneMaxDistance;
    @FXML
    private Label airplanePassengers;
    @FXML
    private Label airplaneFuelCap;
    @FXML
    private Label airplaneParked;

    private int airplaneId = -1;
    Utils utils = Utils.getInstance();
    RedBlackBST<Integer, Airplane> airplanes = utils.getAirplanes();
    SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    Airplane airplane;

    // format float numbers
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    public void initialize(){
        // set separator symbol for large numbers: 1000 -> 1 000
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        airplane = airplanes.get(airplaneId-1); // gets the requested airplane data
        // fill the labels with the airplane info
        airplaneName.setText(airplane.getName().toUpperCase());
        airplaneModel.setText(airplane.getModel());
        airplaneAirline.setText(airplane.getAirline().getName());
        airplaneCruiseSpeed.setText(formatter.format(airplane.getCruiseSpeed()) + " km/h");
        airplaneCruiseAltitude.setText(formatter.format(airplane.getCruiseAltitude()) + " km");
        airplaneMaxDistance.setText(formatter.format(airplane.getMaxRange()) + " km");
        airplanePassengers.setText(String.valueOf(airplane.getPassengersCapacity()));
        airplaneFuelCap.setText(formatter.format(airplane.getFuelCapacity()) + " L");
        airplaneParked.setText(airports.get(airplane.getAirportCode()).getName());
    }

    public void setId(int id){
        this.airplaneId = id;
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void gotoAirline(MouseEvent event){
        VistaNavigator.loadVista(VistaNavigator.AIRLINEDETAILS, airplane.getAirline().getName());
    }

    @FXML
    void gotoAirport(MouseEvent event){
        VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, airplane.getAirportCode());
    }

    @FXML
    void hoverIn(MouseEvent event) {
        if(event.getSource().equals(editAirplane)){
            editAirplane.setStyle("-fx-background-color: rgba(270,270,270,0.2);");
        }else{
            removeAirplane.setStyle("-fx-background-color: rgba(270,270,270,0.2);");
        }
    }

    @FXML
    void hoverOut(MouseEvent event) {
        if(event.getSource().equals(editAirplane)){
            editAirplane.setStyle("-fx-background-color: transparent;");
        }else{
            removeAirplane.setStyle("-fx-background-color: transparent;");
        }
    }

    @FXML
    void optionClicked(MouseEvent event) {
        if(event.getSource().equals(editAirplane)){
            Dialog<ButtonType> dialog = new Dialog<>();
            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide());
            dialog.initOwner(containAirplaneDetails.getScene().getWindow());
            dialog.setTitle("Edit Airplane");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("newAirplaneDialog.fxml"));
            fxmlLoader.setControllerFactory((Class<?> controllerType) -> {
                if (controllerType == NewAirplaneDialogController.class) { // send the code of the airport to show its details
                    NewAirplaneDialogController controller = new NewAirplaneDialogController();
                    controller.setEdit(airplane);
                    return controller;
                }else{
                    try {
                        return controllerType.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            try{
                dialog.getDialogPane().setContent(fxmlLoader.load());
            }catch(IOException e) {
                e.printStackTrace();
                return;
            }
            dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
            dialog.getDialogPane().getStyleClass().add("customDialog");
            dialog.setContentText(null);
            Optional<ButtonType> result = dialog.showAndWait();
            // if the user closes the dialog, the airplane informations will update
            if(!result.isPresent()){
                airplaneName.setText(airplanes.get(airplane.getId()-1).getName().toUpperCase());
                airplaneModel.setText(airplanes.get(airplane.getId()-1).getModel());
                airplaneCruiseSpeed.setText(formatter.format(airplanes.get(airplane.getId()-1).getCruiseSpeed()) + " km/h");
                airplaneCruiseAltitude.setText(formatter.format(airplanes.get(airplane.getId()-1).getCruiseAltitude()) + " km");
                airplaneMaxDistance.setText(formatter.format(airplanes.get(airplane.getId()-1).getMaxRange()) + " km");
                airplanePassengers.setText(String.valueOf(airplanes.get(airplane.getId()-1).getPassengersCapacity()));
                airplaneFuelCap.setText(formatter.format(airplanes.get(airplane.getId()-1).getFuelCapacity()) + " L");
            }
        }else{
            // alert to check if the user really wants to delete the airline
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
            // style the alert
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete \"" + airplane.getName() + "\" airplane ?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Utils.removeAirplane(airplane);
                VistaNavigator.loadVista(VistaNavigator.AIRPLANELIST);
            }
        }
    }
}
