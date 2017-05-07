package airportsProject.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * MainGUI application class.
 */
public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Airports Management");
        stage.setScene(createScene(loadMainPane()));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Loads the main fxml layout.
     * Sets up the vista switching VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane mainPane = (Pane) loader.load(
            getClass().getResourceAsStream(VistaNavigator.MAIN) // main Vista that holds all the vistas to show
        );

        MainGUIController mainGUIController = loader.getController();

        VistaNavigator.setMainGUIController(mainGUIController);
        VistaNavigator.loadVista(VistaNavigator.LANDING); // landing page, first page to show, the program flow will happen from here
//        VistaNavigator.loadVista(VistaNavigator.MAPTEST);

        return mainPane;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane);
//        scene.getStylesheets().setAll(
//            getClass().getResource("style.css").toExternalForm()
//        );
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
