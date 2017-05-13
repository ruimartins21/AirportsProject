package airportsProject.gui;

import airportsProject.Airplane;
import airportsProject.Airport;
import airportsProject.Date;
import airportsProject.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import libs.*;

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
    private Pane routeContainer;

    private String origin = "", destination = "";
    private String typeOfFlight = ""; // to store the type of flight chosen by the user
    private int originPos = -1, destinPos = -1; // position on the symbol table of the origin and destination airports
    private Utils utils = Utils.getInstance();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private boolean typeSelected = false;
    private DijkstraSP dijkstraSP = null;
    private BreadthFirstPaths bfs = null;
    private Airplane airplaneUsed = null;
    private SymbolEdgeWeightedDigraph symbolGraph = utils.getSymbolGraph();
    private double minWeight = 0;

    public void initialize(){
        warning.setStyle("-fx-opacity: 0");
        if(!origin.isEmpty() && !destination.isEmpty()){
            originCode.setText(origin);
            originPlace.setText(airports.get(origin).getCity() + ", " + airports.get(origin).getCountry());
            destinCode.setText(destination);
            destinPlace.setText(airports.get(destination).getCity() + ", " + airports.get(destination).getCountry());
        }
        int countPos = 0;
        // flight origin & destination found, we need to find their index on the ST to send to the Dijkstra Algorithm
        for (String code : airports.keys()) {
            if (code.compareTo(origin) == 0) {
                originPos = countPos;
            } else if (code.compareTo(destination) == 0) {
                destinPos = countPos;
            }
            countPos++;
        }
        flightType.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (flightType.getSelectedToggle() != null) {
                    typeSelected = true; // the user chose a type, from now on there's no way a type isn't chosen when submitting
                    typeOfFlight = ((RadioButton)new_toggle).getText();
                }
            }
        });
    }

    @FXML
    public void submitFlight(){
        if(typeOfFlight.isEmpty()){
            warning.setText("Please choose a type of flight.");
            warning.setStyle("-fx-opacity: 1");
        }else if(!Utils.isNumeric(flightPassengers.getText())){
            warning.setText("Please enter a valid number of passengers. (Integer value)");
            warning.setStyle("-fx-opacity: 1");
        }else{
            // calculate final route chosen
            calculateRoute();
            // set the flight departure date to the current one
            warning.setStyle("-fx-opacity: 0");
            System.out.println("Type selected: " + typeOfFlight);
            System.out.println("Passengers: " + Integer.valueOf(flightPassengers.getText()));
            System.out.println("origin: " + originPos + " destin: " + destinPos);
            System.out.println("Departing at: " + new Date());
            System.out.println("... Submitting ...");
        }
    }

    @FXML
    public void calculateRoute(){
        minWeight = 0;
        RedBlackBST<Integer, Airplane> airplanes = airports.get(origin).getAirplanes();
        switch (typeOfFlight){
           case "Shortest Distance":
               System.out.println("Calculating... Shortest Distance");
               dijkstraSP = new DijkstraSP(originPos, null, "distance");
               break;
           case "Cheapest Flight":
               System.out.println("Calculating... Cheapest Flight");
               if(airplanes.isEmpty()){
                   System.out.println("ERR");
               }else{
                   for(int id : airplanes.keys()){
                       System.out.println("Calculating " + id);
                       DijkstraSP dijkstra = new DijkstraSP(originPos, airplanes.get(id), "monetary");
                       System.out.println(id + " -> " + dijkstra.distTo(destinPos));
                       if(dijkstra.distTo(destinPos) < minWeight){
                           minWeight = dijkstra.distTo(destinPos);
                           dijkstraSP = dijkstra;
                           airplaneUsed = airplanes.get(id);
                       }
                   }
               }
               System.out.println("Min cost: " + minWeight + " | " + airplaneUsed.getId() + " ->" + dijkstraSP.distTo(destinPos));
               break;
           case "Fastest Flight":
               System.out.println("Calculating... Fastest Flight");
               for(int id : airplanes.keys()){
                   DijkstraSP dijkstra = new DijkstraSP(originPos, airplanes.get(id), "time");
               }
               break;
           case "Less Stops":
               System.out.println("Calculating... Less Stops");
               bfs = new BreadthFirstPaths(symbolGraph.G(), originPos);
               break;
           default: break;
        }
    }

    public void setPoints(String origin, String destination){
       this.origin = origin;
       this.destination = destination;
    }
}
