package airportsProject.gui;

import airportsProject.Airline;
import airportsProject.Airplane;
import airportsProject.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;

import java.io.IOException;
import java.util.Optional;

/**
 * Airline Details page
 * Shows the details for an airline like its fleet, its size and the list of airplanes that it got
 */
public class AirlineDetailsController {
    @FXML
    private VBox airlinesContainer;
    @FXML
    private Label airlineName;
    @FXML
    private Pane editAirline;
    @FXML
    private Pane removeAirline;
    @FXML
    private Label fleetSizeNumber;

    private Utils utils = Utils.getInstance();
    SeparateChainingHashST<String, Airline> airlines = utils.getAirlines();
    Airline airline;

    public void initialize(){
        airlineName.setText(String.valueOf(airline.getName()));
        fleetSizeNumber.setText(airline.getFleet().size() + " airplanes");

        RedBlackBST<Integer, Airplane> fleet = airline.getFleet();
        for(int id : fleet.keys()){
            Airplane airplane = fleet.get(id);
            newAirplaneItem(airplane);
        }
    }

    private void updateList(){
        airlinesContainer.getChildren().clear();
        RedBlackBST<Integer, Airplane> fleet = airline.getFleet();
        for(int id : fleet.keys()){
            newAirplaneItem(fleet.get(id));
        }
    }

    public void setAirline(String airlineName){
        this.airline = airlines.get(airlineName);
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void hoverIn(MouseEvent event) {
        if(event.getSource().equals(editAirline)){
            editAirline.setStyle("-fx-background-color: rgba(270,270,270,0.2);");
        }else{
            removeAirline.setStyle("-fx-background-color: rgba(270,270,270,0.2);");
        }
    }

    @FXML
    void hoverOut(MouseEvent event) {
        if(event.getSource().equals(editAirline)){
            editAirline.setStyle("-fx-background-color: transparent;");
        }else{
            removeAirline.setStyle("-fx-background-color: transparent;");
        }
    }

    @FXML
    void optionClicked(MouseEvent event) {
        if(event.getSource().equals(editAirline)){ // edit airline
            Dialog<ButtonType> dialog = new Dialog<>();
            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide()); // enables the user to close the dialog on the close button of the O.S.
            dialog.initOwner(airlinesContainer.getScene().getWindow()); // confines the new window to an owner so the user can't interact with anything else while this window is open
            dialog.setTitle("Edit Airline");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("newAirlineDialog.fxml")); // chooses the fxml to load
            try{
                dialog.getDialogPane().setContent(fxmlLoader.load());
            }catch(IOException e) {
                e.printStackTrace();
                return;
            }
            NewAirlineDialogController controller = fxmlLoader.getController(); // enables the preparation of data needed for editing beforehand
            controller.setInputs(airline.getName(), airline.getNationality());
            dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
            dialog.getDialogPane().getStyleClass().add("customDialog");
            dialog.setContentText(null);
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("images/airline.png").toString()));
            Optional<ButtonType> result = dialog.showAndWait();
            // everytime, if the user inserts a new airline or not, when the dialog is closed, if the user edited the name, it must reopen the details of that airline
            if(!result.isPresent()){
                if(controller.getInitialName().compareTo(airline.getName()) != 0)
                    VistaNavigator.loadVista(VistaNavigator.AIRLINELIST);
            }
        }else{ // delete airline
            // alert to check if the user really wants to delete the airline
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete \"" + airline.getName() + "\" airline ?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Utils.removeAirline(airline);
                VistaNavigator.loadVista(VistaNavigator.AIRLINELIST);
            }
        }
    }

    private boolean removeThis = false;
    private void newAirplaneItem(Airplane airplane){
        // remove airplane
        Label removeAirplane = new Label("remove");
        removeAirplane.setAlignment(Pos.CENTER_RIGHT);
        removeAirplane.setPrefWidth(38.0);
        removeAirplane.setPrefHeight(40.0);
        removeAirplane.setTextFill(Color.valueOf("4185d1"));
        removeAirplane.setFont(Font.font("Helvetica", FontWeight.LIGHT, 10));
        removeAirplane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // tells the event that handles the pane clicking that it was the element "remove" that was clicked inside the pane
                // otherwise the pane would only know it was a click on the pane and would go to the airplane details
                removeThis = true;
            }
        });
        // container pane of a single airplane
        Pane newPane = new Pane();
        newPane.getStyleClass().add("item");
        newPane.setPrefWidth(480.0);
        newPane.setPrefHeight(40.0);
        newPane.setId(String.valueOf(airplane.getId()));
        newPane.setCursor(Cursor.HAND);
        newPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newPane.setStyle("-fx-background-color: #f2f2f2;");
            }
        });
        newPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newPane.setStyle("-fx-background-color: #F9F9F9;");
            }
        });
        // checks id to select the airplane clicked to show its details
        newPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(removeThis){
                    // alert to check if the user really wants to delete the airline
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
                    // style the alert
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete \"" + airplane.getName() + "\" airplane ?");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Utils.removeAirplane(airplane);
                        updateList();
                    }
                    removeThis = false; // reset variable
                }else{
                    VistaNavigator.loadVista(VistaNavigator.AIRPLANEDETAILS, airplane.getId());
                }
            }
        });
        // HBox
        HBox newHBox = new HBox();
        newHBox.setPrefWidth(480.0);
        newHBox.setPrefHeight(40.0);
        // airplane id
        Label airplaneId = new Label(String.valueOf(airplane.getId()));
        airplaneId.setAlignment(Pos.CENTER);
        airplaneId.setPrefWidth(30.0);
        airplaneId.setPrefHeight(40.0);
        airplaneId.setTextFill(Color.valueOf("4185d1"));
        airplaneId.setFont(Font.font("Helvetica", FontWeight.BOLD, 17));
        newHBox.getChildren().add(airplaneId);
        // airplane model
        Label airplaneModel = new Label(airplane.getModel().toUpperCase());
        airplaneModel.setAlignment(Pos.CENTER);
        airplaneModel.setPrefWidth(70.0);
        airplaneModel.setPrefHeight(40.0);
        airplaneModel.setTextFill(Color.valueOf("8f8f8f"));
        airplaneModel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 9));
        newHBox.getChildren().add(airplaneModel);
        // airplane name
        Label airplaneName = new Label(airplane.getName());
        airplaneName.setAlignment(Pos.CENTER_LEFT);
        airplaneName.setPrefWidth(181.0);
        airplaneName.setPrefHeight(40.0);
        airplaneName.setTextFill(Color.valueOf("4185d1"));
        airplaneName.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        newHBox.getChildren().add(airplaneName);
        // spacing label
        Label spacing = new Label("");
        spacing.setPrefWidth(150);
        newHBox.getChildren().add(spacing);
        newHBox.getChildren().add(removeAirplane);
        newPane.getChildren().add(newHBox);
        airlinesContainer.getChildren().add(newPane);
    }
}
