package airportsProject.gui;

import airportsProject.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import libs.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Dialog window to insert a new flight
 */
public class NewFlightDialogController {
    @FXML
    private Label originCode;
    @FXML
    private Label originPlace;
    @FXML
    private Label destinCode;
    @FXML
    private Label destinPlace;
    @FXML
    private Label warning;
    @FXML
    private ToggleGroup flightType;
    @FXML
    private TextField flightPassengers;
    @FXML
    private Label totalLabel;
    @FXML
    private Label totalValue;
    @FXML
    private VBox routeContainer;

    // format float numbers
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    private String origin = "", destination = "";
    private String typeOfFlight = ""; // to store the type of flight chosen by the user
    private int originPos = -1, destinPos = -1; // position on the symbol table of the origin and destination airports
    private Utils utils = Utils.getInstance();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private SymbolEdgeWeightedDigraph symbolGraph = utils.getSymbolGraph();
    private boolean show = true;
    private DijkstraSP dijkstraSP = null;
    private BreadthFirstPaths bfs = null;
    private Airplane airplaneUsed = null;

    public void initialize(){
        // set separator symbol for large numbers: 1000 -> 1 000
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        warning.setStyle("-fx-opacity: 0");
        if(!origin.isEmpty() && !destination.isEmpty()){
            originCode.setText(origin);
            originPlace.setText(airports.get(origin).getCity() + ", " + airports.get(origin).getCountry());
            destinCode.setText(destination);
            destinPlace.setText(airports.get(destination).getCity() + ", " + airports.get(destination).getCountry());
        }
        // flight origin & destination found, we need to find their index on the ST
        originPos = symbolGraph.indexOf(origin);
        destinPos = symbolGraph.indexOf(destination);
        flightType.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (flightType.getSelectedToggle() != null) {
                    typeOfFlight = ((RadioButton)new_toggle).getText();
                }
            }
        });
    }

    @FXML
    public void submitFlight() {
        // calculate final route chosen
        show = false;
        if(!calculateRoute()){
            warning.setText(origin + " doesn't have a connection to the destination chosen or no airplane can do that flight.");
            warning.setStyle("-fx-opacity: 1");
        }else if (typeOfFlight.isEmpty()) { // check if the user chose a type
            warning.setText("Please choose a type of flight.");
            warning.setStyle("-fx-opacity: 1");
        } else if (!Utils.isNumeric(flightPassengers.getText())) { // check if it is a number
            warning.setText("Please enter a valid number of passengers. (Integer value)");
            warning.setStyle("-fx-opacity: 1");
        } else if (airplaneUsed != null && Integer.valueOf(flightPassengers.getText()) > airplaneUsed.getPassengersCapacity()) { // check if the passengers for this flight fit on the airplane chosen
            warning.setText("Too many passengers! (Maximum: " + airplaneUsed.getPassengersCapacity() + ")");
            warning.setStyle("-fx-opacity: 1");
        }else{
            warning.setStyle("-fx-opacity: 0");
            if(typeOfFlight.compareTo("Less Stops") == 0){ // using BFS
                utils.newFlight(bfs, null, new Date(), Integer.valueOf(flightPassengers.getText()), airplaneUsed,
                        airports.get(airplaneUsed.getAirportCode()), airports.get(destination), destinPos, null);
            }else{ // using Dijkstra
                utils.newFlight(null, dijkstraSP, new Date(), Integer.valueOf(flightPassengers.getText()), airplaneUsed,
                        airports.get(airplaneUsed.getAirportCode()), airports.get(destination), destinPos, null);
            }
            // close the dialog window
            flightPassengers.getParent().getParent().getScene().getWindow().hide();
        }
    }

    @FXML
    public void route(){
        if(!calculateRoute()){
            warning.setText(origin + " doesn't have a connection to the destination chosen or no airplane can do that flight.");
            warning.setStyle("-fx-opacity: 1");
        }
    }

    @FXML
    public boolean calculateRoute(){
        routeContainer.getChildren().clear();
        dijkstraSP = null;
        RedBlackBST<Integer, Airplane> airplanes = airports.get(origin).getAirplanes();
        switch (typeOfFlight){
           case "Shortest Distance":
               for(int id : airplanes.keys()) {
                   dijkstraSP = new DijkstraSP(symbolGraph.digraph(), originPos, airplanes.get(id), "distance");
                   if (dijkstraSP.hasPathTo(destinPos)) {
                       airplaneUsed = airplanes.get(id);
                       if (show) {
                           showRoute(dijkstraSP, destinPos);
                           totalLabel.setText("Total Distance: ");
                           totalValue.setText(dijkstraSP.distTo(destinPos) + " km");
                       }
                       return true;
                   }
               }
               return false;
           case "Cheapest Flight":
               for(int id : airplanes.keys()){
                   DijkstraSP dijkstra = new DijkstraSP(symbolGraph.digraph(),originPos, airplanes.get(id), "monetary");
                   if (dijkstraSP == null || dijkstra.distTo(destinPos) < dijkstraSP.distTo(destinPos)) {
                       dijkstraSP = dijkstra;
                       airplaneUsed = airplanes.get(id);
                   }
               }
               if(dijkstraSP.hasPathTo(destinPos)){
                   if(show){
                       showRoute(dijkstraSP, destinPos);
                       totalLabel.setText("Total Cost: ");
                       totalValue.setText(dijkstraSP.distTo(destinPos) + " €");
                   }
                    return true;
               }
               return false;
           case "Fastest Flight":
               for(int id : airplanes.keys()){
                   DijkstraSP dijkstra = new DijkstraSP(symbolGraph.digraph(),originPos, airplanes.get(id), "time");
                   if(dijkstraSP == null || dijkstra.distTo(destinPos) < dijkstraSP.distTo(destinPos)){
                       dijkstraSP = dijkstra;
                       airplaneUsed = airplanes.get(id);
                   }
               }
               if(dijkstraSP.hasPathTo(destinPos)){
                   if(show){
                       showRoute(dijkstraSP, destinPos);
                       totalLabel.setText("Total Time: ");
                       totalValue.setText(Date.convertTime(dijkstraSP.distTo(destinPos)));
                   }
                   return true;
               }
               return false;
           case "Less Stops":
               for(int id : airplanes.keys()) {
                   bfs = new BreadthFirstPaths(symbolGraph.digraph(), originPos, airplanes.get(id));
                   if (bfs.hasPathTo(destinPos)) {
                       if (show) showBFSRoute(bfs, destinPos);
                       airplaneUsed = airplanes.get(id);
                       return true;
                   }
               }
               return false;
           default: return false;
        }
    }

    private void showBFSRoute(BreadthFirstPaths bfs, int destination){
        int count = 1, lastAirport = -1, stops = 0;
        if (bfs.hasPathTo(destination)) {
            for (int x : bfs.pathTo(destination)) {
                if(count%2 == 0){ // it prints a connection (two per time airports)
                    stops += 1;
                    newRouteItem(new Connection(lastAirport, x, -1, -1, -1));
                }else{
                    lastAirport = x;
                }
                count++;
            }
            totalLabel.setText("Total Stops: ");
            totalValue.setText("" + stops);
        }else{
            Pane newPane = new Pane();
            newPane.setPrefWidth(632.0);
            newPane.setPrefHeight(60.0);
            Label noRoute = new Label("No route found for this flight.");
            noRoute.setFont(Font.font("Helvetica", 25));
            noRoute.setTextFill(Color.RED);
            noRoute.setAlignment(Pos.CENTER);
            noRoute.setPrefWidth(632);
            newPane.getChildren().add(noRoute);
            routeContainer.getChildren().add(newPane);
        }
    }

    private void showRoute(DijkstraSP dijkstraSP, int destination){
        for (Connection e : dijkstraSP.pathTo(destination)) {
            newRouteItem(e);
        }
    }

    private void newRouteItem(Connection con){
        Airport fromAirport = airports.get(symbolGraph.nameOf(con.from()));
        Airport toAirport = airports.get(symbolGraph.nameOf(con.to()));
        Pane newPane = new Pane();
        newPane.setPrefWidth(632.0);
        newPane.setPrefHeight(60.0);
        newPane.getStyleClass().add("routeItem");
        Label originCode = new Label(fromAirport.getCode());
        originCode.setTextFill(Color.WHITE);
        originCode.setPrefWidth(100);
        originCode.setMaxWidth(100);
        originCode.setLayoutX(143);
        originCode.setLayoutY(6);
        originCode.setFont(Font.font("Helvetica", 30));
        newPane.getChildren().add(originCode);
        Label originPlace = new Label(fromAirport.getCity() + ", " + fromAirport.getCountry());
        originPlace.setTextFill(Color.WHITE);
        originPlace.setPrefWidth(100);
        originPlace.setMaxWidth(100);
        originPlace.setLayoutX(143);
        originPlace.setLayoutY(39);
        originPlace.setFont(Font.font("Helvetica", FontWeight.LIGHT, 13));
        newPane.getChildren().add(originPlace);
        ImageView img1 = new ImageView("airportsProject/gui/images/flightSep.png");
        img1.setFitHeight(14);
        img1.setFitWidth(64);
        img1.setLayoutX(244);
        img1.setLayoutY(26);
        newPane.getChildren().add(img1);
        Label weightNumber = new Label("");
        switch (typeOfFlight){
            case "Shortest Distance":
                weightNumber = new Label(formatter.format(con.weight()));
                break;
            case "Cheapest Flight":
                weightNumber = new Label("" + airplaneUsed.getAirplaneCost(con));
                break;
            case "Fastest Flight":
                weightNumber = new Label(Date.convertTime(airplaneUsed.getFlightDuration(con)));
                break;
            default: break;
        }
        if(typeOfFlight.compareTo("Less Stops") == 0){
            ImageView plane = new ImageView("airportsProject/gui/images/whiteAirplane.png");
            plane.setFitHeight(18);
            plane.setFitWidth(21);
            plane.setLayoutX(322);
            plane.setLayoutY(23);
            newPane.getChildren().add(plane);
        }else{
            weightNumber.setTextFill(Color.WHITE);
            weightNumber.setAlignment(Pos.CENTER);
            weightNumber.setLayoutX(288);
            weightNumber.setLayoutY(14);
            weightNumber.setPrefWidth(90);
            weightNumber.setFont(Font.font("Helvetica", FontWeight.LIGHT, 13));
            newPane.getChildren().add(weightNumber);
            Label weightUnit = new Label("");
            switch (typeOfFlight){
                case "Shortest Distance":
                    weightUnit = new Label("km");
                    break;
                case "Cheapest Flight":
                    weightUnit = new Label("€");
                    break;
                default:break;
            }
            weightUnit.setTextFill(Color.WHITE);
            weightUnit.setAlignment(Pos.CENTER);
            weightUnit.setLayoutX(307);
            weightUnit.setLayoutY(31);
            weightUnit.setPrefWidth(50);
            weightUnit.setFont(Font.font("Helvetica", FontWeight.LIGHT, 13));
            newPane.getChildren().add(weightUnit);
        }
        ImageView img2 = new ImageView("airportsProject/gui/images/flightSep.png");
        img2.setFitHeight(14);
        img2.setFitWidth(64);
        img2.setLayoutX(358);
        img2.setLayoutY(26);
        newPane.getChildren().add(img2);
        Label destinCode = new Label(toAirport.getCode());
        destinCode.setTextFill(Color.WHITE);
        destinCode.setPrefWidth(100);
        destinCode.setMaxWidth(100);
        destinCode.setLayoutX(433);
        destinCode.setLayoutY(6);
        destinCode.setFont(Font.font("Helvetica", 30));
        newPane.getChildren().add(destinCode);
        Label destinPlace = new Label(toAirport.getCity() + ", " + toAirport.getCountry());
        destinPlace.setTextFill(Color.WHITE);
        destinPlace.setPrefWidth(170);
        destinPlace.setMaxWidth(170);
        destinPlace.setLayoutX(433);
        destinPlace.setLayoutY(39);
        destinPlace.setFont(Font.font("Helvetica", FontWeight.LIGHT, 13));
        newPane.getChildren().add(destinPlace);
        VBox.setMargin(newPane, new Insets(0, 0, 5, 0));
        routeContainer.getChildren().add(newPane);
    }

    public void setPoints(String origin, String destination){
       this.origin = origin;
       this.destination = destination;
    }
}
