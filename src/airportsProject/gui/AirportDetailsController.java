package airportsProject.gui;

import airportsProject.Airplane;
import airportsProject.Airport;
import airportsProject.Date;
import airportsProject.Flight;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class AirportDetailsController {
    @FXML
    private VBox containAirplanes;
    @FXML
    private Label airportName;
    @FXML
    private Label airportCode;
    @FXML
    private Label airportCity;
    @FXML
    private Label airportCountry;
    @FXML
    private Label airportContinent;
    @FXML
    private Label airportRating;
    @FXML
    private Pane editAirport;
    @FXML
    private Pane removeAirport;
    @FXML
    private VBox containArrivals;
    @FXML
    private VBox containDepartures;
    @FXML
    private ScrollPane map;
    @FXML
    private Label mapPin;

    Group zoomGroup;
    private String code = "";

    private Airport airport;

    public void initialize(){
        switch (code){
            case "OPO":
                airport = new Airport("Francisco Sá Carneiro", code, "Porto", "Portugal", "Europe", 10.0f);
                break;
            case "JFK":
                airport = new Airport("International John Kennedy", code, "New York", "USA", "America", 6.0f);
                break;
            case "REC":
                airport = new Airport("International from Recife", code, "Recife", "Brazil", "America", 8.5f);
                break;
        }
        // remove scroll bars and prevent scrolls with mouse on the map
        map.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        map.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        map.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
            }});

        // ir buscar airport a ST pelo code dado
        airportName.setText(airport.getName());
        airportCode.setText(airport.getCode());
        airportCity.setText(airport.getCity());
        airportCountry.setText(airport.getCountry());
        airportContinent.setText(airport.getContinent());
        airportRating.setText(String.valueOf(airport.getRating()));

        // Wrap scroll content in a Group so ScrollPane re-computes scroll bars
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map.getContent());
        map.setContent(contentGroup);
        mapPin.setVisible(false);
        if(airport.getLatitude() != -1 && airport.getLongitude() != -1)
            setPinLocation();

        // airplanes parked on this airport
        Airplane airplane1 = new Airplane(1, "Airbus A340", "Fernão Mendes Pinto", 500, 2000, 10000, "OPO", 300, 3000, null);
        Airplane airplane2 = new Airplane(2, "model2", "airplane2", 600, 1500, 8000, "FRA", 350, 2500, null);
        Airplane airplane3 = new Airplane(3, "model3", "airplane3", 400, 2500, 12000, "JFK", 280, 4000, null);
        Airplane airplane4 = new Airplane(4, "model4", "airplane4", 500, 2000, 10000, "BRA", 280, 3000, null);

        ObservableList<Airplane> airplanesList = FXCollections.observableArrayList();
        airplanesList.add(airplane1);
        airplanesList.add(airplane2);
        airplanesList.add(airplane3);
        airplanesList.add(airplane4);
        for (Airplane airplane : airplanesList) {
            newAirplaneItem(airplane);
        }

        // set airport flight history
        Flight flight1 = new Flight(1000, new Date(), new Date(10, 2, 2017, 10, 10, 10), 150);
        Flight flight2 = new Flight(20000, new Date(), new Date(), 200);
        Flight flight3 = new Flight(10000, new Date(), new Date(), 300);
        Flight flight4 = new Flight(5000, new Date(), new Date(), 350);
        Flight flight5 = new Flight(15000, new Date(), new Date(), 400);
        Flight flight6 = new Flight(15000, new Date(), new Date(), 400);
        flight2.setCosts(10000);
        flight3.setCosts(5000);
        flight4.setCosts(15000);
        flight5.setCosts(1000);

        ObservableList<Flight> flightList = FXCollections.observableArrayList();
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);
        flightList.add(flight5);
        flightList.add(flight6);
        int i = 0;
        for (Flight flight : flightList) {
//            if(flight.getAirportOfDestination().equals(airport)){
//                // arrivals
//            }else{
//                // departures
//            }
            if(i < 3){
                containArrivals.getChildren().add(newFlightItem(flight));
            }else{
                containDepartures.getChildren().add(newFlightItem(flight));
            }
            i++;
        }
    }

    /**
     * In this case of airports, the id is their code, in the airports RedBlack the code is the key used
     * @param code -> code of the airport requested
     */
    public void setCode(String code){this.code = code;}

    private void setPinLocation(){
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = airport.getLongitude() / mapWidth;
        double scrollV = airport.getLatitude() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // move the pin
        mapPin.setLayoutX(airport.getLongitude() - (34 / 2)); // 34 is the pin width, divided by 2 to set the pin bottom to the coordinate (middle of the pin)
        mapPin.setLayoutY(airport.getLatitude() - (45)); // 45 is the pin height
        mapPin.setVisible(true);
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void hoverIn(MouseEvent event) {
        if(event.getSource().equals(editAirport)){
            editAirport.setStyle("-fx-opacity: 0.7");
        }else{
            removeAirport.setStyle("-fx-opacity: 0.7");
        }
    }

    @FXML
    void hoverOut(MouseEvent event) {
        if(event.getSource().equals(editAirport)){
            editAirport.setStyle("-fx-opacity: 1");
        }else{
            removeAirport.setStyle("-fx-opacity: 1");
        }
    }

    @FXML
    void optionClicked(MouseEvent event) {
        if(event.getSource().equals(editAirport)){
            System.out.println("Editing Airport");
        }else{
            System.out.println("Removing Airport");
        }
    }

    int count = 0;
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
//                VistaNavigator.loadVista(VistaNavigator.FLIGHTDETAILS, flight.getDate());
                System.out.println("Clicked -> " + flight.getDate());
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
        // "to" or "from" label
        Label airportLabel;
//        if(flight.getAirportOfDestination().equals(airport)){
//             airportLabel = new Label("from :");
//        }else{
//             airportLabel = new Label("to :");
//        }
        if(count < 3){
            airportLabel = new Label("from :");
        }else{
            airportLabel = new Label("to :");
        }
        airportLabel.setTextFill(Color.valueOf("9a9a9a"));
        airportLabel.setPadding(new Insets(0, 10, 0, 0));
        airportLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        newHBox.getChildren().add(airportLabel);
        // airport name
        Label airportName;
//        if(flight.getAirportOfDestination().equals(airport)){
//            airportName = new Label(flight.getAirportOfOrigin().getName());
//        }else{
//            airportName = new Label(flight.getAirportOfDestination().getName());
//        }
        if(count < 3){
            airportName = new Label("Airport of Origin Very Long Long Name");
        }else{
            airportName = new Label("Airport of Destination Very Long Long Name");
        }
        airportName.setMaxWidth(150);
        airportName.setTextFill(Color.valueOf("5e5d5d"));
        airportName.setFont(Font.font("Helvetica", 15));
        newHBox.getChildren().add(airportName);
        newPane.getChildren().add(newHBox);
        count++;
        return newPane;
    }

    private void newAirplaneItem(Airplane airplane){
        // container pane of a single airplane
        Pane newPane = new Pane();
        newPane.setPrefWidth(400.0);
        newPane.setPrefHeight(40.0);
        newPane.setPadding(new Insets(0,0,0,30));
        // HBox
        HBox newHBox = new HBox();
        newHBox.setPrefWidth(425.0);
        newHBox.setPrefHeight(40.0);
        newHBox.setLayoutX(28.0);
        newHBox.setAlignment(Pos.CENTER_LEFT);
        newHBox.getStyleClass().add("item");
        newHBox.setId(String.valueOf(airplane.getId()));
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
                VistaNavigator.loadVista(VistaNavigator.AIRPLANEDETAILS, airplane.getId());
            }
        });
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
        Label airline = new Label("TAP Air Portugal");
        airline.setAlignment(Pos.CENTER);
        airline.setPrefWidth(90.0);
        airline.setPrefHeight(40.0);
        airline.setTextFill(Color.valueOf("4185d1"));
        airline.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        newHBox.getChildren().add(airline);
        newPane.getChildren().add(newHBox);
        containAirplanes.getChildren().add(newPane);
    }
}