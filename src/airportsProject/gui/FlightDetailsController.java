package airportsProject.gui;

import airportsProject.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;
import libs.SymbolEdgeWeightedDigraph;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import static airportsProject.Utils.euroValue;

/**
 * Shows the route the flight will take among other infos like detailed informations for each connection, the duration (total and for connection)
 * and the airplane used for the flight
 */
public class FlightDetailsController {
    @FXML
    private VBox containFlight;
    @FXML
    private ScrollPane map;
    @FXML
    private Pane mapPane;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Label flightDate;
    @FXML
    private Label originCode;
    @FXML
    private Label originName;
    @FXML
    private Label originLocation;
    @FXML
    private Label destinationCode;
    @FXML
    private Label destinationName;
    @FXML
    private Label destinationLocation;
    @FXML
    private Label flightDuration;
    @FXML
    private Label airplaneModel;
    @FXML
    private Label airplaneName;
    @FXML
    private Label airplaneAirline;
    @FXML
    private Label airplaneSeats;
    @FXML
    private Label airplaneUsedSeats;
    @FXML
    private Pane airplaneInfo;

    private Date date;
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
    Group zoomGroup;

    private Utils utils = Utils.getInstance();
    private RedBlackBST<Date, Flight> flights = utils.getFlights();
    private Flight flight = null;
    private SymbolEdgeWeightedDigraph symbolGraph = utils.getSymbolGraph();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private Date lastConnectionArrivalDate = null; // utility variable for a connection to know the last connection arrival time

    public void initialize(){
        // set separator symbol for large numbers: 1000 -> 1 000
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        if(date != null){
            flight = flights.get(date);
            if(flight != null){
                // fill labels with info from the flight
                flightDate.setText(date.getDateLess().toUpperCase());
                originCode.setText(flight.getAirportOfOrigin().getCode());
                originName.setText(flight.getAirportOfOrigin().getName());
                originLocation.setText(flight.getAirportOfOrigin().getCity() + ", " + flight.getAirportOfOrigin().getCountry());
                destinationCode.setText(flight.getAirportOfDestination().getCode());
                destinationName.setText(flight.getAirportOfDestination().getName());
                destinationLocation.setText(flight.getAirportOfDestination().getCity() + ", " + flight.getAirportOfDestination().getCountry());
                flightDuration.setText(flight.getDuration().getDuration());
                airplaneModel.setText(flight.getAirplane().getModel());
                airplaneName.setText(flight.getAirplane().getName());
                airplaneAirline.setText(flight.getAirplane().getAirline().getName());
                airplaneSeats.setText(String.valueOf(flight.getAirplane().getPassengersCapacity()));
                airplaneUsedSeats.setText("(Used: " + String.valueOf(flight.getPassengers()) + ")");
            }
        }

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

        containFlight.getChildren().remove(airplaneInfo);
        // list all the connections
        int comp = 0;
        for (String code : flight.getConnections()) {
            for (Connection e : symbolGraph.digraph().adj(symbolGraph.indexOf(code))) {
                if (comp + 1 >= flight.getConnections().size()) {
                } else if (symbolGraph.nameOf(e.to()).compareTo(flight.getConnections().get(comp + 1)) == 0) {
                    if(comp != 0){ // each 2 "connection" cycles we put a separator between connections
                        refilling();
                    }
                    newConnection(e);
                }
            }
            comp++;
        }
        containFlight.getChildren().add(airplaneInfo);

        Airport lastAirport = null;
        // list the connections on the map
        for (String code : flight.getConnections()) {
            Airport airport = airports.get(code);
            if(lastAirport != null){
                drawPath(lastAirport.getLongitude(), lastAirport.getLatitude(), airport.getLongitude(), airport.getLatitude());
            }
            setPinLocation(airport);
            lastAirport = airport;
        }
    }

