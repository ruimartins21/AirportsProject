package airportsProject.gui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class StatsController {
    @FXML
    private HBox containCharts;
    @FXML
    private ImageView goBack;

    @FXML
    void optionClicked(MouseEvent event){
        if(event.getSource().equals(goBack)) {
            VistaNavigator.loadVista(VistaNavigator.LANDING);
        }
    }
}
