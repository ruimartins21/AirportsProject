package airportsProject.gui;

import airportsProject.Airplane;
import airportsProject.Airport;
import airportsProject.Date;
import airportsProject.Flight;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

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
    private Pane airplaneInfo;

    private Date date;
//    NumberFormat formatter = new DecimalFormat("#0.##");
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
    Group zoomGroup;

    private Airport airport1 = new Airport("Francisco Sá Carneiro", "OPO", "Porto", "Portugal", "Europe", 10.0f);
    private Airport airport2 = new Airport("International John Kennedy", "JFK", "New York", "USA", "America", 6.0f);
    private Airplane airplane = new Airplane(1, "model1", "Fernão Mendes Pinto", 400, 5000, 10000, "OPO", 350, 500, null);
    private Flight flight = new Flight(10000, new Date(0,0,0,9,20,10), new Date(), 300, airplane, airport1, airport2);

    public void initialize(){
        // set separator symbol for large numbers: 1000 -> 1 000
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        flight.setCosts(5000);
        // fill labels with info from the flight
        originCode.setText(flight.getAirportOfOrigin().getCode());
        originName.setText(flight.getAirportOfOrigin().getName());
        originLocation.setText(flight.getAirportOfOrigin().getCity() + ", " + flight.getAirportOfOrigin().getCountry());
        destinationCode.setText(flight.getAirportOfDestination().getCode());
        destinationName.setText(flight.getAirportOfDestination().getName());
        destinationLocation.setText(flight.getAirportOfDestination().getCity() + ", " + flight.getAirportOfDestination().getCountry());
        flightDuration.setText(flight.getDuration().getDuration());
        airplaneModel.setText(flight.getAirplane().getModel());
        airplaneName.setText(flight.getAirplane().getName());
        airplaneAirline.setText("Airline 1"); // mudar depois
        airplaneSeats.setText(String.valueOf(flight.getAirplane().getPassengersCapacity()));

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
        // connections
        for (int i = 0; i < 3; i++) {
            if(i == 1){
                refilling();
            }else{
                newConnection(flight);
            }
        }
        containFlight.getChildren().add(airplaneInfo);

        setPinLocation(airport1);
        setPinLocation(airport2);
//        Line line = new Line();
//        line.setStartX(airport1.getLongitude());
//        line.setStartY(airport1.getLatitude());
//        line.setEndX(airport2.getLongitude());
//        line.setEndY(airport2.getLatitude());
//        mapPane.getChildren().add(line);

        Path path = new Path();
        // First move to starting point
        MoveTo moveTo = new MoveTo();
        moveTo.setX(airport1.getLongitude());
        moveTo.setY(airport1.getLatitude());

        // Then start drawing a line
        LineTo lineTo = new LineTo();
        lineTo.setX(airport2.getLongitude() + 5);
        lineTo.setY(airport2.getLatitude());

        // arrow >
        Line upperLine = new Line();
        upperLine.setStartX(airport2.getLongitude() + 5);
        upperLine.setStartY(airport2.getLatitude());
        upperLine.setEndX(airport2.getLongitude() + 10);
        upperLine.setEndY(airport2.getLatitude() - 5);
        upperLine.setStroke(Color.WHITE);
        Line lowerLine = new Line();
        lowerLine.setStartX(airport2.getLongitude() + 5);
        lowerLine.setStartY(airport2.getLatitude());
        lowerLine.setEndX(airport2.getLongitude() + 10);
        lowerLine.setEndY(airport2.getLatitude() + 5);
        lowerLine.setStroke(Color.WHITE);

        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        path.setStroke(Color.WHITE);

        mapPane.getChildren().add(path);
        mapPane.getChildren().addAll(upperLine, lowerLine);
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

    // funcao de cancelar voo caso ainda nao tenha saido

    private void newConnection(Flight flight){
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
        String hour = (flight.getDate().getHour() / 10 < 1 ? "0"+flight.getDate().getHour() : String.valueOf(flight.getDate().getHour()));
        String minute = (flight.getDate().getMinute() / 10 < 1 ? "0"+flight.getDate().getMinute() : String.valueOf(flight.getDate().getMinute()));
        Label startHour = new Label(hour + ":" + minute);
        startHour.setTextFill(Color.valueOf("8a8a8a"));
        startHour.setPrefWidth(40);
        startHour.setLayoutX(79);
        startHour.setLayoutY(9);
        startHour.setAlignment(Pos.CENTER);
        startHour.setFont(Font.font("Helvetica", FontWeight.LIGHT, 15));
        leftPane.getChildren().add(startHour);
        // arriving hour
        Label endHour = new Label("14:30"); // starting hour + duration of connection
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
        Label originAirport = new Label(flight.getAirportOfOrigin().getName() + " (" + flight.getAirportOfOrigin().getCode() + ")");
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
        Label distance = new Label(formatter.format(flight.getDistance()) + " km");
        distance.setLayoutX(95);
        distance.setLayoutY(54);
        distance.setTextFill(Color.valueOf("8a8a8a"));
        distance.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(distance);
        // cost number
        Label cost = new Label(formatter.format(flight.getFlightCostEuros()) + " €");
        cost.setLayoutX(95);
        cost.setLayoutY(79);
        cost.setTextFill(Color.valueOf("8a8a8a"));
        cost.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(cost);
        // wind speed number
        Label windSpeed = new Label(formatter.format(flight.getConnection().getWindSpeed()) + " km/h");
        windSpeed.setLayoutX(95);
        windSpeed.setLayoutY(104);
        windSpeed.setTextFill(Color.valueOf("8a8a8a"));
        windSpeed.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(windSpeed);
        // altitude number
        Label altitude = new Label(formatter.format(flight.getConnection().getAltitude()) + " km");
        altitude.setLayoutX(95);
        altitude.setLayoutY(129);
        altitude.setTextFill(Color.valueOf("8a8a8a"));
        altitude.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        rightPane.getChildren().add(altitude);
        // airport of destination for this connection
        Label destinationAirport = new Label(flight.getAirportOfDestination().getName() + " (" + flight.getAirportOfDestination().getCode() + ")");
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
