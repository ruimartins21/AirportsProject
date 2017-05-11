package airportsProject.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class MenuController {
    @FXML
    private Pane airportNetW;
    @FXML
    private Pane airplanes;
    @FXML
    private Pane airlines;
    @FXML
    private Pane flights;
    @FXML
    private Pane statistics;
    @FXML
    private Pane saveProgram;
    @FXML
    private Label credits;
    @FXML
    private VBox menuContainer;
    @FXML
    private ImageView goBack;

    @FXML
    void hoverIn(MouseEvent event) {
        if (event.getSource().equals(airportNetW)) {
            airportNetW.setStyle("-fx-background-color: #2D6AB0");
        }else if(event.getSource().equals(airplanes)){
            airplanes.setStyle("-fx-background-color: #2D6AB0");
        }else if(event.getSource().equals(airlines)){
            airlines.setStyle("-fx-background-color: #2D6AB0");
        }else if(event.getSource().equals(flights)){
            flights.setStyle("-fx-background-color: #2D6AB0");
        }else if(event.getSource().equals(saveProgram)){
            saveProgram.setStyle("-fx-background-color: #2D6AB0");
        }else{
            statistics.setStyle("-fx-background-color: #EEEEEE;");
        }
    }

    @FXML
    void hoverOut(MouseEvent event) {
        if (event.getSource().equals(airportNetW)) {
            airportNetW.setStyle("-fx-background-color: #4185d1");
        }else if(event.getSource().equals(airplanes)){
            airplanes.setStyle("-fx-background-color: #4185d1");
        }else if(event.getSource().equals(airlines)){
            airlines.setStyle("-fx-background-color: #4185d1");
        }else if(event.getSource().equals(flights)){
            flights.setStyle("-fx-background-color: #4185d1");
        }else if(event.getSource().equals(saveProgram)){
            saveProgram.setStyle("-fx-background-color: #4185d1");
        }else{
            statistics.setStyle("-fx-background-color: white");
        }
    }

    @FXML
    void optionClicked(MouseEvent event){
        if (event.getSource().equals(airportNetW)) {
            VistaNavigator.loadVista(VistaNavigator.AIRPORTNETW);
        }else if(event.getSource().equals(airplanes)){
            VistaNavigator.loadVista(VistaNavigator.AIRPLANELIST);
        }else if(event.getSource().equals(airlines)){
            VistaNavigator.loadVista(VistaNavigator.AIRLINELIST);
        }else if(event.getSource().equals(flights)){
            VistaNavigator.loadVista(VistaNavigator.FLIGHTLIST);
        }else if(event.getSource().equals(saveProgram)){
            System.out.println("Saving program");
        }else if(event.getSource().equals(goBack)){
            VistaNavigator.loadVista(VistaNavigator.LANDING);
        }else{
            VistaNavigator.loadVista(VistaNavigator.STATS);
        }
    }

    @FXML
    void inCredits(MouseEvent event) {
        credits.setStyle("-fx-background-color: rgba(0,0,0,0.7)");
    }

    @FXML
    void outCredits(MouseEvent event) {
        credits.setStyle("-fx-background-color: rgba(0,0,0,0.3)");
    }

    @FXML
    void openCredits(MouseEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(menuContainer.getScene().getWindow());
        try{
            Parent root = FXMLLoader.load(getClass().getResource("creditsDialog.fxml"));
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
        }catch (IOException e){
            System.out.println("Couldn't load the credits window");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CLOSE){
//            System.out.println("Close Credits");
        }else{
            System.out.println("Action not recognized");
        }
    }
}
