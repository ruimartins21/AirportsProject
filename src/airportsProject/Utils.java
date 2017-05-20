package airportsProject;

import airportsProject.Exceptions.WrongTypeFileException;
import libs.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    public static int extraValue = 200;
    public static int euroValue = 30;  // 30 €/L
    public static int windCost = 10;   // aditional cost by km/h of wind (adds 10L per km/h to the airplane fuel cost if it is against the airplane, or substracts if in favor)
    public static int mapWidth = 1536; // width of the world map used
    public static int mapHeight = 768; // height of the world map used

    private static Utils instance = null;
    private static ArrayList<String> remove = new ArrayList<>(); // array that will store removed airports to ignore when reading the graph from the file

    // paths to default data
//    public transient Thread asd;
    private static final String pathAirports = ".//data//airports.txt";
    private static final String pathAirplanes = ".//data//airplanes.txt";
    private static final String pathAirlines = ".//data//airlines.txt";

    // Symbol Tables
    private static SeparateChainingHashST<String, Airport> airportST = new SeparateChainingHashST<>();
    private static SeparateChainingHashST<String, Airline> airlineST = new SeparateChainingHashST<>();
    private static RedBlackBST<Integer, Airplane> airplaneST = new RedBlackBST<>();
    private static RedBlackBST<Date, Flight> flightST = new RedBlackBST<>();
    private static SymbolEdgeWeightedDigraph mainGraph = new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";");
    private static SymbolEdgeWeightedDigraph symbolGraph = new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";");

    protected Utils() {
        // prevents instantiation
    }

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public SymbolEdgeWeightedDigraph getSymbolGraph() {
        return symbolGraph;
    }

    public static void setSymbolGraph(SymbolEdgeWeightedDigraph sGraph) {
        symbolGraph = sGraph;
    }

    public static void filterGraph(String continent){
        symbolGraph = new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";", filterAirportsByContinent(continent));
    }

    /**
     * Will fill the symbol tables with the data required
     *
     * @param path -> will be empty if the user chooses to create a new program and will bring a file path if
     *             he chooses to load a program
     */
    public static boolean initProgram(String path) {
        log("reset", ""); // clean the log file from the previous program
        if (path.length() > 0) { // load program
            try {
                ImportFromFile.loadProgram(path, airportST, airlineST, airplaneST);
                File file = new File(path);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                System.out.println("Last opened in: " + sdf.format(file.lastModified()));
            } catch (WrongTypeFileException wt) {
                System.out.println(wt.getMessage());
                return false;
            }
        } else { // new program
            ImportFromFile.importAirports(airportST, pathAirports);
            ImportFromFile.importAirlines(airlineST, pathAirlines);
            ImportFromFile.importPlanes(airportST, airplaneST, airlineST, pathAirplanes);
            setFlights(new RedBlackBST<>());
            setSymbolGraph(new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";"));
        }
        return true;
    }

    /* AIRPORTS */

    public SeparateChainingHashST<String, Airport> getAirports() {
        return airportST;
    }

    public static void setAirports(SeparateChainingHashST<String, Airport> airports) {
        airportST = airports;
    }

    public void newAirport(Airport airport) {
        if (remove.contains(airport.getCode())) // if the user is inserting an airport with a code equals to an airport removed before
            remove.remove(airport.getCode());
        airportST.put(airport.getCode(), airport);
//        add airport to a vertice of graph
        log("airportST", "New airport [" + airport.getCode() + "] \" Name:" + airport.getName() + "\" Rating:" +
                airport.getRating());
        if (!symbolGraph.contains(airport.getCode())) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(".//data//backup.txt", true))) { // FileWriter with only one parameter will overwrite the file content each time that is what we want
                bw.write(airport.getCode() + ";");
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ImportFromFile.loadGraph();
            } catch (WrongTypeFileException wt) {
                System.out.println(wt.getMessage());
            }
        }
    }

    /**
     * Receives the data possible to be edited on an airport and changes it using the class setters
     *
     * @param code   code of the airport to edit
     * @param name   new name of the airport
     * @param rating new rating of the airport
     */
    public void editAirport(String code, String name, Float rating) {
        airportST.get(code).setName(name);
        airportST.get(code).setRating(rating);
        log("airportST", "Edited airport [" + airportST.get(code).getCode() + "] \" Name:" + airportST.get(code).getName() + "\" Rating:" +
                airportST.get(code).getRating());
    }

    /**
     * Removes an airport incluiding all the airplanes parked there
     *
     * @param airport airport to remove
     */
    public static void removeAirport(Airport airport) {
        if (!airport.getAirplanes().isEmpty()) { // all the airplanes parked on the airport are removed aswell
            for (Integer p : airportST.get(airport.getCode()).getAirplanes().keys()) {
                removeAirplane(airportST.get(airport.getCode()).getAirplanes().get(p));
            }
        }
        if (!airport.getFlights().isEmpty()) { // all the flights that passed on the airport are removed too, it can't exist a history of a flight that passed through an airport that doesn't exist
            for (Date date : airport.getFlights().keys()) {
                Flight flight = flightST.get(date);
                for (String code : flight.getConnections()) { // removes the flight from the history of all the airports from where it passed
                    airportST.get(code).getFlights().put(date, null);
                }
                flight.getAirplane().getAirplaneFlights().put(date, null); // removes it from the airplane history aswell
                log("flightST", "Removed Flight from \"" + flight.getAirportOfOrigin().getName() + "\" (" + flight.getAirportOfOrigin().getCity() + ", " +
                        flight.getAirportOfOrigin().getCountry() + ") to \"" + flight.getAirportOfDestination().getName() + "\" (" +
                        flight.getAirportOfDestination().getCity() + ", " + flight.getAirportOfDestination().getCountry() + ")");
                flightST.put(date, null);
            }
        }
        // removes it from the symbol graph
        if (!remove.contains(airport.getCode())) {
            remove.add(airport.getCode());
        }
        symbolGraph = new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";", remove);
        airportST.put(airport.getCode(), null);
        log("AirportST", "Removed airport \"" + airport.getName() + "\"");
        Utils.getInstance().createCoordinatesFile();
    }

    /* AIRLINES */

    public SeparateChainingHashST<String, Airline> getAirlines() {
        return airlineST;
    }

    public static void setAirlines(SeparateChainingHashST<String, Airline> airlines) {
        airlineST = airlines;
    }

    public void newAirline(Airline airline) {
        if (airlineST.get(airline.getName()) == null) // there's no airline with that name, if there is, it can't be edited here
            airlineST.put(airline.getName(), airline);
        log("airlineST", "New airline \"" + airline.getName() + "\" from " + airline.getNationality());
    }

    /**
     * Receives the data possible to be edited on an airline and changes it using the class setters
     *
     * @param oldName     previous name of the airline to match it in the ST
     * @param newName     new name of the airline
     * @param nationality airline nationality
     */
    public void editAirline(String oldName, String newName, String nationality) {
        if (oldName.compareTo(newName) == 0) { // same name (key), updates the entry
            airlineST.get(oldName).setNationality(nationality);
        } else { // otherwise the entry must be replaced
            Airline newAirline = new Airline(newName, nationality);
            newAirline.setFleet(airlineST.get(oldName).getFleet()); // copies the airline fleet before replacing it
            airlineST.put(oldName, null); // removes the previous key
            airlineST.put(newName, newAirline);
        }
        log("airlineST", "Edited airline \"" + oldName + "\" from " + nationality);
    }

    /**
     * Removes an airline including all its airplanes
     *
     * @param airline is the airline to remove
     */
    public static void removeAirline(Airline airline) {
        for (Integer p : airline.getFleet().keys()) {
            removeAirplane(airline.getFleet().get(p));
        }
        airlineST.put(airline.getName(), null);
        log("AirlineST", "Removed airline \"" + airline.getName() + "\"");
    }

    /* AIRPLANES */

    /**
     * @return returns the airplanes symbol table
     */
    public RedBlackBST<Integer, Airplane> getAirplanes() {
        return airplaneST;
    }

    public static void setAirplanes(RedBlackBST<Integer, Airplane> airplanes) {
        airplaneST = airplanes;
    }

    /**
     * Receives all the data needed to create a new airplane and creates it
     * Checking first if the airport and the airline connected to it are real
     *
     * @param model              model of the airplane
     * @param name               name of the airplane
     * @param airlineName        airline that owns the airplane
     * @param cruiseSpeed        cruise speed of the airplane
     * @param cruiseAltitude     cruise altitude of the airplane
     * @param maxRange           max range of the airplane
     * @param airportCode        airport where the airplane will be parked at first
     * @param passengersCapacity passengers capacity of the airplane
     * @param fuelCapacity       fuel capacity of the airplane
     * @return returns false if the airline or the airport in question does not exist
     */
    public boolean addAirplane(String model, String name, String airlineName, float cruiseSpeed, float cruiseAltitude,
                               float maxRange, String airportCode, int passengersCapacity, int fuelCapacity) {
        int id = 1; // if the airplanes ST is empty this will be the first entry
        if (!airplaneST.isEmpty())
            id = airplaneST.max() + 2; // adds 2 because 1 is for the id to match airplanes ids and another to add the new plane

        // searches for the airline existence
        Airline thisPlaneAirline = airlineST.get(airlineName);
        if (thisPlaneAirline == null)
            return false;

        // searches for the airport existence
        Airport thisPlaneAirport = airportST.get(airportCode);
        if (thisPlaneAirport == null)
            return false;

        Airplane newPlane = new Airplane(id, model, name, cruiseSpeed, cruiseAltitude, maxRange, airportCode,
                passengersCapacity, fuelCapacity, thisPlaneAirline);
        airportST.get(airportCode).receivePlane(newPlane);  // adds this new plane to the respective airport
        thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
        airplaneST.put(id - 1, newPlane); // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
        log("airplaneST", "Inserted airplane \"" + newPlane.getName() + "\"");
        return true;
    }

    /**
     * Receives the data possible to be edited on an airplane and changes it using the class setters
     *
     * @param idAirplane         id of the airplane to edit
     * @param model              new model of the airplane
     * @param name               new name of the airplane
     * @param cruiseSpeed        new cruise speed of the airplane
     * @param cruiseAltitude     new cruise altitude of the airplane
     * @param maxRange           new max range of the airplane
     * @param passengersCapacity new passengers capacity of the airplane
     * @param fuelCapacity       new fuel capacity of the airplane
     */
    public void editAirplane(int idAirplane, String model, String name, float cruiseSpeed, float cruiseAltitude,
                             float maxRange, int passengersCapacity, int fuelCapacity) {
        idAirplane -= 1; // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
        // searches for the airplane existence
        Airplane plane = airplaneST.get(idAirplane);
        plane.setModel(model);
        plane.setName(name);
        plane.setCruiseSpeed(cruiseSpeed);
        plane.setCruiseAltitude(cruiseAltitude);
        plane.setMaxRange(maxRange);
        plane.setPassengersCapacity(passengersCapacity);
        plane.setFuelCapacity(fuelCapacity);
        log("airplaneST", "Edited airplane [" + plane.getId() + "] \"" + plane.getName() + "\"");
    }

    /**
     * Removes an airplane from the database (respective symbol table, airline and the current airport where it is parked)
     *
     * @param plane is the plane to remove
     */
    public static void removeAirplane(Airplane plane) {
        Airline airline = plane.getAirline();
        airline.removePlane(plane);
        airportST.get(plane.getAirportCode()).sendPlane(plane); // goes to the airport where the plane is parked to remove it
        airplaneST.put(plane.getId() - 1, null); // ids on the ST starts from 0 and airplanes ids from 1
        log("Airline \"" + airline.getName() + "\"", "Removed airplane \"" + plane.getName() + "\"");
        log("AirplaneST", "Removed airplane \"" + plane.getName() + "\"");
        log("Airport \"" + airportST.get(plane.getAirportCode()) + "\"", "Removed airplane \"" + plane.getName() + "\"");
    }

    public RedBlackBST<Date, Flight> getFlights() {
        return flightST;
    }

    public static void setFlights(RedBlackBST<Date, Flight> flights) {
        flightST = flights;
    }

    public void createCoordinatesFile() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        for (String code : airportST.keys()) {
            Airport airport = airportST.get(code);
            coordinates.add(new Coordinates(code, airport.getLongitude(), airport.getLatitude()));
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(".//data//coordinates.bin"))) {
            oos.writeObject(coordinates);
            oos.close();
        } catch (Exception ex) {
            System.out.println("Couldn't create the file");
        }
    }

