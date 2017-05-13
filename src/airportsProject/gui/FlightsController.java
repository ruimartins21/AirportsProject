package airportsProject.gui;

import airportsProject.Date;
import airportsProject.Flight;
import airportsProject.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import libs.RedBlackBST;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;
import static javafx.geometry.Orientation.VERTICAL;

public class FlightsController {
    @FXML
    private VBox listFlightsContainer;
    @FXML
    private TextField searchFlight;

    private Utils utils = Utils.getInstance();
    private RedBlackBST<Date, Flight> flights = utils.getFlights();

    NumberFormat formatter = new DecimalFormat("#0.##");

    public void initialize(){
        if(!flights.isEmpty()){
            Date currentDate = flights.select(0);
            newFlightDate(currentDate);
            for (Date date : flights.keys()) {
                Flight flight = flights.get(date);
                if(flight.getDate().compareDate(currentDate) != 0){
                    newFlightDate(date);
                }
                newFlightItem(flight);
                currentDate = flight.getDate();
            }
        }
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    private void updateList(){
        listFlightsContainer.getChildren().clear(); // removes the previous list
        for(Date date : flights.keys()){ // lists all the existent flights
            Flight flight = flights.get(date);
            newFlightItem(flight);
        }
        searchFlight.setText("");
    }

    @FXML
    void clearSearch(){
        updateList();
    }

    private void getResults(String search){
        search = search.toUpperCase();
        RedBlackBST<Date, Flight> resultFlights = new RedBlackBST<>();
        for(Date date : flights.keys()){
            // searches the keyword occurrence on the flights
//            if(){
//                resultFlights.put(date, flights.get(date));
//            }
        }
        listFlightsContainer.getChildren().clear(); // removes the previous list
        if(!resultFlights.isEmpty()){
            for(Date date : resultFlights.keys()){ // lists all the existent airports
                Flight flight = resultFlights.get(date);
                newFlightItem(flight);
            }
        }
    }

    @FXML
    void getInput(ActionEvent actionEvent){
        if(searchFlight.getText().trim().length() != 0){
            getResults(searchFlight.getText());
            searchFlight.setText("");
        }
    }

    private void newFlightDate(Date date){
        Pane newPane = new Pane();
        newPane.setStyle("-fx-background-color: #4185d1;");
        newPane.setPrefWidth(490.0);
        newPane.setPrefHeight(20.0);
        newPane.setNodeOrientation(LEFT_TO_RIGHT);
        Label newDateLabel = new Label(date.getDateLess());
        newDateLabel.setLayoutX(43.0);
        newDateLabel.setLayoutY(2.0);
        newDateLabel.setTextFill(Color.WHITE);
        newDateLabel.setFont(Font.font("Helvetica Light", 13.0));
        newPane.getChildren().add(newDateLabel);
        listFlightsContainer.getChildren().add(newPane);
    }

    private void newFlightItem(Flight flight){
        Pane newPane = new Pane();
        newPane.setStyle("-fx-background-color: white;");
        newPane.setPrefWidth(490.0);
        newPane.setNodeOrientation(LEFT_TO_RIGHT);
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
                VistaNavigator.loadVista(VistaNavigator.FLIGHTDETAILS, flight.getDate());
            }
        });
        // departure label
        Label departure = new Label("DEPARTURE");
        departure.setLayoutX(43.0);
        departure.setLayoutY(14.0);
        departure.setTextFill(Color.valueOf("838383"));
        departure.setFont(Font.font("Helvetica Light", 10.0));
        newPane.getChildren().add(departure);
        // departure hour
        Label departureHour = new Label(flight.getDate().getHourString() + ":" + flight.getDate().getMinuteString());
        departureHour.setLayoutX(43.0);
        departureHour.setLayoutY(26.0);
        departureHour.setTextFill(Color.valueOf("4185d1"));
        departureHour.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 35.0));
        newPane.getChildren().add(departureHour);
        // departure airport
        Label departureAirport = new Label(flight.getAirportOfOrigin().getName());
        departureAirport.setLayoutX(43.0);
        departureAirport.setLayoutY(70.0);
        departureAirport.setTextFill(Color.valueOf("838383"));
        departureAirport.setFont(Font.font("Helvetica", 13.0));
        newPane.getChildren().add(departureAirport);
        // arrow
        ImageView divideArrow = new ImageView("airportsProject/gui/images/flightSeparator.png");
        divideArrow.setFitHeight(30.0);
        divideArrow.setFitWidth(30.0);
        divideArrow.setLayoutX(222.0);
        divideArrow.setLayoutY(33.0);
        newPane.getChildren().add(divideArrow);
        // arrival label
        Label arrival = new Label("ARRIVAL");
        arrival.setLayoutX(336.0);
        arrival.setLayoutY(14.0);
        arrival.setTextFill(Color.valueOf("838383"));
        arrival.setFont(Font.font("Helvetica Light", 10.0));
        newPane.getChildren().add(arrival);
        // arrival hour
        Date arrivalDate = flight.getDate().plus(flight.getDuration());
        Label arrivalHour = new Label(arrivalDate.getHourString() + ":" + arrivalDate.getMinuteString());
        arrivalHour.setLayoutX(336.0);
        arrivalHour.setLayoutY(26.0);
        arrivalHour.setTextFill(Color.valueOf("4185d1"));
        arrivalHour.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 35.0));
        newPane.getChildren().add(arrivalHour);
        // arrival airport
        Label arrivalAirport = new Label(flight.getAirportOfDestination().getName());
        arrivalAirport.setMaxWidth(140);
        arrivalAirport.setLayoutX(336.0);
        arrivalAirport.setLayoutY(68.0);
        arrivalAirport.setTextFill(Color.valueOf("838383"));
        arrivalAirport.setFont(Font.font("Helvetica", 13.0));
        newPane.getChildren().add(arrivalAirport);
        // horizontal separator between information
        Separator horizontal = new Separator();
        horizontal.setLayoutX(0.0);
        horizontal.setLayoutY(100);
        horizontal.setPrefHeight(1.0);
        horizontal.setPrefWidth(480);
        newPane.getChildren().add(horizontal);
        // passengers label
        Label passengers = new Label("PASSENGERS");
        passengers.setLayoutX(9.0);
        passengers.setLayoutY(114.0);
        passengers.setTextFill(Color.valueOf("838383"));
        passengers.setFont(Font.font("Helvetica", 9.0));
        newPane.getChildren().add(passengers);
        // passengers number
        Label passengersNumber = new Label(String.valueOf(flight.getPassengers()));
        passengersNumber.setLayoutX(93.0);
        passengersNumber.setLayoutY(109.0);
        passengersNumber.setTextFill(Color.valueOf("4185d1"));
        passengersNumber.setFont(Font.font("Helvetica", FontWeight.BOLD, 16.0));
        newPane.getChildren().add(passengersNumber);
        // vertical separator 1
        Separator vertical1 = new Separator();
        vertical1.setOrientation(VERTICAL);
        vertical1.setLayoutX(145.0);
        vertical1.setLayoutY(100.0);
        vertical1.setPrefHeight(36.0);
        vertical1.setPrefWidth(1.0);
        newPane.getChildren().add(vertical1);
        // distance label
        Label distance = new Label("DISTANCE (km)");
        distance.setLayoutX(156.0);
        distance.setLayoutY(114.0);
        distance.setTextFill(Color.valueOf("838383"));
        distance.setFont(Font.font("Helvetica", 9.0));
        newPane.getChildren().add(distance);
        // distance number
        Label distanceNumber = new Label(String.valueOf(formatter.format(flight.getDistance())));
        distanceNumber.setLayoutX(237.0);
        distanceNumber.setLayoutY(109.0);
        distanceNumber.setTextFill(Color.valueOf("4185d1"));
        distanceNumber.setFont(Font.font("Helvetica", FontWeight.BOLD, 16.0));
        newPane.getChildren().add(distanceNumber);
        // cost label
        Label cost = new Label("€");
        cost.setPadding(new Insets(0,0,0,10));
        cost.setLayoutX(311.0);
        cost.setLayoutY(101.0);
        cost.setPrefHeight(36.0);
        cost.setPrefWidth(169.0);
        cost.setStyle("-fx-background-color: #4185d1;");
        cost.setTextFill(Color.WHITE);
        cost.setFont(Font.font("Helvetica Light", 15.0));
        newPane.getChildren().add(cost);
        // cost number
        Label costNumber = new Label(String.valueOf(formatter.format(flight.getCosts())));
        costNumber.setLayoutX(375.0);
        costNumber.setLayoutY(109.0);
        costNumber.setTextFill(Color.WHITE);
        costNumber.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 16.0));
        newPane.getChildren().add(costNumber);
        // horizontal separator between different flights
        Separator horizontalF = new Separator();
        horizontalF.setLayoutX(0.0);
        horizontalF.setLayoutY(137);
        horizontalF.setPrefHeight(1.0);
        horizontalF.setPrefWidth(480);
        newPane.getChildren().add(horizontalF);
        listFlightsContainer.getChildren().add(newPane);
    }
}
