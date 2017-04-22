package airportsProject.gui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate
 * simple access from anywhere in the application.
 */
public class VistaNavigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN              = "mainGUI.fxml";
    public static final String LANDING           = "landingPage.fxml";
    public static final String MENU              = "menu.fxml";
//    public static final String CREDITS           = "credits.fxml";
//    public static final String STATS             = "stats.fxml";
//    public static final String SEARCH            = "search.fxml";
//    public static final String FLIGHTLIST        = "flightList.fxml";
//    public static final String FLIGHTDETAILS     = "flightDetails.fxml";
//    public static final String AIRPORTNETW       = "airportNetW.fxml";
//    public static final String AIRPORTDETAILS    = "airportDetails.fxml";
//    public static final String AIRPLANELIST      = "airplaneList.fxml";
//    public static final String AIRPLANEDETAILS   = "airplaneDetails.fxml";
//    public static final String AIRLINELIST       = "airlineList.fxml";
//    public static final String AIRLINEDETAILS    = "airlineDetails.fxml";

    /** The main application layout controller. */
    private static MainGUIController mainGUIController;

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainGUIController the main application layout controller.
     */
    public static void setMainGUIController(MainGUIController mainGUIController) {
        VistaNavigator.mainGUIController = mainGUIController;
    }

    /**
     * Loads the vista specified by the fxml file into the
     * vistaHolder pane of the main application layout.
     *
     * Previously loaded vista for the same fxml file are not cached.
     * The fxml is loaded anew and a new vista node hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     *   cache FXMLLoaders
     *   cache loaded vista nodes, so they can be recalled or reused
     *   allow a user to specify vista node reuse or new creation
     *   allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadVista(String fxml) {
        System.out.println(fxml);
        try {
            mainGUIController.setVista(FXMLLoader.load(VistaNavigator.class.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives data as parameter
     * @param lastVista data as parameter
     */
    public static void loadVista(String fxml, int lastVista) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(VistaNavigator.class.getResource(fxml));
            Node node = loader.load();
            mainGUIController.setVista(node);
            switch (fxml) {
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}