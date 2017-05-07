package airportsProject.gui;

import airportsProject.Utils;
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
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

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
        if(event.getSource().equals(newProgram)){ // new program
            Utils.initProgram("");
            VistaNavigator.loadVista(VistaNavigator.MENU); // jumps to the menu after the new program is created
        }else{ // load a program
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text", "*.txt"),
                    new FileChooser.ExtensionFilter("Binary", "*.bin")
            );
            // opens a dialog window where the user chooses the file, this dialog is created inside the scene window what prevents
            // the user from dragging it and from using the program while this dialog is open
            File pathToFile = fileChooser.showOpenDialog(containLanding.getScene().getWindow());
            if(pathToFile != null){
                if(Utils.initProgram(pathToFile.getPath())) // loads the program requested
                    VistaNavigator.loadVista(VistaNavigator.MENU); // jumps to the menu page
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
        dialog.initStyle(StageStyle.UNDECORATED);
        try{
            Parent credits = FXMLLoader.load(getClass().getResource("creditsDialog.fxml"));
            dialog.getDialogPane().setContent(credits);
            dialog.getDialogPane().getStylesheets().add("airportsProject/gui/style.css");
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
