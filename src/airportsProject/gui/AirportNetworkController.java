package airportsProject.gui;

import airportsProject.Airport;
import airportsProject.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.ArrayList;
import java.util.List;
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
    @FXML
    private Label warning;

    private static boolean updated = false; // will tell the update method if a user added/removed an airport or not, to know if it is needed to update the list
    private Group zoomGroup;
    private Utils utils = Utils.getInstance();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private String origin = "", destination = "";

    public void initialize(){
        warning.setStyle("-fx-opacity: 0");
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

        for (int i = 0; i < utils.getSymbolGraph().digraph().V(); i++) {
            Airport airport = airports.get(utils.getSymbolGraph().nameOf(i));
            newAirportItem(airport);
            if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                setPinLocation(airport);
            }else{
                Thread t1 = new Thread(new Runnable() {
                    public void run() {
                        airport.setCoordinates();
                    }
                });
                t1.start();
                try{
                    t1.join(); // waits for the function to end
                    // saves the new coordinates on the file
                    Utils.getInstance().createCoordinatesFile();
                    if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                        setPinLocation(airport);
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

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
                pin.setStyle("-fx-background-color: white");
                warning.setStyle("-fx-opacity: 0");
                if (origin.isEmpty()) {
                    if(!airport.getAirplanes().isEmpty()){ // if the chosen origin has airplanes parked to perform the flight
                        origin = airport.getCode();
                    }else{
                        pin.setStyle("-fx-background-color: red");
                        warning.setStyle("-fx-opacity: 1");
                    }
                } else if(airport.getCode().compareTo(origin) != 0){ // can't choose the same airport as origin and destination
                    destination = airport.getCode();
                    setNewFlight(origin, destination);
                    origin = "";
                    destination = "";
                }
            }
        });
        pin.setLayoutX(airport.getLongitude() - (24 / 2)); // 24 is the pin width, divided by 2 to set the pin bottom to the coordinate (middle of the pin)
        pin.setLayoutY(airport.getLatitude() - 33); // 33 is the pin height
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
        dialog.showAndWait();
    }

    private void updateList(){
        if(updated){
            airports = utils.getAirports();
            containAirports.getChildren().clear(); // removes the previous list
            mapPane.getChildren().remove(1, mapPane.getChildren().size()); // resets pins locations to update
            for (int i = 0; i < utils.getSymbolGraph().digraph().V(); i++) {
                Airport airport = airports.get(utils.getSymbolGraph().nameOf(i));
                newAirportItem(airport);
                if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                    setPinLocation(airport);
                }
            }
        }
        searchAirport.setText("");
        updated = false;
    }

    private void updateList(SeparateChainingHashST<String, Airport> results){
        containAirports.getChildren().clear(); // removes the previous list
        mapPane.getChildren().remove(1, mapPane.getChildren().size()); // resets pins locations to update
        if(!results.isEmpty()){
            for(String code : results.keys()){ // lists all the existent airports
                Airport airport = results.get(code);
                newAirportItem(airport);
                if(airport.getLatitude() != -1 && airport.getLongitude() != -1) {
                    setPinLocation(airport);
                }
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
            noResult.setLayoutY(65);
            noResult.setPrefWidth(213);
            noResult.setFont(Font.font("Helvetica", 20));
            noResult.setTextFill(Color.valueOf("4185d1"));
            noResult.setWrapText(true);
            newPane.getChildren().add(noResult);
            VBox.setMargin(newPane, new Insets(10,0,0,0));
            containAirports.getChildren().add(newPane);
        }
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
    void openGraph(){
        utils.checkGraphIsConnected(utils.getSymbolGraph().digraph()); // check if the graph is connected
        Utils.showGraphs();
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

    /**
     * parses the inputted search, checks for any boolean operators and searches for the results having all the variables in consideration
     * @param search -> search wanted
     */
    private void getResults(String search){
        search = search.toUpperCase();
        SeparateChainingHashST<String, Airport> results = new SeparateChainingHashST<>();
        String[] multipleAND = search.split("&&"); // searches for the boolean operator AND
        List<String> gatherResults = new ArrayList<>();
        List<String> auxList = new ArrayList<>(), toRemove = new ArrayList<>();
        if(multipleAND.length > 1){ // multiple searches with the AND operator
            for (int i = 0; i < multipleAND.length; i++) { // the results must meet ALL the requirements
                multipleAND[i] = multipleAND[i].trim();
                String[] multipleOR = multipleAND[i].split("\\|\\|"); // searches for the boolean operator OR inside each statement separated by an AND operator
                if(multipleOR.length > 1){
                    for (int j = 0; j < multipleOR.length; j++) {
                        multipleOR[j] = multipleOR[j].trim();
                        auxList.addAll(searchIt(multipleOR[j]));
                        // checks for any AND statement that, together with this OR statements, must get a result that is common to both
                        if(gatherResults.size() > 0){
                            for(String code : auxList){
                                if(!gatherResults.contains(code)){ // a result is not present in both statements, it is not a result wanted
                                    toRemove.add(code);
                                }
                            }
                            if(toRemove.size() > 0){ // stores a list of airports that don't meet all the requirements to remove now
                                auxList.removeAll(toRemove);
                                toRemove.clear();
                            }
                        }
                    }
                    if(auxList.size() == 0){ // at the end of the OR operators if no airport met the requirements, it means there's no results wanted even if there was on an AND statement
                        gatherResults.clear();
                    }
                }else{ // no operators within an AND statement, proceeds with the search
                    if(gatherResults.size() > 0 ){ // one of the two AND statement has already been done
                        auxList.addAll(searchIt(multipleAND[i]));
                        for (String code : auxList){
                            if(!gatherResults.contains(code)){
                                toRemove.add(code);
                            }
                        }
                        if(toRemove.size() > 0){ // stores a list of airports that don't meet all the requirements to remove now
                            auxList.removeAll(toRemove);
                            toRemove.clear();
                        }
                        if(auxList.size() == 0){ // at the end of the OR operators if no airport met the requirements, it means there's no results wanted even if there was on an AND statement
                            gatherResults.clear();
                        }
                    }else {
                        gatherResults.addAll(searchIt(multipleAND[i]));
                    }
                }
            }
            for (String code : gatherResults){
                results.put(code, airports.get(code));
            }
            updateList(results);
        }else{
            String[] multipleOR = search.split("\\|\\|"); // searches for the boolean operator OR
            if(multipleOR.length > 1){
                for (int i = 0; i < multipleOR.length; i++) { // results must only meet one requirement AT LEAST, they are not dependent on each other, the results will be added up for each requirement met
                    multipleOR[i] = multipleOR[i].trim();
                    // there can't be any AND operators inside because the first split already checks for their existence
                    gatherResults.addAll(searchIt(multipleOR[i]));
                }
            }else{ // no boolean operators found, proceeds with the search
                gatherResults.addAll(searchIt(search));
            }
            for (String code : gatherResults){
                results.put(code, airports.get(code));
            }
            updateList(results);
        }
    }

    private List<String> searchIt(String search){
        List<String> result = new ArrayList<>();
        if(search.contains("CONNECTIONS:")){
            String[] cons = search.split("CONNECTIONS:");
            cons[1] = cons[1].trim();
            if(Utils.isNumeric(cons[1])){
                for(String code : utils.airportWithConnections(Integer.valueOf(cons[1])).keys()){
                    result.add(code);
                }
            }
        }else{
            for (int i = 0; i < utils.getSymbolGraph().digraph().V(); i++) {
                Airport airport = airports.get(utils.getSymbolGraph().nameOf(i));
                // searches the keyword occurrence on the airport
                if (search.compareTo(airport.getCode()) == 0 ||
                        search.compareTo(airport.getName().toUpperCase()) == 0 ||
                        Utils.isNumeric(search) && Float.valueOf(search).compareTo(airport.getRating()) == 0 ||
                        search.compareTo(airport.getCity().toUpperCase()) == 0 ||
                        search.compareTo(airport.getCountry().toUpperCase()) == 0 ||
                        search.compareTo(airport.getContinent().toUpperCase()) == 0) {
                    result.add(airport.getCode());
                }
            }
        }
        return result;
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
