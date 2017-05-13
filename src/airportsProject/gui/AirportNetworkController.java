package airportsProject.gui;

import airportsProject.Airport;
import airportsProject.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import libs.SeparateChainingHashST;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

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

    private static boolean updated = false; // will tell the update method if a user added/removed an airport or not, to know if it is needed to update the list
    private Group zoomGroup;
    private Utils utils = Utils.getInstance();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private String origin = "", destination = "";

    public void initialize(){
        searchAirport.getParent().requestFocus();
        searchAirport.setFocusTraversable(false);

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

        for(String code : airports.keys()){
            Airport airport = airports.get(code);
            newAirportItem(airport);
            if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                setPinLocation(airport);
            }
        }

        // checks for the existence of the coordinates file, and creates it if it doesn't exist
        // it is done here because if it doesn't exist, then each airport created had to get their coordinates and that takes time
        // this way it is less probable that there will be wrong coordinates saved on the file
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(".//data//coordinates.bin"))) {
            oos.close();
        } catch (Exception ex) {
            // no file still exists, creates it and stores all the airports loaded
            Utils.getInstance().createCoordinatesFile();
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
                if (origin.isEmpty()) {
                    origin = airport.getCode();
                } else if(airport.getCode().compareTo(origin) != 0){ // can't choose the same airport as origin and destination
                    destination = airport.getCode();
                    setNewFlight(origin, destination);
                    origin = "";
                    destination = "";
                }
            }
        });
        pin.setLayoutX(airport.getLongitude() - (34 / 2)); // 34 is the pin width, divided by 2 to set the pin bottom to the coordinate (middle of the pin)
        pin.setLayoutY(airport.getLatitude() - (45)); // 45 is the pin height
        pin.setVisible(true);
        mapPane.getChildren().add(pin);
    }

    private void setNewFlight(String o, String d){
        // pops up the new flight dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        dialog.initOwner(containAirports.getScene().getWindow());
        dialog.setTitle("New Flight");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newFlightDialog.fxml"));
        fxmlLoader.setControllerFactory((Class<?> controllerType) -> {
            if (controllerType == NewFlightDialogController.class) { // send the code of the airport to show its details
                NewFlightDialogController controller = new NewFlightDialogController();
                controller.setPoints(o, d);
                return controller;
            } else {
                try {
                    return controllerType.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // reset strings to accept another flight creation
        origin = "";
        destination = "";
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
        dialog.getDialogPane().getStyleClass().add("customDialog");
        dialog.setContentText(null);
        Optional<ButtonType> result = dialog.showAndWait();
        // Closes the dialog
        if (!result.isPresent()) {
            System.out.println("Closed new flight");
        }
    }

    private void updateList(){
        if(updated){
            containAirports.getChildren().clear(); // removes the previous list
            mapPane.getChildren().remove(1, mapPane.getChildren().size()); // resets pins locations to update
            for(String name : airports.keys()){ // lists all the existent airports
                Airport airport = airports.get(name);
                newAirportItem(airport);
                if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                    setPinLocation(airport);
                }
            }
        }
        searchAirport.setText("");
        updated = false;
    }

    public static void setUpdate(){
        updated = true;
    }

    @FXML
    void clearSearch(){
        updated = true;
        updateList();
    }

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void newAirport(MouseEvent event){
        updated = false;
        Dialog<ButtonType> dialog = new Dialog<>();
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        dialog.initOwner(containAirports.getScene().getWindow());
        dialog.setTitle("New Airport");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newAirportDialog.fxml"));
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
        // if the user closes the dialog, the list of airports will update
        if(!result.isPresent()){
            updateList();
        }
    }

    private void getResults(String search){
        search = search.toUpperCase();
        SeparateChainingHashST<String, Airport> resultAirports = new SeparateChainingHashST<>();
        for(String code : airports.keys()){
            // searches the keyword occurrence on the airports
            if(search.compareTo(code) == 0 ||
            search.compareTo(airports.get(code).getName().toUpperCase()) == 0 ||
            Utils.isNumeric(search) && Float.valueOf(search).compareTo(airports.get(code).getRating()) == 0 ||
            search.compareTo(airports.get(code).getCity().toUpperCase()) == 0 ||
            search.compareTo(airports.get(code).getCountry().toUpperCase()) == 0 ||
            search.compareTo(airports.get(code).getContinent().toUpperCase()) == 0){
                resultAirports.put(code, airports.get(code));
            }
        }
        containAirports.getChildren().clear(); // removes the previous list
        mapPane.getChildren().remove(1, mapPane.getChildren().size()); // resets pins locations to update
        if(!resultAirports.isEmpty()){
            for(String name : resultAirports.keys()){ // lists all the existent airports
                Airport airport = resultAirports.get(name);
                newAirportItem(airport);
                if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                    setPinLocation(airport);
                }
            }
        }
    }

    @FXML
    void getInput(ActionEvent actionEvent){
        if(searchAirport.getText().trim().length() != 0){
            getResults(searchAirport.getText());
            searchAirport.setText("");
        }
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
                    // alert to check if the user really wants to delete the airline
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
                    // style the alert
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete \"" + airport.getName() + "\" airport ?");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Utils.removeAirport(airport);
                        updated = true;
                        updateList();
                    }
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
