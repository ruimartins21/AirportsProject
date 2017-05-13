package airportsProject.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class StatsController {
    @FXML
    private HBox containCharts;
    @FXML
    private ImageView goBack;

    public void initialize(){

    }

    private ObservableList<XYChart.Series<String, Double>> getMostrTrafficAirport(){
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();

        return data;
    }

    @FXML
    void optionClicked(MouseEvent event){
        if(event.getSource().equals(goBack)) {
            VistaNavigator.loadVista(VistaNavigator.LANDING);
        }
    }
}
