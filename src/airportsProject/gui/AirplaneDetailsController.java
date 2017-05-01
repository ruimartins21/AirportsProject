package airportsProject.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AirplaneDetailsController {
    @FXML
    private VBox airplaneContainer;
    @FXML
    private Pane editAirplane;
    @FXML
    private Pane removeAirplane;
    @FXML
    private Label airplaneName1;
    @FXML
    private Label airplaneName2;
    @FXML
    private Label airplaneModel;
    @FXML
    private Label airplaneAirline;
    @FXML
    private Label airplaneCruiseSpeed;
    @FXML
    private Label airplaneCruiseAltitude;
    @FXML
    private Label airplaneMaxDistance;
    @FXML
    private Label airplanePassengers;
    @FXML
    private Label airplaneFuelCap;
    @FXML
    private Label airplaneParked;

    private int airplaneId = -1;

    public void initialize(){
        // ir buscar airline a ST pelo id dado
        airplaneName1.setText(String.valueOf(airplaneId).toUpperCase());
        airplaneName2.setText(String.valueOf(airplaneId));
        airplaneModel.setText("Airbus A340");
        airplaneAirline.setText("TAP Air Portugal");
        airplaneCruiseSpeed.setText("200 km/h");
        airplaneCruiseAltitude.setText("2000 km");
        airplaneMaxDistance.setText("10000 km");
        airplanePassengers.setText("300");
        airplaneFuelCap.setText("2500 L");
        airplaneParked.setText("Airport Francisco Sá Carneiro");
    }

    public void setId(int id){this.airplaneId = id;}

    @FXML
    void gotoMenu(MouseEvent event) {
        VistaNavigator.loadVista(VistaNavigator.MENU);
    }

    @FXML
    void hoverIn(MouseEvent event) {
        if(event.getSource().equals(editAirplane)){
            editAirplane.setStyle("-fx-background-color: rgba(270,270,270,0.2);");
        }else{
            removeAirplane.setStyle("-fx-background-color: rgba(270,270,270,0.2);");
        }
    }

    @FXML
    void hoverOut(MouseEvent event) {
        if(event.getSource().equals(editAirplane)){
            editAirplane.setStyle("-fx-background-color: transparent;");
        }else{
            removeAirplane.setStyle("-fx-background-color: transparent;");
        }
    }

    @FXML
    void optionClicked(MouseEvent event) {
        if(event.getSource().equals(editAirplane)){
            System.out.println("Editing Airplane");
        }else{
            System.out.println("Removing Airplane");
        }
    }
}
