package airportsProject.gui;

import airportsProject.Airport;
import airportsProject.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import libs.SeparateChainingHashST;

import java.util.List;
import java.util.Map;

public class StatsController {
    @FXML
    private HBox containCharts;
    @FXML
    private Pane goBack;

    @FXML
    CategoryAxis xAxis = new CategoryAxis();
    @FXML
    NumberAxis yAxis;

    private SeparateChainingHashST<String, Airport> airports        = Utils.getInstance().getAirports();
    private List<Map.Entry<String, Integer>> mostTrafficAirports    = Utils.mostTrafficAirport();
    private List<Map.Entry<String, Integer>> mostPassengersFlights  = Utils.mostPassengersFlight();
    private List<Map.Entry<String, Integer>> mostPassengersAirports = Utils.mostPassengersAirport();

    public void initialize(){
        int maxValue = mostTrafficAirports.size() > 0 ? mostTrafficAirports.get(0).getValue() : 0;
        yAxis = new NumberAxis("Number of Flights", 0, maxValue+5, 1);
        BarChart mostTrafficAirport = new BarChart(xAxis, yAxis, getMostTrafficAirport());
        mostTrafficAirport.setBarGap(0);
        containCharts.getChildren().add(mostTrafficAirport);

        maxValue = mostPassengersFlights.size() > 0 ? mostPassengersFlights.get(0).getValue() : 0;
        yAxis = new NumberAxis("Number of Passengers", 0, maxValue+5, 1);
        BarChart mostPassengersFlight = new BarChart(xAxis, yAxis, getMostPassengersFlight());
        mostPassengersFlight.setPrefHeight(300);
        mostPassengersFlight.setBarGap(0);
        containCharts.getChildren().add(mostPassengersFlight);

        maxValue = mostPassengersAirports.size() > 0 ? mostPassengersAirports.get(0).getValue() : 0;
        yAxis = new NumberAxis("Number of Passengers", 0, maxValue+5, 1);
        BarChart mostPassengersAirport = new BarChart(xAxis, yAxis, getMostPassengersAirport());
        mostPassengersAirport.setBarGap(0);
        containCharts.getChildren().add(mostPassengersAirport);
    }

    private ObservableList<BarChart.Series<String, Integer>> getMostTrafficAirport(){
        ObservableList<BarChart.Series<String, Integer>> data = FXCollections.observableArrayList();
        for (int i = 0; i < mostTrafficAirports.size(); i++){
            BarChart.Series<String, Integer> airport = new BarChart.Series<>();
            airport.setName(airports.get(mostTrafficAirports.get(i).getKey()).getName());
            airport.getData().add(new BarChart.Data(mostTrafficAirports.get(i).getKey(), mostTrafficAirports.get(i).getValue()));
            data.add(airport);
        }
        return data;
    }

    private ObservableList<BarChart.Series<String, Integer>> getMostPassengersFlight(){
        ObservableList<BarChart.Series<String, Integer>> data = FXCollections.observableArrayList();
        for (int i = 0; i < mostPassengersFlights.size(); i++){
            BarChart.Series<String, Integer> flight = new BarChart.Series<>();
            flight.setName(mostPassengersFlights.get(i).getKey());
            flight.getData().add(new BarChart.Data(mostPassengersFlights.get(i).getKey(), mostPassengersFlights.get(i).getValue()));
            data.add(flight);
        }
        return data;
    }

    private ObservableList<BarChart.Series<String, Integer>> getMostPassengersAirport(){
        ObservableList<BarChart.Series<String, Integer>> data = FXCollections.observableArrayList();
        for (int i = 0; i < mostPassengersAirports.size(); i++){
            BarChart.Series<String, Integer> airport = new BarChart.Series<>();
            airport.setName(airports.get(mostTrafficAirports.get(i).getKey()).getName());
            airport.getData().add(new BarChart.Data(mostPassengersAirports.get(i).getKey(), mostPassengersAirports.get(i).getValue()));
            data.add(airport);
        }
        return data;
    }

    @FXML
    void optionClicked(MouseEvent event){
        if(event.getSource().equals(goBack)) {
            VistaNavigator.loadVista(VistaNavigator.MENU);
        }
    }
}
