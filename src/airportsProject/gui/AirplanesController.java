package airportsProject.gui;

import airportsProject.Airplane;
import airportsProject.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import libs.RedBlackBST;

public class AirplanesController {
    @FXML
    private VBox airplanesContainer;
    @FXML
    private Pane newAirplane;
    @FXML
    private TextField searchAirplane;

    Utils utils = Utils.getInstance();
    RedBlackBST<Integer, Airplane> airplanes = utils.getAirplanes();

    public void initialize(){
        searchAirplane.getParent().requestFocus();
        searchAirplane.setFocusTraversable(false);

        ObservableList<Airplane> airplanesList = FXCollections.observableArrayList();
        for (int id : airplanes.keys()) {
            Airplane airplane = airplanes.get(id);
            airplanesList.add(airplane);
            newAirplaneItem(airplane);
        }
    }

    private void updateList(){
        airplanesContainer.getChildren().remove(1, airplanes.size()+2); // removes the previous list with the removed airline still showing
        for(int id : airplanes.keys()){ // lists all the existent airlines
            Airplane airplane = airplanes.get(id);
            newAirplaneItem(airplane);
        }
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void hoverIn(MouseEvent event) {
        newAirplane.setStyle("-fx-opacity: 0.7");
    }

    @FXML
    void hoverOut(MouseEvent event) {
        newAirplane.setStyle("-fx-opacity: 1");
    }

    @FXML
    void getInput(ActionEvent actionEvent){
        System.out.println("Searched for: \"" + searchAirplane.getText() + "\"");
    }

    @FXML
    void newAirplane(MouseEvent event) {
        System.out.println("+ New Airplane");
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
        // airline label
        Label airlineLabel = new Label("AIRLINE :");
        airlineLabel.setAlignment(Pos.CENTER);
        airlineLabel.setPrefWidth(45.0);
        airlineLabel.setPrefHeight(40.0);
        airlineLabel.setTextFill(Color.valueOf("8f8f8f"));
        airlineLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 9));
        newHBox.getChildren().add(airlineLabel);
        // airline
//        Label airline = new Label(airplane.getAirline().getName());
        Label airline = new Label(airplane.getAirline().getName());
        airline.setAlignment(Pos.CENTER);
        airline.setPrefWidth(111.0);
        airline.setPrefHeight(40.0);
        airline.setTextFill(Color.valueOf("4185d1"));
        airline.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        newHBox.getChildren().add(airline);
        newHBox.getChildren().add(removeAirplane);
        newPane.getChildren().add(newHBox);
        airplanesContainer.getChildren().add(newPane);
    }
}
