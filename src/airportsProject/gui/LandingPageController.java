package airportsProject.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LandingPageController {
    @FXML
    private Pane newProgram;
    @FXML
    private Pane loadProgram;
    @FXML
    private Label credits;
    @FXML
    private VBox containLanding;

    @FXML
    void hoverIn(MouseEvent event) {
        if(event.getSource().equals(newProgram)){
            newProgram.setStyle("-fx-background-color: #2D6AB0");
        }else{
            loadProgram.setStyle("-fx-background-color: #2D6AB0");
        }
    }

    @FXML
    void hoverOut(MouseEvent event) {
        if(event.getSource().equals(newProgram)){
            newProgram.setStyle("-fx-background-color: #4185d1");
        }else{
            loadProgram.setStyle("-fx-background-color: #4185d1");
        }
    }

    @FXML
    void optionClicked(MouseEvent event){
        if(event.getSource().equals(newProgram)){
//            System.out.println("Creating new program ...");
//            log("reset", ""); // clean the logs file
//            ImportFromFile.importAirports(airportST, pathAirports);
//            ImportFromFile.importAirlines(airlinesST, pathAirlines);
//            ImportFromFile.importPlanes(airportST, airplaneST, airlinesST, pathAirplanes);
            VistaNavigator.loadVista(VistaNavigator.MENU); // jumps to the menu after the new program is created
        }else{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text", "*.txt"),
                    new FileChooser.ExtensionFilter("Binary", "*.bin")
            );
            File pathToFile = fileChooser.showOpenDialog(containLanding.getScene().getWindow());
            if(pathToFile != null){
                System.out.println("File: " + pathToFile.getPath());
//                if(ImportFromFile.currentProgram(pathToFile,airportST,airlinesST,airplaneST,flightST)){
//                    File file = new File(".//data//currentProgram.txt");
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                    System.out.println(" (Last opened in: " + sdf.format(file.lastModified()) + ")");
//                    validChoice = true;
//                }else{
//                    System.out.println("Error opening previous program.");
//                }
            }else{
                System.out.println("Load program cancelled");
            }
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
        dialog.initOwner(containLanding.getScene().getWindow());
        try{
            Parent root = FXMLLoader.load(getClass().getResource("creditsDialog.fxml"));
            dialog.getDialogPane().setContent(root);
        }catch (IOException e){
            System.out.println("Couldn't load the credits window");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CLOSE){
//            System.out.println("Close Credits");
        }else{
            System.out.println("Action not recognized");
        }
    }
}
