package airportsProject.gui;

import airportsProject.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

/**
 * Page for showing some statistics for airports and flights
 */
public class StatsController {
    @FXML
    private VBox chart1;
    @FXML
    private VBox chart2;
    @FXML
    private VBox chart3;
    @FXML
    private Pane goBack;

    private List<Map.Entry<String, Integer>> mostTrafficAirports    = Utils.mostTrafficAirport();
    private List<Map.Entry<String, Integer>> mostPassengersFlights  = Utils.mostPassengersFlight();
    private List<Map.Entry<String, Integer>> mostPassengersAirports = Utils.mostPassengersAirport();

    public void initialize(){
        int maxValue = mostTrafficAirports.size() > 0 ? mostTrafficAirports.get(0).getValue() : 0;
        NumberAxis yAxis = new NumberAxis("Number of Flights", 0, maxValue+10, 1);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Airports");
        BarChart mostTrafficAirport = new BarChart(xAxis, yAxis);
        mostTrafficAirport.setLegendVisible(false);
        mostTrafficAirport.setAnimated(false);
        mostTrafficAirport.getData().add(getMostTrafficAirport());
        mostTrafficAirport.setBarGap(0);
        chart1.getChildren().add(mostTrafficAirport);

        maxValue = mostPassengersFlights.size() > 0 ? mostPassengersFlights.get(0).getValue() : 0;
        NumberAxis yAxis2 = new NumberAxis("Number of Passengers", 0, maxValue+20, 1);
        CategoryAxis xAxis2 = new CategoryAxis();
        xAxis2.setLabel("Flights");
        BarChart mostPassengersFlight = new BarChart(xAxis2, yAxis2);
        mostPassengersFlight.setLegendVisible(false);
        mostPassengersFlight.setAnimated(false);
        mostPassengersFlight.getData().add(getMostPassengersFlight());
        mostPassengersFlight.setBarGap(0);
        chart2.getChildren().add(mostPassengersFlight);

        maxValue = mostPassengersAirports.size() > 0 ? mostPassengersAirports.get(0).getValue() : 0;
        NumberAxis yAxis3 = new NumberAxis("Number of Passengers", 0, maxValue+20, 1);
        CategoryAxis xAxis3 = new CategoryAxis();
        xAxis3.setLabel("Airports");
        BarChart mostPassengersAirport = new BarChart(xAxis3, yAxis3);
        mostPassengersAirport.setLegendVisible(false);
        mostPassengersAirport.setAnimated(false);
        mostPassengersAirport.getData().add(getMostPassengersAirport());
        mostPassengersAirport.setBarGap(0);
        chart3.getChildren().add(mostPassengersAirport);
    }

    private BarChart.Series<String, Integer> getMostTrafficAirport(){
        BarChart.Series<String, Integer> data = new BarChart.Series();
        data.setName("Airports");
        for (int i = 0; i < mostTrafficAirports.size(); i++){
            BarChart.Data<String, Integer> airport = new BarChart.Data();
            airport.setXValue(mostTrafficAirports.get(i).getKey());
            airport.setYValue(mostTrafficAirports.get(i).getValue());
            airport.setExtraValue(i);
            airport.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        if(airport.getExtraValue().equals(0)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_1;");
                        }else if(airport.getExtraValue().equals(1)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_2;");
                        }else if(airport.getExtraValue().equals(2)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_3;");
                        }else if(airport.getExtraValue().equals(3)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_4;");
                        }else{
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_5;");
                        }
                        displayLabel(airport);
                    }
                }
            });
            data.getData().add(airport);
        }
        return data;
    }

    private BarChart.Series<String, Integer> getMostPassengersFlight(){
        BarChart.Series<String, Integer> data = new BarChart.Series();
        data.setName("Flights");
        for (int i = 0; i < mostPassengersFlights.size(); i++){
            BarChart.Data<String, Integer> flight = new BarChart.Data();
            flight.setXValue(mostPassengersFlights.get(i).getKey());
            flight.setYValue(mostPassengersFlights.get(i).getValue());
            flight.setExtraValue(i);
            flight.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        if(flight.getExtraValue().equals(0)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_1;");
                        }else if(flight.getExtraValue().equals(1)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_2;");
                        }else if(flight.getExtraValue().equals(2)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_3;");
                        }else if(flight.getExtraValue().equals(3)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_4;");
                        }else{
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_5;");
                        }
                        displayLabel(flight);
                    }
                }
            });
            data.getData().add(flight);
        }
        return data;
    }

    private BarChart.Series<String, Integer> getMostPassengersAirport(){
        BarChart.Series<String, Integer> data = new BarChart.Series();
        data.setName("Airports");
        for (int i = 0; i < mostPassengersAirports.size(); i++){
            BarChart.Data<String, Integer> airport = new BarChart.Data();
            airport.setXValue(mostPassengersAirports.get(i).getKey());
            airport.setYValue(mostPassengersAirports.get(i).getValue());
            airport.setExtraValue(i);
            airport.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        if(airport.getExtraValue().equals(0)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_1;");
                        }else if(airport.getExtraValue().equals(1)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_2;");
                        }else if(airport.getExtraValue().equals(2)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_3;");
                        }else if(airport.getExtraValue().equals(3)){
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_4;");
                        }else{
                            newNode.setStyle("-fx-bar-fill: CHART_COLOR_5;");
                        }
                        displayLabel(airport);
                    }
                }
            });
            data.getData().add(airport);
        }
        return data;
    }

    /**
     * places a text label with a bar's value above a bar node
     * @param data -> the node where the label will be inserted
     */
    private void displayLabel(BarChart.Data<String, Integer> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        dataText.setFont(Font.font("Helvetica", 10));
        dataText.setFill(Color.WHITE);
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });
        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
                dataText.setLayoutY(
                        Math.round(bounds.getMinY() - dataText.prefHeight(-1) * 0.5));
            }
        });
    }

    @FXML
    void optionClicked(MouseEvent event){
        if(event.getSource().equals(goBack)) {
            VistaNavigator.loadVista(VistaNavigator.MENU);
        }
    }
}
