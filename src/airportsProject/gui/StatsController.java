package airportsProject.gui;

import airportsProject.Airport;
import airportsProject.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    private LineChart mostTrafficAirport;
    @FXML
    private LineChart mostPassengersFlight;
    @FXML
    private LineChart mostPassengersAirport;

    private List<Map.Entry<String, Integer>> results;
    private SeparateChainingHashST<String, Airport> airports = Utils.getInstance().getAirports();

    public void initialize(){
//        mostTrafficAirport.getData().add(getMostTrafficAirport());
    }

    private ObservableList<XYChart.Series<String, Integer>> getMostTrafficAirport(){
        ObservableList<XYChart.Series<String, Integer>> data = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> airport1 = new XYChart.Series<>();
        XYChart.Series<String, Integer> airport2 = new XYChart.Series<>();
        XYChart.Series<String, Integer> airport3 = new XYChart.Series<>();
        XYChart.Series<String, Integer> airport4 = new XYChart.Series<>();
        XYChart.Series<String, Integer> airport5 = new XYChart.Series<>();

        results = Utils.mostTrafficAirport();
        airport1.setName(airports.get(results.get(0).getKey()).getName());
        airport1.getData().add(new XYChart.Data(results.get(0).getKey(), results.get(0).getValue()));
        airport2.setName(airports.get(results.get(1).getKey()).getName());
        airport2.getData().add(new XYChart.Data(results.get(1).getKey(), results.get(1).getValue()));
        airport3.setName(airports.get(results.get(2).getKey()).getName());
        airport3.getData().add(new XYChart.Data(results.get(2).getKey(), results.get(2).getValue()));
        airport4.setName(airports.get(results.get(3).getKey()).getName());
        airport4.getData().add(new XYChart.Data(results.get(3).getKey(), results.get(3).getValue()));
        airport5.setName(airports.get(results.get(4).getKey()).getName());
        airport5.getData().add(new XYChart.Data(results.get(4).getKey(), results.get(4).getValue()));

        data.addAll(airport1, airport2, airport3, airport4, airport5);
        return data;
    }

    @FXML
    void optionClicked(MouseEvent event){
        if(event.getSource().equals(goBack)) {
            VistaNavigator.loadVista(VistaNavigator.MENU);
        }
    }
}
