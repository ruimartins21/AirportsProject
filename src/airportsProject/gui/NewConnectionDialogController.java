package airportsProject.gui;

import airportsProject.Airport;
import airportsProject.Connection;
import airportsProject.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import libs.SeparateChainingHashST;

import java.util.ArrayList;

import static airportsProject.Utils.isNumeric;
import static airportsProject.Utils.log;

public class NewConnectionDialogController{
    @FXML
    private TextField distance;
    @FXML
    private TextField altitude;
    @FXML
    private TextField windSpeed;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private Label warning;

    private Airport airport;
    private Utils utils = Utils.getInstance();
    private SeparateChainingHashST<String, Airport> airports = utils.getAirports();
    private ArrayList<String> listAirports = new ArrayList<>(); // will store the airports to where this airport can connect

    public void initialize(){
        ObservableList<String> airportsList = FXCollections.observableArrayList();
        ArrayList<String> connections = utils.airportConnections(airport);
        for (int i = 0; i < utils.getSymbolGraph().digraph().V(); i++) {
            if(airport.getCode().compareTo(utils.getSymbolGraph().nameOf(i)) != 0){ // can't list the airport from which the connection starts
                if(!connections.contains(utils.getSymbolGraph().nameOf(i))){ // it only lists the airports to where this airport doesn't have a connection to yet
                    Airport airport = airports.get(utils.getSymbolGraph().nameOf(i));
                    listAirports.add(utils.getSymbolGraph().nameOf(i));
                    airportsList.add(airport.getCode() + " - " + airport.getName() + " (" + airport.getCountry() + ")");
                }
            }
        }
        destination.setItems(airportsList);
        destination.getItems().add(0, "Select Destination");
        destination.getSelectionModel().selectFirst();
        destination.setFocusTraversable(false);
        warning.setStyle("-fx-opacity: 0");
    }

    @FXML
    void getNewConnectionInput(ActionEvent event) {
        if(!isNumeric(distance.getText()) || !isNumeric(altitude.getText()) || !isNumeric(windSpeed.getText()) ||
                destination.getSelectionModel().getSelectedIndex() == 0){
            warning.setStyle("-fx-opacity: 1");
        }else{
            warning.setStyle("-fx-opacity: 0");
            int count = 1;
            String destinCode = "";
            for (int i = 0; i < listAirports.size(); i++) {
                if(count == destination.getSelectionModel().getSelectedIndex()){
                    destinCode = listAirports.get(i);
                }
                count++;
            }
            Connection c = new Connection(utils.getSymbolGraph().indexOf(airport.getCode()), utils.getSymbolGraph().indexOf(destinCode),
                    Double.valueOf(distance.getText()), Double.valueOf(altitude.getText()), Double.valueOf(windSpeed.getText()));
            utils.getSymbolGraph().digraph().addEdge(c);
            log("SymbolGraph", "New connection between " + airport.getCode() + " and " + destinCode);
            // close the dialog window
            distance.getParent().getParent().getScene().getWindow().hide();
        }
    }

    public void setOrigin(Airport airport){
        this.airport = airport;
    }
}
