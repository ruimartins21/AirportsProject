package airportsProject.gui;
import airportsProject.Date;
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
//    public static final String MAPTEST           = "mapTest.fxml";
    public static final String LANDING           = "landingPage.fxml";
    public static final String MENU              = "menu.fxml";
//    public static final String STATS             = "stats.fxml";
//    public static final String SEARCH            = "search.fxml";
    public static final String FLIGHTLIST        = "flights.fxml";
    public static final String FLIGHTDETAILS     = "flightDetails.fxml";
    public static final String AIRPORTNETW       = "airportNetwork.fxml";
    public static final String AIRPORTDETAILS    = "airportDetails.fxml";
    public static final String AIRPLANELIST      = "airplanes.fxml";
    public static final String AIRPLANEDETAILS   = "airplaneDetails.fxml";
    public static final String AIRLINELIST       = "airlines.fxml";
    public static final String AIRLINEDETAILS    = "airlineDetails.fxml";
    public static final String GRAPHS            = "graphStreamEx.fxml";

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
        try {
            mainGUIController.setVista(FXMLLoader.load(VistaNavigator.class.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param param receives an ID
     */
    public static void loadVista(String fxml, int param) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(VistaNavigator.class.getResource(fxml));
            // set controller factory allows the setting of the ID value needed for the page requested BEFORE the page is loaded
            // without this control of the order the page would load and when the initialize function in the new page was called,
            // the value needed there wouldn't be there yet
            loader.setControllerFactory((Class<?> controllerType) -> {
                if(controllerType == AirplaneDetailsController.class){ // sends the id of the airplane to show
                    AirplaneDetailsController controller = new AirplaneDetailsController();
                    controller.setId(param);
                    return controller;
                } else {
                    try {
                        return controllerType.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Node node = loader.load();
            mainGUIController.setVista(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key receives an airport code or an airline name depending on what to show
     */
    public static void loadVista(String fxml, String key) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(VistaNavigator.class.getResource(fxml));
            // set controller factory allows the setting of the ID value needed for the page requested BEFORE the page is loaded
            // without this control of the order the page would load and when the initialize function in the new page was called,
            // the value needed there wouldn't be there yet
            loader.setControllerFactory((Class<?> controllerType) -> {
                if (controllerType == AirportDetailsController.class) { // send the code of the airport to show its details
                    AirportDetailsController controller = new AirportDetailsController();
                    controller.setCode(key);
                    return controller;
                }else if(controllerType == AirlineDetailsController.class) { // send the name of the airline
                    AirlineDetailsController controller = new  AirlineDetailsController();
                    controller.setAirline(key);
                    return controller;
                }else{
                    try {
                        return controllerType.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Node node = loader.load();
            mainGUIController.setVista(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param date receives the date of a flight
     */
    public static void loadVista(String fxml, Date date) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(VistaNavigator.class.getResource(fxml));
            // set controller factory allows the setting of the ID value needed for the page requested BEFORE the page is loaded
            // without this control of the order the page would load and when the initialize function in the new page was called,
            // the value needed there wouldn't be there yet
            loader.setControllerFactory((Class<?> controllerType) -> {
                if (controllerType == FlightDetailsController.class) { // send the id of the airline to show its details
                    FlightDetailsController controller = new FlightDetailsController();
                    controller.setDate(date);
                    return controller;
                } else {
                    try {
                        return controllerType.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Node node = loader.load();
            mainGUIController.setVista(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}