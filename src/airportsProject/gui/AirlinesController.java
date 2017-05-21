package airportsProject.gui;

import airportsProject.Airline;
import airportsProject.Utils;
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
import libs.SeparateChainingHashST;

import java.io.IOException;
import java.util.Optional;

import static javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;

/**
 * Shows all the airlines existent and lets the user create a new one and remove an airline or see its details
 */
public class AirlinesController {
    @FXML
    private VBox airlinesContainer;
    @FXML
    private Pane newAirline;

    private int airlineId = 0; // used to find the airline on the list when updating it

    Utils utils = Utils.getInstance();
    SeparateChainingHashST<String, Airline> airlines = utils.getAirlines();

    public void initialize(){
        if(!airlines.isEmpty()){
            for(String name : airlines.keys()){
                Airline airline = airlines.get(name);
                newAirlineItem(airline);
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
            Label noResult = new Label("No airlines found !");
            noResult.setAlignment(Pos.CENTER);
            noResult.setLayoutX(200);
            noResult.setLayoutY(90);
            noResult.setPrefWidth(213);
            noResult.setFont(Font.font("Helvetica", 20));
            noResult.setTextFill(Color.valueOf("4185d1"));
            noResult.setWrapText(true);
            newPane.getChildren().add(noResult);
            VBox.setMargin(newPane, new Insets(10,0,0,0));
            airlinesContainer.getChildren().add(newPane);
        }
    }

    private void updateList(){
        airlinesContainer.getChildren().remove(0, airlineId); // removes the previous list with the removed airline still showing
        airlineId = 0; // resets the id counter
        if(!airlines.isEmpty()){
            for(String name : airlines.keys()){
                Airline airline = airlines.get(name);
                newAirlineItem(airline);
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
            Label noResult = new Label("No airlines found !");
            noResult.setAlignment(Pos.CENTER);
            noResult.setLayoutX(200);
            noResult.setLayoutY(90);
            noResult.setPrefWidth(213);
            noResult.setFont(Font.font("Helvetica", 20));
            noResult.setTextFill(Color.valueOf("4185d1"));
            noResult.setWrapText(true);
            newPane.getChildren().add(noResult);
            VBox.setMargin(newPane, new Insets(10,0,0,0));
            airlinesContainer.getChildren().add(newPane);
        }
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void hoverIn(MouseEvent event) {
        newAirline.setStyle("-fx-opacity: 0.7");
    }

    @FXML
    void hoverOut(MouseEvent event) {
        newAirline.setStyle("-fx-opacity: 1");
    }

    @FXML
    void newAirline(MouseEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        dialog.initOwner(airlinesContainer.getScene().getWindow());
        dialog.setTitle("New Airline");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newAirlineDialog.fxml"));
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
        stage.getIcons().add(new Image(this.getClass().getResource("images/airline.png").toString()));
        Optional<ButtonType> result = dialog.showAndWait();
        // if the user closes the dialog, the list of airlines will update
        if(!result.isPresent()){
            updateList();
        }
    }

    private boolean removeThis = false;
    private void newAirlineItem(Airline airline){
        // remove airline
        Label removeAirline = new Label("remove");
        removeAirline.setAlignment(Pos.CENTER_RIGHT);
        removeAirline.setPrefWidth(93.0);
        removeAirline.setPrefHeight(40.0);
        removeAirline.setTextFill(Color.valueOf("4185d1"));
        removeAirline.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        removeAirline.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // tells the event that handles the pane clicking that it was the element "remove" that was clicked inside the pane
                // otherwise the pane would only know it was a click on the pane and would go to the airline details
                removeThis = true;
            }
        });
        Pane newPane = new Pane();
        newPane.setPrefWidth(480.0);
        newPane.setPrefHeight(40.0);
        newPane.setNodeOrientation(LEFT_TO_RIGHT);
        newPane.setId(String.valueOf(airlineId));
        newPane.setCursor(Cursor.HAND);
        newPane.getStyleClass().add("item");
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
        // checks id to select the airline clicked to show its details or remove it
        newPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(removeThis){
                    // alert to check if the user really wants to delete the airline
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete \"" + airline.getName() + "\" airline ?");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Utils.removeAirline(airline);
                        updateList();
                    }
                    removeThis = false; // reset variable
                }else{
                    VistaNavigator.loadVista(VistaNavigator.AIRLINEDETAILS, airline.getName());
                }
            }
        });
        airlineId += 1;
        // HBox
        HBox newHBox = new HBox();
        newHBox.setPrefWidth(480.0);
        newHBox.setPrefHeight(40.0);
        // airline name
        Label airlineName = new Label(airline.getName());
        airlineName.setAlignment(Pos.CENTER);
        airlineName.setPrefWidth(180.0);
        airlineName.setPrefHeight(40.0);
        airlineName.setTextFill(Color.valueOf("4185d1"));
        airlineName.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        newHBox.getChildren().add(airlineName);
        // nationality label
        Label nationality = new Label("NATIONALITY :");
        nationality.setAlignment(Pos.CENTER);
        nationality.setPrefWidth(79.0);
        nationality.setPrefHeight(40.0);
        nationality.setTextFill(Color.valueOf("8f8f8f"));
        nationality.setFont(Font.font("Helvetica", FontWeight.LIGHT, 9));
        newHBox.getChildren().add(nationality);
        // airline nationality
        Label airlineNationality = new Label(airline.getNationality());
        airlineNationality.setAlignment(Pos.CENTER);
        airlineNationality.setPrefWidth(95.0);
        airlineNationality.setPrefHeight(40.0);
        airlineNationality.setTextFill(Color.valueOf("4185d1"));
        airlineNationality.setFont(Font.font("Helvetica", FontWeight.LIGHT, 14));
        newHBox.getChildren().add(airlineNationality);
        newHBox.getChildren().add(removeAirline);
        newPane.getChildren().add(newHBox);
        airlinesContainer.getChildren().add(newPane);
    }
}
