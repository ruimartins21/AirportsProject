package airportsProject.gui;

import airportsProject.Airplane;
import airportsProject.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import java.io.IOException;
import java.util.Optional;

/**
 * List of existent airplanes
 * In this page the user can search for an airplane by its ID, name, airline, the code of the airport where the plane
 * is parked and their model
 * the user can add and remove an airplane and see details of it
 */
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
        if(!airplanes.isEmpty()){
            for (int id : airplanes.keys()) {
                Airplane airplane = airplanes.get(id);
                newAirplaneItem(airplane);
            }
        }else{
            Pane newPane = new Pane();
            newPane.setPrefHeight(200);
            newPane.setPrefWidth(480);
            ImageView icon = new ImageView();
            icon.setImage(new Image("airportsProject/gui/images/noResults.png"));
            icon.setFitHeight(92);
            icon.setFitWidth(92);
            icon.setLayoutX(80);
            icon.setLayoutY(53);
            newPane.getChildren().add(icon);
            Label noResult = new Label("No airplanes found !");
            noResult.setAlignment(Pos.CENTER);
            noResult.setLayoutX(200);
            noResult.setLayoutY(90);
            noResult.setPrefWidth(213);
            noResult.setFont(Font.font("Helvetica", 20));
            noResult.setTextFill(Color.valueOf("4185d1"));
            noResult.setWrapText(true);
            newPane.getChildren().add(noResult);
            VBox.setMargin(newPane, new Insets(10,0,0,0));
            airplanesContainer.getChildren().add(newPane);
        }
    }

    private void updateList(){
        airplanesContainer.getChildren().clear(); // removes the previous list with the removed airline still showing
        searchAirplane.setText("");
        if(!airplanes.isEmpty()){
            for(int id : airplanes.keys()){ // lists all the existent airlines
                Airplane airplane = airplanes.get(id);
                newAirplaneItem(airplane);
            }

        }else{
            Pane newPane = new Pane();
            newPane.setPrefHeight(200);
            newPane.setPrefWidth(480);
            ImageView icon = new ImageView();
            icon.setImage(new Image("airportsProject/gui/images/noResults.png"));
            icon.setFitHeight(92);
            icon.setFitWidth(92);
            icon.setLayoutX(80);
            icon.setLayoutY(53);
            newPane.getChildren().add(icon);
            Label noResult = new Label("No airplanes found !");
            noResult.setAlignment(Pos.CENTER);
            noResult.setLayoutX(200);
            noResult.setLayoutY(90);
            noResult.setPrefWidth(213);
            noResult.setFont(Font.font("Helvetica", 20));
            noResult.setTextFill(Color.valueOf("4185d1"));
            noResult.setWrapText(true);
            newPane.getChildren().add(noResult);
            VBox.setMargin(newPane, new Insets(10,0,0,0));
            airplanesContainer.getChildren().add(newPane);
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

    private void getResults(String search){
        search = search.toUpperCase();
        RedBlackBST<Integer, Airplane> resultAirplanes = new RedBlackBST<>();
        for(int id : airplanes.keys()){
            // searches the keyword occurrence on the airports
            if(Utils.isNumeric(search) && Integer.valueOf(search).compareTo(id+1) == 0 ||
                    search.compareTo(airplanes.get(id).getName().toUpperCase()) == 0 ||
                    search.compareTo(airplanes.get(id).getAirportCode()) == 0 ||
                    search.compareTo(airplanes.get(id).getModel().toUpperCase()) == 0 ||
                    search.compareTo(airplanes.get(id).getAirline().getName().toUpperCase()) == 0){
                resultAirplanes.put(id, airplanes.get(id));
            }
        }
        airplanesContainer.getChildren().clear(); // removes the previous list
        if(!resultAirplanes.isEmpty()){
            for(int id : resultAirplanes.keys()){ // lists all the existent airports
                Airplane airplane = resultAirplanes.get(id);
                newAirplaneItem(airplane);
            }
        }else{
            Pane newPane = new Pane();
            newPane.setPrefHeight(200);
            newPane.setPrefWidth(480);
            ImageView icon = new ImageView();
            icon.setImage(new Image("airportsProject/gui/images/noResults.png"));
            icon.setFitHeight(92);
            icon.setFitWidth(92);
            icon.setLayoutX(80);
            icon.setLayoutY(53);
            newPane.getChildren().add(icon);
            Label noResult = new Label("Sorry we couldn't find any matches for that search");
            noResult.setAlignment(Pos.CENTER);
            noResult.setLayoutX(200);
            noResult.setLayoutY(70);
            noResult.setPrefWidth(213);
            noResult.setFont(Font.font("Helvetica", 20));
            noResult.setTextFill(Color.valueOf("4185d1"));
            noResult.setWrapText(true);
            newPane.getChildren().add(noResult);
            VBox.setMargin(newPane, new Insets(10,0,0,0));
            airplanesContainer.getChildren().add(newPane);
        }
    }

    @FXML
    void getInput(ActionEvent actionEvent){
        if(searchAirplane.getText().trim().length() != 0){
            getResults(searchAirplane.getText());
            searchAirplane.setText("");
        }
    }

    @FXML
    void clearSearch(){
        updateList();
    }

    @FXML
    void newAirplane(MouseEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        dialog.initOwner(airplanesContainer.getScene().getWindow());
        dialog.setTitle("New Airplane");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newAirplaneDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e) {
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
        dialog.getDialogPane().getStyleClass().add("customDialog");
        dialog.setContentText(null);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("images/aeroplane.png").toString()));
        Optional<ButtonType> result = dialog.showAndWait();
        // if the user closes the dialog, the list of airplanes will update
        if(!result.isPresent()){
            updateList();
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
