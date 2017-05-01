package airportsProject.gui;

import airportsProject.Airport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AirportNetworkController {
    @FXML
    private VBox containAirports;
    @FXML
    private Pane newAirport;
    @FXML
    private TextField searchAirport;
    @FXML
    private ScrollPane map;
    @FXML
    private Pane mapPane;
    @FXML
    private Slider zoomSlider;

    Group zoomGroup;

    public void initialize(){
        searchAirport.getParent().requestFocus();
        searchAirport.setFocusTraversable(false);

        Airport airport1 = new Airport("Francisco Sá Carneiro", "OPO", "Porto", "Portugal", "Europe", 10.0f);
        Airport airport2 = new Airport("International John Kennedy", "JFK", "New York", "USA", "America", 6.0f);
        Airport airport3 = new Airport("International from Recife", "REC", "Recife", "Brazil", "America", 8.5f);

        ObservableList<Airport> airportsList = FXCollections.observableArrayList();
        airportsList.add(airport1);
        airportsList.add(airport2);
        airportsList.add(airport3);

        // set zoom values
        zoomSlider.setMin(0.8);
        zoomSlider.setMax(1.5);
        zoomSlider.setValue(0.8);
        zoomSlider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        // Wrap scroll content in a Group so ScrollPane re-computes scroll bars
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map.getContent());
        map.setContent(contentGroup);

        zoom(0.8);
        map.setHvalue(0.4);
        map.setVvalue(0.5);

        for (Airport airport : airportsList) {
            newAirportItem(airport);
            // set airport location on the map
            if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                setPinLocation(airport);
            }
        }
    }

    /**
     * Creates a pin for a specific airport and sets it on the location of the airport on the map
     * @param airport -> airport to set its location
     */
    private void setPinLocation(Airport airport){
        Label pin = new Label("");
        pin.getStyleClass().add("pin");
        pin.setCursor(Cursor.HAND);
        pin.setId(airport.getCode());
        pin.setTooltip(new Tooltip(airport.getName()));
        pin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, pin.getId());
            }
        });
        pin.setLayoutX(airport.getLongitude() - (34 / 2)); // 34 is the pin width, divided by 2 to set the pin bottom to the coordinate (middle of the pin)
        pin.setLayoutY(airport.getLatitude() - (45)); // 45 is the pin height
        pin.setVisible(true);
        mapPane.getChildren().add(pin);
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void newAirport(MouseEvent event){
        System.out.println("+ New Airport");
    }

    @FXML
    void getInput(ActionEvent actionEvent){
        System.out.println("Searched for: \"" + searchAirport.getText() + "\"");
    }

    @FXML
    void hoverIn(MouseEvent event) {
        newAirport.setStyle("-fx-background-color: #3A76B9");
    }

    @FXML
    void hoverOut(MouseEvent event) {
        newAirport.setStyle("-fx-background-color: #4185D1");
    }

    @FXML
    void zoomIn(MouseEvent event) {
        double sliderVal = zoomSlider.getValue();
        zoomSlider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(MouseEvent event) {
        double sliderVal = zoomSlider.getValue();
        zoomSlider.setValue(sliderVal - 0.1);
    }

    private void zoom(double scaleValue) {
        double scrollH = map.getHvalue();
        double scrollV = map.getVvalue();
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        map.setHvalue(scrollH);
        map.setVvalue(scrollV);
    }

    private boolean removeThis = false;
    private void newAirportItem(Airport airport){
        // remove airport
        Label removeAirport = new Label("remove");
        removeAirport.setLayoutX(425);
        removeAirport.setLayoutY(27);
        removeAirport.setCursor(Cursor.HAND);
        removeAirport.setTextFill(Color.valueOf("4185d1"));
        removeAirport.setFont(Font.font("Helvetica", FontWeight.EXTRA_LIGHT, 12));
        removeAirport.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // tells the event that handles the pane clicking that it was the element "remove" that was clicked inside the pane
                // otherwise the pane would only know it was a click on the pane and would go to the airplane details
                removeThis = true;
            }
        });
        // container pane of an airport
        Pane newPane = new Pane();
        newPane.setPrefWidth(480.0);
        newPane.setPrefHeight(70.0);
        newPane.getStyleClass().add("item");
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
                    System.out.println("Remove airport -> " + airport.getCode());
                    // alert para verificar se quer mesmo remover
                    removeThis = false; // reset variable
                }else{
                    VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, airport.getCode());
                }
            }
        });
        // airport code
        Label airportCode = new Label(airport.getCode());
        airportCode.setLayoutX(23);
        airportCode.setLayoutY(14);
        airportCode.setTextFill(Color.valueOf("4185d1"));
        airportCode.setFont(Font.font("Helvetica", FontWeight.LIGHT, 35));
        newPane.getChildren().add(airportCode);
        // airport name
        Label airportName = new Label(airport.getName().toUpperCase());
        airportName.setLayoutX(110);
        airportName.setLayoutY(19);
        airportName.setMaxWidth(180);
        airportName.setTextFill(Color.valueOf("797979"));
        airportName.setFont(Font.font("Helvetica", 12));
        newPane.getChildren().add(airportName);
        // airport location
        Label airportLocation = new Label(airport.getCity() + ", " + airport.getCountry());
        airportLocation.setLayoutX(110);
        airportLocation.setLayoutY(37);
        airportLocation.setMaxWidth(180);
        airportLocation.setTextFill(Color.valueOf("a1a1a1"));
        airportLocation.setFont(Font.font("Helvetica", FontWeight.LIGHT, 11));
        newPane.getChildren().add(airportLocation);
        // rating label
        Label ratingLabel = new Label("RATING :");
        ratingLabel.setLayoutX(306);
        ratingLabel.setLayoutY(30);
        ratingLabel.setTextFill(Color.valueOf("a1a1a1"));
        ratingLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 11));
        newPane.getChildren().add(ratingLabel);
        // airport rating
        Label airportRating = new Label(String.valueOf(airport.getRating()));
        airportRating.setLayoutX(356);
        airportRating.setLayoutY(24);
        airportRating.setTextFill(Color.valueOf("4185d1"));
        airportRating.setFont(Font.font("Helvetica", FontWeight.BOLD, 21));
        newPane.getChildren().add(airportRating);

        newPane.getChildren().add(removeAirport);
        containAirports.getChildren().add(newPane);
    }
}
