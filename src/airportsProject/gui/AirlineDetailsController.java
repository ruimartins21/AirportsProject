package airportsProject.gui;

import airportsProject.Airline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;

public class AirlineDetailsController {
    @FXML
    private VBox airlinesContainer;

    private int airlineId = 0;

    public void initialize(){
        System.out.println("Airline nº " + airlineId);

        Airline airline1 = new Airline("Nome1 muito muito muito muito muito grande", "Nacionalidade 1");
        Airline airline2 = new Airline("Nome2", "Nacionalidade 2");
        Airline airline3 = new Airline("Nome3", "Nacionalidade 3");
        Airline airline4 = new Airline("Nome4", "Nacionalidade 4");

        ObservableList<Airline> airlinesList = FXCollections.observableArrayList();
        airlinesList.add(airline1);
        airlinesList.add(airline2);
        airlinesList.add(airline3);
        airlinesList.add(airline4);
        for (Airline airline : airlinesList) {
            newAirlineItem(airline);
        }
    }

    public void setId(int id){
        this.airlineId = id;
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void hoverIn(MouseEvent event) {
//        newAirline.setStyle("-fx-opacity: 0.7");
    }

    @FXML
    void hoverOut(MouseEvent event) {
//        newAirline.setStyle("-fx-opacity: 1");
    }

    @FXML
    void newFlight(MouseEvent event) {
        System.out.println("+ New Airline");
    }

    private void newAirlineItem(Airline airline){
        Pane newPane = new Pane();
        newPane.setStyle("-fx-background-color: F9F9F9;-fx-border-color: #F0F0F0; -fx-border-width: 1;");
        newPane.setPrefWidth(480.0);
        newPane.setPrefHeight(40.0);
        newPane.setNodeOrientation(LEFT_TO_RIGHT);
        newPane.setId(String.valueOf(airlineId));
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
                newPane.setStyle("-fx-background-color: white;");
            }
        });
        // checks id to select the flight clicked to show its details
        newPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Clicked " + newPane.getId());
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
                System.out.println("Clicked " + newPane.getId() + " to remove");
                // fazer alert para verificar se quer mesmo cancelar
            }
        });
        newHBox.getChildren().add(removeAirline);
        newPane.getChildren().add(newHBox);
        airlinesContainer.getChildren().add(newPane);
    }
}