//    /**
//     * searches airports of a certain continent
//     *
//     * @param airportST Symbol table that stores all the available airports
//     * @param search    the continent to search for
//     * @return returns all the airports that matched the search
//     */
//    public static ArrayList<Airport> searchAirportsOfContinent(SeparateChainingHashST<String, Airport> airportST, String search) {
//        ArrayList<Airport> airportSearch = new ArrayList<>();
//        search = search.toLowerCase();
//        for (String code : airportST.keys()) {
//            if (airportST.get(code).getContinent().toLowerCase().compareTo(search) == 0) {
//                airportSearch.add(airportSearch.size(), airportST.get(code));
//            }
//        }
//        return airportSearch;
//    }

    /**
     * Creates a copy of the current program so the next time the program runs there will be an option of loading the previous program
     * or create a new one from the data imported from the files and without flights
     */
    public static void dump(String path) {
        if (path.length() != 0 && path.contains("bin")) { // store on binary file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeObject(airportST);
                oos.writeObject(airlineST);
                oos.writeObject(airplaneST);
                oos.writeObject(flightST);
                oos.writeObject(symbolGraph);
                oos.flush();
                oos.close();
            } catch (Exception ex) {
                System.out.println("Couldn't create the file");
            }
        }else if(path.length() != 0) { // store on text file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) { // FileWriter with only one parameter will overwrite the file content each time that is what we want
                bw.write("#");
                bw.newLine();
                bw.write("airportName;airportCode;city;country;continent;rating;");
                bw.newLine();
                for (String a : airportST.keys()) {
                    Airport airport = airportST.get(a);
                    bw.write(
                            airport.getName() + ";" +
                                    airport.getCode() + ";" +
                                    airport.getCity() + ";" +
                                    airport.getCountry() + ";" +
                                    airport.getContinent() + ";" +
                                    airport.getRating() + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                bw.write("name;nationality;");
                bw.newLine();
                for (String al : airlineST.keys()) {
                    Airline airline = airlineST.get(al);
                    bw.write(
                            airline.getName() + ";" +
                                    airline.getNationality() + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                bw.write("planeId;model;name;airline;cruiseSpeed;cruiseAltitude;maxRange;airportCode;" +
                        "passengers;fuelCap");
                bw.newLine();
                for (Integer ap : airplaneST.keys()) {
                    Airplane airplane = airplaneST.get(ap);
                    bw.write(
                            airplane.getId() + ";" +
                                    airplane.getModel() + ";" +
                                    airplane.getName() + ";" +
                                    airplane.getAirline().getName() + ";" +
                                    airplane.getCruiseSpeed() + ";" +
                                    airplane.getCruiseAltitude() + ";" +
                                    airplane.getMaxRange() + ";" +
                                    airplane.getAirportCode() + ";" +
                                    airplane.getPassengersCapacity() + ";" +
                                    airplane.getFuelCapacity() + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                bw.write("date;passengers;plane;originAirport;destinAirport;connections;");
                bw.newLine();
                for (Date d : flightST.keys()) {
                    Flight flight = flightST.get(d);
                    StringBuilder sb = new StringBuilder();
                    for (String code : flight.getConnections()) {
                        sb.append(code);
                        sb.append("|");
                    }
                    bw.write(
                            flight.getDate().getSlashes() + ";" +
                                    flight.getPassengers() + ";" +
                                    flight.getAirplane().getId() + ";" +
                                    flight.getAirportOfOrigin().getCode() + ";" +
                                    flight.getAirportOfDestination().getCode() + ";" +
                                    sb + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                // save symbol graph
                bw.write("originAirport;airportCode;distance;windSpeed;airTunnel;");
                bw.newLine();
                bw.write("3");
                bw.newLine();
                for (int i = 0; i < symbolGraph.digraph().V(); i++) {
                    bw.write(symbolGraph.nameOf(i));
                    for (Connection e : symbolGraph.digraph().adj(i)) {
                        bw.write(";" + symbolGraph.nameOf(e.to()) + ";" + e.weight() + ";" + Math.round(e.getWindSpeed() * 100) / 100f + ";" + e.getAltitude());
                    }
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{ // backup save, both binary and txt
            String binPath = ".//data//backup.bin";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binPath))) {
                oos.writeObject(airportST);
                oos.writeObject(airlineST);
                oos.writeObject(airplaneST);
                oos.writeObject(flightST);
                oos.writeObject(symbolGraph);
                oos.flush();
                oos.close();
            } catch (Exception ex) {
                System.out.println("Couldn't create the file");
            }
            path = ".//data//backup.txt";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) { // FileWriter with only one parameter will overwrite the file content each time that is what we want
                bw.write("#");
                bw.newLine();
                bw.write("airportName;airportCode;city;country;continent;rating;");
                bw.newLine();
                for (String a : airportST.keys()) {
                    Airport airport = airportST.get(a);
                    bw.write(
                            airport.getName() + ";" +
                                    airport.getCode() + ";" +
                                    airport.getCity() + ";" +
                                    airport.getCountry() + ";" +
                                    airport.getContinent() + ";" +
                                    airport.getRating() + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                bw.write("name;nationality;");
                bw.newLine();
                for (String al : airlineST.keys()) {
                    Airline airline = airlineST.get(al);
                    bw.write(
                            airline.getName() + ";" +
                                    airline.getNationality() + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                bw.write("planeId;model;name;airline;cruiseSpeed;cruiseAltitude;maxRange;airportCode;" +
                        "passengers;fuelCap");
                bw.newLine();
                for (Integer ap : airplaneST.keys()) {
                    Airplane airplane = airplaneST.get(ap);
                    bw.write(
                            airplane.getId() + ";" +
                                    airplane.getModel() + ";" +
                                    airplane.getName() + ";" +
                                    airplane.getAirline().getName() + ";" +
                                    airplane.getCruiseSpeed() + ";" +
                                    airplane.getCruiseAltitude() + ";" +
                                    airplane.getMaxRange() + ";" +
                                    airplane.getAirportCode() + ";" +
                                    airplane.getPassengersCapacity() + ";" +
                                    airplane.getFuelCapacity() + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                bw.write("date;passengers;plane;originAirport;destinAirport;connections;");
                bw.newLine();
                for (Date d : flightST.keys()) {
                    Flight flight = flightST.get(d);
                    StringBuilder sb = new StringBuilder();
                    for (String code : flight.getConnections()) {
                        sb.append(code);
                        sb.append("|");
                    }
                    bw.write(
                            flight.getDate().getSlashes() + ";" +
                                    flight.getPassengers() + ";" +
                                    flight.getAirplane().getId() + ";" +
                                    flight.getAirportOfOrigin().getCode() + ";" +
                                    flight.getAirportOfDestination().getCode() + ";" +
                                    sb + ";"
                    );
                    bw.newLine();
                }
                bw.write("#");
                bw.newLine();
                // save symbol graph
                bw.write("originAirport;airportCode;distance;windSpeed;airTunnel;");
                bw.newLine();
                bw.write("3");
                bw.newLine();
                for (int i = 0; i < symbolGraph.digraph().V(); i++) {
                    bw.write(symbolGraph.nameOf(i));
                    for (Connection e : symbolGraph.digraph().adj(i)) {
                        bw.write(";" + symbolGraph.nameOf(e.to()) + ";" + e.weight() + ";" + Math.round(e.getWindSpeed() * 100) / 100f + ";" + e.getAltitude());
                    }
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stores actions like insertions of airports, airlines and airplanes, editings and removals in a log file
     *
     * @param from       tells from wich class the action was made
     * @param changeMade is the action made
     */
    public static void log(String from, String changeMade) {
        String path = ".//data//logs.txt";
        if (from.contentEquals("reset")) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
                bw.write("");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) { // passing the boolean true for appending and not rewriting the file
                bw.write(timeStamp + " # From " + from + ": " + changeMade);
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dump(""); // saves a backup of the current program when the user does some change
        }
    }

    /**
     * Utility function to check for a search input to be numeric or not
     *
     * @param str -> search input
     * @return returns true if it is a number and false if not
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            Integer.valueOf(str);
        } catch (NumberFormatException nfe) {
            try {
                Float.parseFloat(str);
                Float.valueOf(str);
            } catch (NumberFormatException nfe2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Draw in the new GUI interface the Graph used in project
     *
     */
    public static void showGraphs() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = new SingleGraph("SymbolGraph");
        for (int i = 0; i < symbolGraph.digraph().V(); i++) {
            if(symbolGraph.digraph().adj(i).iterator().hasNext()){
                for (Connection c : symbolGraph.digraph().adj(i)) {
                    try {
                        graph.addNode(symbolGraph.nameOf(c.from()));
                    } catch (Exception e) {
                        // ignore
                    }
                    try {
                        graph.addNode(symbolGraph.nameOf(c.to()));
                    } catch (Exception e) {
                        // ignore
                    }
                    try {
                        graph.addEdge(symbolGraph.nameOf(c.from()) + "-" + symbolGraph.nameOf(c.to()), symbolGraph.nameOf(c.from()), symbolGraph.nameOf(c.to()), true);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }else{
                try {
                    graph.addNode(symbolGraph.nameOf(i));
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
            node.setAttribute("ui.class", "marked");
        }
        String styleSheet =
                "node {" +
                        "   fill-color: black;" +
                        "}" +
                        "node.marked {" +
                        "	fill-color: #4185d1;" +
                        "   padding: 15px; " +
                        "   text-color: white; " +
                        "   text-style: bold;" +
                        "   text-font: Helvetica;" +
                        "   text-size: 13px;" +
                        "   size: 40px; " +
                        "}" +
                        "edge {" +
                        "   fill-color:#4185d1;" +
                        "   padding: 4px;" +
                        "}";
        graph.addAttribute("ui.stylesheet", styleSheet);
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }

    /**
     * All the flights between a period of time
     *
     * @param start starting date
     * @param end   ending date
     */
    public static RedBlackBST<Date, Flight> flightsBetweenTimes(Date start, Date end) {
        RedBlackBST<Date, Flight> results = new RedBlackBST<>();
        for (Date date : flightST.keys()) {
            Flight flight = flightST.get(date);
            if ((flight.getDate().compareTo(start) == 1 || flight.getDate().compareTo(start) == 0) &&
                    (flight.getDate().compareTo(end) == -1 || flight.getDate().compareTo(end) == 0)) {
                results.put(date, flight);
            }
        }
        return results;
    }

    // prints shortest path, cheaper one, and fastest one
//    public void printShortestPath(DijkstraSP dijkstraSP, SymbolEdgeWeightedDigraph symbolGraph, int airportOfDestination, Airplane airplane, String typeOfSearch) {
//        for (Connection e : dijkstraSP.pathTo(airportOfDestination)) {
//            if (typeOfSearch.compareTo("distance") == 0) {
//                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : " + e.weight() + " Km");
//            } else if (typeOfSearch.compareTo("monetary") == 0) {
//                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : " + airplane.getAirplaneCost(e) + " €");
////                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : " + euroValue * (double) Math.round(airplane.getAirplaneCost(e) * 100) / 100f + " €");
//            } else if (typeOfSearch.compareTo("time") == 0) {
//                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : ");
//                System.out.println(Date.convertTime(airplane.getFlightDuration(e)));
//            }
//            System.out.println();
//        }
//        if (typeOfSearch.compareTo("time") == 0) {
//            System.out.print("Total Cost: ");
//            System.out.println(Date.convertTime(dijkstraSP.distTo(airportOfDestination)));
//        } else if (typeOfSearch.compareTo("distance") == 0) {
//            System.out.println("Total Cost: " + dijkstraSP.distTo(airportOfDestination) + " km");
//        } else if (typeOfSearch.compareTo("monetary") == 0) {
//            System.out.println("Total Cost: " + dijkstraSP.distTo(airportOfDestination) + " €");
////            System.out.println("Total Cost: " + euroValue * (double) Math.round(dijkstraSP.distTo(airportOfDestination) * 100) / 100f + " €");
//        }
//        System.out.println();
//    }

    // prints fastest path (less connections)
//    public void printAShortestPath(BreadthFirstPaths bfs, SymbolEdgeWeightedDigraph symbolGraph, int airportOfDestination) {
//        if (bfs.hasPathTo(airportOfDestination)) {
//            // with airport positions on the symbol graph
//            for (int x : bfs.pathTo(airportOfDestination)) {
//                if (x == airportOfDestination) System.out.print(x);
//                else System.out.print(x + " - ");
//            }
//            System.out.println();
//            // with airport codes
//            for (int x : bfs.pathTo(airportOfDestination)) {
//                if (x == airportOfDestination) System.out.print(symbolGraph.nameOf(x));
//                else System.out.print(symbolGraph.nameOf(x) + " - ");
//            }
//        }
//        System.out.println("\n");
//    }


    /**
     * check if the graph is connected
     *
     * @param graph - graph will be checked
     */
    public void checkGraphIsConnected(EdgeWeightedDigraph graph) {
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(graph);
        if (kosarajuSharirSCC.count() == 1) {
            System.out.println("Graph is connected!");
        } else {
            System.out.println("Graph is not connected!");
        }
    }

    /**
     * Creates a new flight between two airports
     *
     * @param bfs - Breadth First Search object
     * @param dijkstraSP - dijkstraSP object
     * @param date - date of flight
     * @param passengers - passengers of flight
     * @param airplane - airplane used in the flight
     * @param airportOfOrigin - airport of origin the flight
     * @param airportOfDestination - airport of Destination the flight
     * @param gIdAirportDest - id will be used for search the hasPathTo in bfs and dijkstraSP algorithm
     */
    public void newFlight(BreadthFirstPaths bfs, DijkstraSP dijkstraSP, Date date, int passengers, Airplane airplane,
                          Airport airportOfOrigin, Airport airportOfDestination, int gIdAirportDest, ArrayList<String> cons) {

        double distance = 0, cost = 0, timeDuration = 0;
        int comp = 0;
        Flight newFlight = new Flight(date, passengers, airplane, airportOfOrigin, airportOfDestination);
        if (dijkstraSP != null && dijkstraSP.hasPathTo(gIdAirportDest)) {
            for (Connection e : dijkstraSP.pathTo(gIdAirportDest)) {
                newFlight.setConnection(symbolGraph.nameOf(e.from()));
                if (symbolGraph.nameOf(e.from()).compareTo(airportOfOrigin.getCode()) != 0) {
                    getAirports().get(symbolGraph.nameOf(e.from())).newFlight(newFlight);
                }
            }
            newFlight.setConnection(symbolGraph.nameOf(gIdAirportDest));
        } else if (bfs != null && bfs.hasPathTo(gIdAirportDest)) {
            for (int x : bfs.pathTo(gIdAirportDest)) {
                newFlight.setConnection(symbolGraph.nameOf(x));
                if (symbolGraph.nameOf(x).compareTo(airportOfOrigin.getCode()) != 0 && symbolGraph.nameOf(x).compareTo(airportOfDestination.getCode()) != 0) {
                    getAirports().get(symbolGraph.nameOf(x)).newFlight(newFlight);
                }
            }
        } else if (!cons.isEmpty()) {
            newFlight.setConnections(cons);
        }

        // get from all the flight connections, all the info of the weights needed
        for (String code : newFlight.getConnections()) {
            for (Connection e : symbolGraph.digraph().adj(symbolGraph.indexOf(code))) {
                if (comp + 1 >= newFlight.getConnections().size()) {
                } else if (symbolGraph.nameOf(e.to()).compareTo(newFlight.getConnections().get(comp + 1)) == 0) {
                    distance += e.weight();
                    cost += airplane.getAirplaneCost(e);
                    timeDuration += airplane.getFlightDuration(e);
                }
            }
            comp++;
        }

        // set total values for the flight
        Date duration = Date.convertTimeToDate(timeDuration);
        newFlight.setDuration(duration);
        newFlight.setCosts(cost);
        newFlight.setDistance(distance);

        flightST.put(newFlight.getDate(), newFlight);
        log("flightST", "New Flight from \"" + newFlight.getAirportOfOrigin().getName() + "\" (" + newFlight.getAirportOfOrigin().getCity() + ", " +
                newFlight.getAirportOfOrigin().getCountry() + ") to \"" + newFlight.getAirportOfDestination().getName() + "\" (" +
                newFlight.getAirportOfDestination().getCity() + ", " + newFlight.getAirportOfDestination().getCountry() + ")");
    }


    /**
     * Search the airports that have the number of conections
     *
     * @param number - number of connections of airport
     * @return - Return SeparateChainingHashST of airports that have number connections
     */
    public SeparateChainingHashST<String, Airport> airportWithConnections(int number) {
        SeparateChainingHashST<String, Airport> results = new SeparateChainingHashST<>();
        for (String key : airportST.keys()) {
            Airport airport = airportST.get(key);
            if (airportConnections(airport).size() == number) {
                results.put(key, airport);
            }
        }
        return results;
    }

    /**
     * Search the edges/connections of determinate airport
     *
     * @param airport - Airport for search
     * @return Arraylist of Strings with name of airports connections
     */
    public ArrayList<String> airportConnections(Airport airport) {
        ArrayList<String> results = new ArrayList<>();
        for (Connection e : symbolGraph.digraph().adj(symbolGraph.indexOf(airport.getCode()))) {
            results.add(symbolGraph.nameOf(e.to()));
        }
        return results;
    }

    /***
     *  function responsible for cloning the arrayList
     *
     * @param list -> array for the cloning
     * @return An arraylist cloned
     */
//    //    criar um novo grafo perante um continente escolhido
//    public EdgeWeightedDigraph filterGraph(String searchContinent) {
//        EdgeWeightedDigraph newGraph = new EdgeWeightedDigraph(searchAirportsOfContinent(airportST, searchContinent).size());
////        criar tabela de simbolos para mapear os codigos originais da symbolDiagraph nos novos
//        ST<Integer, Integer> map = new ST<>();
//        int i = 0;
////        impirmir lista de adjacencias com filtro
//        for (String code : airportST.keys()) {
//            if (airportST.get(code).getContinent().toLowerCase().compareTo(searchContinent) == 0) {
//                for (Connection e : symbolGraph.digraph().adj(symbolGraph.indexOf(code))) {
//                    map.put(i, symbolGraph.indexOf(code));
//                }
//                i++;
//            }
//        }
//        for (Integer inte : map.keys()) {
////            System.out.println("[" + inte + "]   " + map.get(inte));
//        }
////        System.out.println();
//        return newGraph;
//    }


    /***
     *  function responsible for cloning the arrayList
     *
     * @param list -> array for the cloning
     * @return An arraylist cloned
     */
    public static ArrayList<String> cloneList(ArrayList<String> list) {
        ArrayList<String> clone = new ArrayList<String>(list.size());
        clone.addAll(list);
        return clone;
    }


    /***
     *  Search in the airportST for the airports that are NOT from the continent passed
     *  Returns a list of airports to ignore when creating a graph with only the airports from a continent
     *  Uses a "mainGraph" that is the one that holds all the airports without filters
     * @param continent -> continent for search
     * @return - An Arraylist of airports of the continent different of continent param
     */
   public static ArrayList<String> filterAirportsByContinent(String continent) {
        ArrayList<String> filter = new ArrayList<>();
        for (int i = 0; i < mainGraph.digraph().V(); i++) {
            if (airportST.get(mainGraph.nameOf(i)).getContinent().toUpperCase().compareTo(continent.toUpperCase()) != 0) {
                filter.add(mainGraph.nameOf(i));
            }
        }
        return filter;
    }

    /* STATICtistics */

    /**
     * Determines the airports with the most traffic (number of flights)
     *
     * @return returns the list with all the matches for the most traffic airport, can be more than one with the same ammount
     */
    public static List<Map.Entry<String, Integer>> mostTrafficAirport() {
        Map<String, Integer> airports = new HashMap<>();
        for (String code : airportST.keys()) {
            airports.put(code, airportST.get(code).getFlights().size());
        }
        Set<Map.Entry<String, Integer>> set = airports.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return list.subList(0, 5);
    }

    /**
     * Determines the flights with the most passengers transported
     *
     * @return returns the list with all the matches for the flight that transported most passengers, can be more than one
     */
    public static List<Map.Entry<String, Integer>> mostPassengersFlight() {
        Map<String, Integer> flights = new HashMap<>();
        for (Date d : flightST.keys()) {
            flights.put(d.minifyDate(), flightST.get(d).getPassengers());
        }
        Set<Map.Entry<String, Integer>> set = flights.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int size = list.size() < 5 ? list.size() : 5;
        return list.subList(0, size);
    }

    /**
     * Determines the airports wich had the most passengers passing through it
     *
     * @return returns the arraylist with all the matches for the most passengers passed through, can be more than one if the same ammount
     */
    public static List<Map.Entry<String, Integer>> mostPassengersAirport() {
        Map<String, Integer> airports = new HashMap<>();
        RedBlackBST<Date, Flight> airportFlights;
        int nPassengers;
        for (String code : airportST.keys()) {
            nPassengers = 0;
            airportFlights = airportST.get(code).getFlights();
            for (Date d : airportFlights.keys()) {
                nPassengers += airportFlights.get(d).getPassengers();
            }
            airports.put(code, nPassengers);
        }
        Set<Map.Entry<String, Integer>> set = airports.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return list.subList(0, 5);
    }


    /***
     *  function responsible for removing an edge of graph
     *
     * @param originCode -  code of origin Airport
     * @param destinationCode - code of destination Airport
     */
    public static void removeConnectionOfAirport(String originCode, String destinationCode) {
        for (Connection e : symbolGraph.digraph().adj(symbolGraph.indexOf(originCode))) {
            if (e.to() == symbolGraph.indexOf(destinationCode)) {
                symbolGraph.digraph().removeEdge(e);
                log("SymbolGraph", "Removed connection between " + originCode + " and " + destinationCode);
            }

        }
    }
}
