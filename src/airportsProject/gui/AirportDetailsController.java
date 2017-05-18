package airportsProject.gui;

import airportsProject.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;
import libs.SeparateChainingHashST;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class AirportDetailsController {
    @FXML
    private HBox containAirportDetails;
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
    private VBox containConnections;
    @FXML
    private ScrollPane map;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Label mapPin;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    Group zoomGroup;
    private String code = "";

    Utils utils = Utils.getInstance();
    SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private Airport airport;
    private Date from = null, to = null;

    public void initialize(){
        startDate.getParent().requestFocus();
        startDate.setFocusTraversable(false);
        endDate.setFocusTraversable(false);
        mapPin.getStyleClass().add("pin");
        airport = airports.get(code);
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
        if(airport.getAirplanes().size() == 0){
            Label node = new Label("No airplanes parked here");
            node.getStyleClass().add("infoLabel");
            containAirplanes.getChildren().add(node);
        }else {
            for (int id : airport.getAirplanes().keys()) {
                Airplane airplane = airport.getAirplanes().get(id);
                newAirplaneItem(airplane);
            }
        }

        // set airport flight history
        setFlights();

        // connections to and from this airport
        ArrayList<String> connections = utils.airportConnections(airport);
        if(connections.size() == 0){
            Label node = new Label("No connections");
            node.getStyleClass().add("infoLabel");
            containConnections.getChildren().add(node);
        }else{
            for(String code : connections){
                Airport airport = airports.get(code);
                containConnections.getChildren().add(newAirportItem(airport));
            }
        }

        // setting date pickers to search flights from this airport between a time
        String pattern = "dd/MM/yyyy";
        startDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        endDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        startDate.setOnAction(event -> {
            LocalDate date = startDate.getValue();
            if(date != null){
                from = new Date(date.getDayOfMonth(), date.getMonthValue(), date.getYear(), 23,59,59);
            }else{
                from = null;
                setFlights();
            }
            if(to != null){ // ready to search
                setFlights(from, to);
            }
        });
        endDate.setOnAction(event -> {
            LocalDate date = endDate.getValue();
            if(date != null){
                to = new Date(date.getDayOfMonth(), date.getMonthValue(), date.getYear(), 23,59,59);
            }else{
                to = null;
                setFlights();
            }
            if(from != null){ // ready to search
                setFlights(from, to);
            }
        });
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
        mapPin.setLayoutX(airport.getLongitude() - (24 / 2)); // 24 is the pin width, divided by 2 to set the pin bottom to the coordinate (middle of the pin)
        mapPin.setLayoutY(airport.getLatitude() - 33); // 33 is the pin height
        mapPin.setVisible(true);
    }

    private void setFlights(){
        containArrivals.getChildren().clear();
        containDepartures.getChildren().clear();
        if(airport.getFlights().size() == 0){
            Label node = new Label("No flights arriving here");
            node.getStyleClass().add("infoLabel");
            containArrivals.getChildren().add(node);
            Label node2 = new Label("No flights departing from here");
            node2.getStyleClass().add("infoLabel");
            containDepartures.getChildren().add(node2);
        }else {
            for (Date date : airport.getFlights().keys()) {
                Flight flight = airport.getFlights().get(date);
                if (flight.getAirportOfDestination().equals(airport)) { // arrivals
                    containArrivals.getChildren().add(newFlightItem(flight));
                } else { // departures
                    containDepartures.getChildren().add(newFlightItem(flight));
                }
            }
        }
    }

    private void setFlights(Date start, Date end){
        containArrivals.getChildren().clear();
        containDepartures.getChildren().clear();
        if(airport.getFlights().size() == 0){
            Label node = new Label("No flights arriving here");
            node.getStyleClass().add("infoLabel");
            containArrivals.getChildren().add(node);
            Label node2 = new Label("No flights departing from here");
            node2.getStyleClass().add("infoLabel");
            containDepartures.getChildren().add(node2);
        }else {
            for (Date f : airport.getFlights().keys()) {
                Flight flight = airport.getFlights().get(f);
                if ((flight.getDate().compareTo(start) == 1 || flight.getDate().compareDate(start) == 0) &&
                        (flight.getDate().compareTo(end) == -1 || flight.getDate().compareDate(end) == 0)) {
                    if (flight.getAirportOfDestination().equals(airport)) { // arrivals
                        containArrivals.getChildren().add(newFlightItem(flight));
                    } else { // departures
                        containDepartures.getChildren().add(newFlightItem(flight));
                    }
                }
            }
        }
    }

    @FXML
    void newConnection(ActionEvent event) {
        // para guardar edge
//        utils.getSymbolGraph().G().addEdge();
        // verificar se ja nao existe a conexao
        // fazer log depois
        Dialog<ButtonType> dialog = new Dialog<>();
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        dialog.initOwner(containAirportDetails.getScene().getWindow());
        dialog.setTitle("New Connection");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newConnectionDialog.fxml"));
        fxmlLoader.setControllerFactory((Class<?> controllerType) -> {
            if (controllerType == NewConnectionDialogController.class) { // send the code of the airport to show its details
                NewConnectionDialogController controller = new NewConnectionDialogController();
                controller.setOrigin(airport);
                return controller;
            }else{
                try {
                    return controllerType.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e) {
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
        dialog.getDialogPane().getStyleClass().add("customDialog");
        dialog.setContentText(null);
        Optional<ButtonType> result = dialog.showAndWait();
        // if the user closes the dialog, the information edited will update
        if(!result.isPresent()){
            updateConnections();
        }
    }

    private void updateConnections(){
        containConnections.getChildren().clear();
        ArrayList<String> connections = utils.airportConnections(airport);
        if(connections.size() == 0){
            Label node = new Label("No connections");
            node.getStyleClass().add("infoLabel");
            containConnections.getChildren().add(node);
        }else{
            for(String code : connections){
                Airport airport = airports.get(code);
                containConnections.getChildren().add(newAirportItem(airport));
            }
        }
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
            Dialog<ButtonType> dialog = new Dialog<>();
            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide());
            dialog.initOwner(containAirportDetails.getScene().getWindow());
            dialog.setTitle("Edit Airport");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("newAirportDialog.fxml"));
            fxmlLoader.setControllerFactory((Class<?> controllerType) -> {
                if (controllerType == NewAirportDialogController.class) { // send the code of the airport to show its details
                    NewAirportDialogController controller = new NewAirportDialogController();
                    controller.setEdit(airport);
                    return controller;
                }else{
                    try {
                        return controllerType.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            try{
                dialog.getDialogPane().setContent(fxmlLoader.load());
            }catch(IOException e) {
                e.printStackTrace();
                return;
            }
            dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
            dialog.getDialogPane().getStyleClass().add("customDialog");
            dialog.setContentText(null);
            Optional<ButtonType> result = dialog.showAndWait();
            // if the user closes the dialog, the information edited will update
            if(!result.isPresent()){
                airportName.setText(airports.get(code).getName());
                airportRating.setText(String.valueOf(airports.get(code).getRating()));
            }
        }else{
            // alert to check if the user really wants to delete the airline
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
            // style the alert
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete \"" + airport.getName() + "\" airport ?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Utils.removeAirport(airport);
                VistaNavigator.loadVista(VistaNavigator.AIRPORTNETW);
            }
        }
    }

    private boolean removeThis = false;
    private Pane newAirportItem(Airport airport2){
        // remove airport
        Label removeAirport = new Label("remove");
        removeAirport.setLayoutX(420);
        removeAirport.setLayoutY(15);
        removeAirport.setCursor(Cursor.HAND);
        removeAirport.setTextFill(Color.valueOf("808080"));
        removeAirport.setFont(Font.font("Helvetica", FontWeight.EXTRA_LIGHT, 12));
        removeAirport.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // tells the event that handles the pane clicking that it was the element "remove" that was clicked inside the pane
                // otherwise the pane would only know it was a click on the pane and would go to the airplane details
                removeThis = true;
            }
        });
        // container pane of a single flight
        Pane newPane = new Pane();
        newPane.setPrefWidth(480.0);
        newPane.setPrefHeight(40.0);
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
                    // alert to check if the user really wants to delete the airline
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
                    // style the alert
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete the connection from \"" + airport.getName() + "\" to \"" + airport2.getName() + "\" ?");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Utils.removeConnectionOfAirport(airport.getCode(), airport2.getCode());
                        updateConnections();

                    }
                    removeThis = false; // reset variable
                }else{
                    VistaNavigator.loadVista(VistaNavigator.AIRPORTDETAILS, airport2.getCode());
                }
            }
        });
        newPane.getChildren().add(removeAirport);
        Label airportCode = new Label(airport2.getCode());
        airportCode.setLayoutX(134);
        airportCode.setLayoutY(3);
        airportCode.setTextFill(Color.valueOf("4185d1"));
        airportCode.setFont(Font.font("Helvetica", 28));
        newPane.getChildren().add(airportCode);
        Label airportName = new Label(airport2.getName());
        airportName.setLayoutX(207);
        airportName.setLayoutY(4);
        airportName.setMaxWidth(200);
        airportName.setTextFill(Color.valueOf("757575"));
        airportName.setFont(Font.font("Helvetica", 14));
        newPane.getChildren().add(airportName);
        Label airportPlace = new Label(airport2.getCity() + ", " + airport2.getCountry());
        airportPlace.setLayoutX(207);
        airportPlace.setLayoutY(21);
        airportPlace.setTextFill(Color.valueOf("bcbcbc"));
        airportPlace.setFont(Font.font("Helvetica", FontWeight.LIGHT, 12));
        newPane.getChildren().add(airportPlace);
        return newPane;
    }

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
                VistaNavigator.loadVista(VistaNavigator.FLIGHTDETAILS, flight.getDate());
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
        if(flight.getAirportOfDestination().equals(airport)){
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
        if(flight.getAirportOfDestination().equals(airport)){
            airportName = new Label(flight.getAirportOfOrigin().getName());
        }else{
            airportName = new Label(flight.getAirportOfDestination().getName());
        }
        airportName.setMaxWidth(150);
        airportName.setTextFill(Color.valueOf("5e5d5d"));
        airportName.setFont(Font.font("Helvetica", 15));
        newHBox.getChildren().add(airportName);
        newPane.getChildren().add(newHBox);
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
        Label airline = new Label(airplane.getAirline().getName());
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
