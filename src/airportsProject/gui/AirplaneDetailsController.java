package airportsProject.gui;

import airportsProject.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

/**
 *  Shows a certain airplane details aswell as options to edit or remove it, and it's flight history
 */
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
    @FXML
    private VBox containFlights;

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

        for(Date date : airplane.getAirplaneFlights().keys()){
            Flight flight = airplane.getAirplaneFlights().get(date);
            containFlights.getChildren().add(newFlightItem(flight));
        }
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

    private Pane newFlightItem(Flight flight){
        // container pane of a single flight
        Pane newPane = new Pane();
        newPane.setLayoutX(17);
        // HBox
        HBox newHBox = new HBox();
        newHBox.setPrefWidth(425.0);
        newHBox.setPrefHeight(40.0);
        newHBox.setLayoutX(11.0);
        newHBox.setAlignment(Pos.CENTER);
        newHBox.getStyleClass().add("item");
        newHBox.setPadding(new Insets(0,5,0,5));
        newHBox.setId(String.valueOf(flight.getDate()));
        newHBox.setCursor(Cursor.HAND);
        newHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newHBox.setStyle("-fx-background-color: #f2f2f2;");
            }
        });
        newHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newHBox.setStyle("-fx-background-color: #F9F9F9;");
            }
        });
        // checks id to select the airplane clicked to show its details
        newHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VistaNavigator.loadVista(VistaNavigator.FLIGHTDETAILS, flight.getDate());
            }
        });
        // flight date
        Label flightDate = new Label(flight.getDate().getDateLess());
        flightDate.setTextFill(Color.valueOf("4185d1"));
        flightDate.setPadding(new Insets(0, 10, 0, 0));
        flightDate.setFont(Font.font("Helvetica", 15));
        newHBox.getChildren().add(flightDate);
        // flight duration
        Label flightDuration = new Label(flight.getDuration().getDurationLess());
        flightDuration.setTextFill(Color.valueOf("9a9a9a"));
        flightDuration.setPadding(new Insets(0, 20, 0, 0));
        flightDuration.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        newHBox.getChildren().add(flightDuration);
        // "to" label
        Label airportLabel = new Label("to :");
        airportLabel.setTextFill(Color.valueOf("9a9a9a"));
        airportLabel.setPadding(new Insets(0, 10, 0, 0));
        airportLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        newHBox.getChildren().add(airportLabel);
        // airport name
        Label airportName = new Label(flight.getAirportOfDestination().getName());
        airportName.setMaxWidth(150);
        airportName.setTextFill(Color.valueOf("5e5d5d"));
        airportName.setFont(Font.font("Helvetica", 15));
        newHBox.getChildren().add(airportName);
        newPane.getChildren().add(newHBox);
        return newPane;
    }
}