    private void drawPath(double startX, double startY, double endX, double endY){
        if(startX == -1 || startY == -1 || endX == -1 || endY == -1) return;
        Path path = new Path();
        // First move to starting point
        MoveTo moveTo = new MoveTo();
        moveTo.setX(startX);
        moveTo.setY(startY);

        // Then start drawing a line to the destination
        LineTo lineTo = new LineTo();
        lineTo.setX(endX);
        lineTo.setY(endY);

        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        path.setStroke(Color.WHITE);
        path.setStrokeWidth(2);

        mapPane.getChildren().add(path);
    }

    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Creates a pin for a specific airport and sets it on the location of the airport on the map
     * @param airport -> airport to set its location
     */
    private void setPinLocation(Airport airport){
        Label pin = new Label("");
        pin.getStyleClass().add("pin");
        if(airport.equals(flight.getAirportOfOrigin())){
            pin.getStyleClass().add("origin");
        }else if(airport.equals(flight.getAirportOfDestination())){
            pin.getStyleClass().add("destin");
        }
        pin.setCursor(Cursor.HAND);
        pin.setId(airport.getCode());
        pin.setTooltip(new Tooltip(airport.getName()));
        pin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, pin.getId());
            }
        });
        pin.setLayoutX(airport.getLongitude() - (24 / 2)); // 24 is the pin width, divided by 2 to set the pin bottom to the coordinate (middle of the pin)
        pin.setLayoutY(airport.getLatitude() - 33); // 33 is the pin height
        pin.setVisible(true);
        mapPane.getChildren().add(pin);
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
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

    private void newConnection(Connection con){
        // container pane of a connection of a flight
        Pane newPane = new Pane();
        newPane.setPrefWidth(480.0);
        newPane.setPrefHeight(200.0);
        // hbox containing two panes of equal width
        HBox newHBox = new HBox();
        newHBox.setPrefWidth(480);
        newHBox.setPrefHeight(200);
        // left pane
        Pane leftPane = new Pane();
        leftPane.setPrefWidth(200);
        leftPane.setPrefHeight(200);
        // link image
        ImageView connection = new ImageView();
        connection.setImage(new Image("airportsProject/gui/images/connection.png"));
        connection.setFitWidth(25);
        connection.setFitHeight(132);
        connection.setLayoutX(88);
        connection.setLayoutY(33);
        leftPane.getChildren().add(connection);
        // departing hour
        Date departDate;
        if(lastConnectionArrivalDate != null){ // there was a connection before
            departDate = lastConnectionArrivalDate;
        }else{ // it is the first connection of the flight
            departDate = flight.getDate();
        }
        String hour = (departDate.getHour() / 10 < 1 ? "0"+departDate.getHour() : String.valueOf(departDate.getHour()));
        String minute = (departDate.getMinute() / 10 < 1 ? "0"+departDate.getMinute() : String.valueOf(departDate.getMinute()));
        Label startHour = new Label(hour + ":" + minute);
        startHour.setTextFill(Color.valueOf("8a8a8a"));
        startHour.setPrefWidth(40);
        startHour.setLayoutX(79);
        startHour.setLayoutY(9);
        startHour.setAlignment(Pos.CENTER);
        startHour.setFont(Font.font("Helvetica", FontWeight.LIGHT, 15));
        leftPane.getChildren().add(startHour);
        // arriving hour -> time from arrival of the last connection/departing hour + duration of connection
        Date connectionDuration = Date.convertTimeToDate(flight.getAirplane().getFlightDuration(con));
        Date arrivDate = departDate.plus(connectionDuration);
        lastConnectionArrivalDate = arrivDate; // stores the last connection arrival time
        String dhour = (arrivDate.getHour() / 10 < 1 ? "0"+arrivDate.getHour() : String.valueOf(arrivDate.getHour()));
        String dminute = (arrivDate.getMinute() / 10 < 1 ? "0"+arrivDate.getMinute() : String.valueOf(arrivDate.getMinute()));
        Label endHour = new Label(dhour + ":" + dminute);
        endHour.setTextFill(Color.valueOf("8a8a8a"));
        endHour.setPrefWidth(40);
        endHour.setLayoutX(79);
        endHour.setLayoutY(171);
        endHour.setAlignment(Pos.CENTER);
        endHour.setFont(Font.font("Helvetica", FontWeight.LIGHT, 15));
        leftPane.getChildren().add(endHour);
        newHBox.getChildren().add(leftPane);
        // right pane
        Pane rightPane = new Pane();
        rightPane.setPrefHeight(200);
        rightPane.setPrefWidth(322);
        // airport of origin
        Label originAirport = new Label(airports.get(symbolGraph.nameOf(con.from())).getName() + " (" + symbolGraph.nameOf(con.from()) + ")");
        originAirport.setTextFill(Color.valueOf("8a8a8a"));
        originAirport.setStyle("-fx-cursor: hand");
        originAirport.setMaxWidth(265);
        originAirport.setLayoutX(14);
        originAirport.setLayoutY(11);
        originAirport.setFont(Font.font("Helvetica", FontWeight.LIGHT, 14));
        originAirport.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, flight.getAirportOfOrigin().getCode());
            }
        });
        rightPane.getChildren().add(originAirport);
        // distance label
        Label distanceLabel = new Label("Distance :");
        distanceLabel.setLayoutX(14);
        distanceLabel.setLayoutY(54);
        distanceLabel.setTextFill(Color.valueOf("4185d1"));
        distanceLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(distanceLabel);
        // cost label
        Label costLabel = new Label("Cost :");
        costLabel.setLayoutX(14);
        costLabel.setLayoutY(79);
        costLabel.setTextFill(Color.valueOf("4185d1"));
        costLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(costLabel);
        // wind speed label
        Label windSpeedLabel = new Label("Wind Speed :");
        windSpeedLabel.setLayoutX(14);
        windSpeedLabel.setLayoutY(104);
        windSpeedLabel.setTextFill(Color.valueOf("4185d1"));
        windSpeedLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(windSpeedLabel);
        // altitude label
        Label altitudeLabel = new Label("Altitude :");
        altitudeLabel.setLayoutX(14);
        altitudeLabel.setLayoutY(129);
        altitudeLabel.setTextFill(Color.valueOf("4185d1"));
        altitudeLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(altitudeLabel);
        // distance number
        Label distance = new Label(formatter.format(con.weight()) + " km");
        distance.setLayoutX(95);
        distance.setLayoutY(54);
        distance.setTextFill(Color.valueOf("8a8a8a"));
        distance.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(distance);
        // cost number
        Label cost = new Label(formatter.format(euroValue * (double) Math.round(flight.getAirplane().getAirplaneCost(con) * 100) / 100f) + " €");
        cost.setLayoutX(95);
        cost.setLayoutY(79);
        cost.setTextFill(Color.valueOf("8a8a8a"));
        cost.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(cost);
        // wind speed number
        Label windSpeed = new Label(formatter.format(con.getWindSpeed()) + " km/h");
        windSpeed.setLayoutX(95);
        windSpeed.setLayoutY(104);
        windSpeed.setTextFill(Color.valueOf("8a8a8a"));
        windSpeed.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(windSpeed);
        // altitude number
        Label altitude = new Label(formatter.format(con.getAltitude()) + " km");
        altitude.setLayoutX(95);
        altitude.setLayoutY(129);
        altitude.setTextFill(Color.valueOf("8a8a8a"));
        altitude.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(altitude);
        // airport of destination for this connection
        Label destinationAirport = new Label(airports.get(symbolGraph.nameOf(con.to())).getName() + " (" + symbolGraph.nameOf(con.to()) + ")");
        destinationAirport.setTextFill(Color.valueOf("8a8a8a"));
        destinationAirport.setStyle("-fx-cursor: hand");
        destinationAirport.setMaxWidth(265);
        destinationAirport.setLayoutX(14);
        destinationAirport.setLayoutY(171);
        destinationAirport.setFont(Font.font("Helvetica", FontWeight.LIGHT, 14));
        destinationAirport.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, flight.getAirportOfDestination().getCode());
            }
        });
        rightPane.getChildren().add(destinationAirport);
        newHBox.getChildren().add(rightPane);
        newPane.getChildren().add(newHBox);
        containFlight.getChildren().add(newPane);
    }

    private void refilling(){
        Pane newPane = new Pane();
        newPane.setPrefHeight(40);
        newPane.setPrefWidth(480);
        newPane.setStyle("-fx-background-color:#4185d1");
        ImageView icon = new ImageView();
        icon.setImage(new Image("airportsProject/gui/images/refill.png"));
        icon.setFitHeight(30);
        icon.setFitWidth(29);
        icon.setLayoutX(143);
        icon.setLayoutY(5);
        newPane.getChildren().add(icon);
        Label refillLabel = new Label("Refilling the airplane");
        refillLabel.setTextFill(Color.WHITE);
        refillLabel.setLayoutX(190);
        refillLabel.setLayoutY(11);
        refillLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 15));
        newPane.getChildren().add(refillLabel);
        containFlight.getChildren().add(newPane);
    }
}
