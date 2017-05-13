package airportsProject;

import airportsProject.Exceptions.WrongTypeFileException;
import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.DirectedEdge;
import libs.*;
import libs.BreadthFirstPaths;
import libs.DijkstraSP;
import libs.EdgeWeightedDigraph;
import libs.KosarajuSharirSCC;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Utils {
    public static int nValue = 200;
    public static int mValue = 250;
    public static int euroValue = 30;  // 30 €/L
    public static int windCost = 10;   // aditional cost by km/h of wind (adds 10L per km/h to the airplane fuel cost if it is against the airplane, or substracts if in favor)
    public static int mapWidth = 1536; // width of the world map used
    public static int mapHeight = 768; // height of the world map used

    private static Utils instance = null;

    // paths to default data
    private static final String pathAirports = ".//data//airports.txt";
    private static final String pathAirplanes = ".//data//airplanes.txt";
    private static final String pathAirlines = ".//data//airlines.txt";

    // Symbol Tables
    private static SeparateChainingHashST<String, Airport> airportST = new SeparateChainingHashST<>();
    private static SeparateChainingHashST<String, Airline> airlinesST = new SeparateChainingHashST<>();
    private static RedBlackBST<Integer, Airplane> airplaneST = new RedBlackBST<>();
    private static RedBlackBST<Date, Flight> flightST = new RedBlackBST<>();
    private static SymbolEdgeWeightedDigraph symbolGraph = new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";");

    protected Utils(){
        // prevents instantiation
    }

    public static Utils getInstance(){
        if(instance == null){
            instance = new Utils();
        }
        return instance;
    }

    public SymbolEdgeWeightedDigraph getSymbolGraph() {
        return symbolGraph;
    }

    /**
     * Will fill the symbol tables with the data required
     *
     * @param path -> will be empty if the user chooses to create a new program and will bring a file path if
     *             he chooses to load a program
     */
    public static boolean initProgram(String path) {
        if (path.length() > 0) { // load program
            try {
                ImportFromFile.loadProgram(path, airportST, airlinesST, airplaneST, flightST);
                File file = new File(path);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                System.out.println("Last opened in: " + sdf.format(file.lastModified()));
            } catch (WrongTypeFileException wt) {
                System.out.println(wt.getMessage());
                return false;
            }
        } else { // new program
            log("reset", ""); // clean the log file from the previous program
            ImportFromFile.importAirports(airportST, pathAirports);
            ImportFromFile.importAirlines(airlinesST, pathAirlines);
            ImportFromFile.importPlanes(airportST, airplaneST, airlinesST, pathAirplanes);
        }
        /*
                TO REMOVE !!!!
         */
        Flight flight1 = new Flight(10000, new Date(0,0,0,3,20,10), new Date(1,2,2015,0,0,0), 100, airplaneST.get(0), airportST.get("OPO"), airportST.get("NRT"));
        Flight flight2 = new Flight(10000, new Date(0,0,0,7,20,10), new Date(2,4,2017,0,0,0), 100, airplaneST.get(5), airportST.get("ALG"), airportST.get("OPO"));
        Flight flight3 = new Flight(10000, new Date(0,0,0,9,20,0), new Date(12,11,2016,16,5,0), 100, airplaneST.get(9), airportST.get("NRT"), airportST.get("ALG"));
        Flight flight4 = new Flight(10000, new Date(0,0,0,11,20,0), new Date(2,11,2016,16,5,0), 150, airplaneST.get(0), airportST.get("NRT"), airportST.get("ALG"));
        Flight flight5 = new Flight(10000, new Date(0,0,0,2,20,10), new Date(2,4,2017,4,0,0), 100, airplaneST.get(5), airportST.get("LIS"), airportST.get("OPO"));
        flightST.put(flight1.getDate(), flight1);
        flightST.put(flight2.getDate(), flight2);
        flightST.put(flight3.getDate(), flight3);
        flightST.put(flight4.getDate(), flight4);
        flightST.put(flight5.getDate(), flight5);
        return true;
    }

    /* AIRPORTS */

    public SeparateChainingHashST<String, Airport> getAirports() {
        return airportST;
    }

    public void newAirport(Airport airport) {
        airportST.put(airport.getCode(), airport);
    }

    /**
     * Receives the data possible to be edited on an airport and changes it using the class setters
     *
     * @param code   code of the airport to edit
     * @param name   new name of the airport
     * @param rating new rating of the airport
     */
    public void editAirport(String code, String name, Float rating){
        airportST.get(code).setName(name);
        airportST.get(code).setRating(rating);
        log("airportST", "Edited airport [" +  airportST.get(code).getCode() +  "] \" Name:" + airportST.get(code).getName() + "\" Rating:" +
                airportST.get(code).getRating());
    }

    /**
     * Removes an airport incluiding all the airplanes parked there
     * @param airport airport to remove
     */
    public static void removeAirport(Airport airport){
        if(!airport.getAirplanes().isEmpty()){
            for (Integer p : airportST.get(airport.getCode()).getAirplanes().keys() ) {
                removeAirplane(airportST.get(airport.getCode()).getAirplanes().get(p));
            }
        }
        log("AirportST","Removed airport \"" + airportST.get(airport.getCode()).getName() + "\"");
        airportST.put(airport.getCode(), null);
        Utils.getInstance().createCoordinatesFile();
    }

    /* AIRLINES */

    public SeparateChainingHashST<String, Airline> getAirlines(){
        return airlinesST;
    }

    public void newAirline(Airline airline){
        if(airlinesST.get(airline.getName()) == null) // there's no airline with that name, if there is, it can't be edited here
            airlinesST.put(airline.getName(), airline);
    }

    /**
     * Receives the data possible to be edited on an airline and changes it using the class setters
     * @param oldName previous name of the airline to match it in the ST
     * @param newName new name of the airline
     * @param nationality airline nationality
     */
    public void editAirline(String oldName, String newName, String nationality){
        if(oldName.compareTo(newName) == 0){ // same name (key), updates the entry
            airlinesST.get(oldName).setNationality(nationality);
        }else{ // otherwise the entry must be replaced
            Airline newAirline = new Airline(newName, nationality);
            newAirline.setFleet(airlinesST.get(oldName).getFleet()); // copies the airline fleet before replacing it
            airlinesST.put(oldName, null); // removes the previous key
            airlinesST.put(newName, newAirline);
        }
        log("airlineST", "Edited airline \"" + oldName + "\" from " + nationality);
    }

    /**
     * Removes an airline including all its airplanes
     * @param airline is the airline to remove
     */
    public static void removeAirline(Airline airline){
        for (Integer p: airline.getFleet().keys()) {
            removeAirplane(airline.getFleet().get(p));
        }
        log("AirlineST","Removed airline \"" + airline.getName() + "\"");
        airlinesST.put(airline.getName(), null);
    }

    /* AIRPLANES */

    /**
     *
     * @return returns the airplanes symbol table
     */
    public RedBlackBST<Integer, Airplane> getAirplanes() {
        return airplaneST;
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
     * @param fuelCapacity fuel capacity of the airplane
     * @return returns false if the airline or the airport in question does not exist
     */
    public boolean addAirplane(String model, String name, String airlineName,  float cruiseSpeed, float cruiseAltitude,
                               float maxRange, String airportCode, int passengersCapacity, int fuelCapacity){
        int id = 1; // if the airplanes ST is empty this will be the first entry
        if(!airplaneST.isEmpty())
            id = airplaneST.max() + 2; // adds 2 because 1 is for the id to match airplanes ids and another to add the new plane

        // searches for the airline existence
        Airline thisPlaneAirline = airlinesST.get(airlineName);
        if(thisPlaneAirline == null)
            return false;

        // searches for the airport existence
        Airport thisPlaneAirport = airportST.get(airportCode);
        if(thisPlaneAirport == null)
            return false;

        Airplane newPlane = new Airplane(id, model, name, cruiseSpeed, cruiseAltitude, maxRange, airportCode,
                passengersCapacity, fuelCapacity, thisPlaneAirline);
        airportST.get(airportCode).receivePlane(newPlane);  // adds this new plane to the respective airport
        thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
        airplaneST.put(id-1, newPlane); // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
        log("airplaneST", "Inserted airplane \"" + newPlane.getName() + "\"");
        return true;
    }

    /**
     * Receives the data possible to be edited on an airplane and changes it using the class setters
     * @param idAirplane id of the airplane to edit
     * @param model new model of the airplane
     * @param name new name of the airplane
     * @param cruiseSpeed new cruise speed of the airplane
     * @param cruiseAltitude new cruise altitude of the airplane
     * @param maxRange new max range of the airplane
     * @param passengersCapacity new passengers capacity of the airplane
     * @param fuelCapacity new fuel capacity of the airplane
     */
    public void editAirplane(int idAirplane, String model, String name,  float cruiseSpeed, float cruiseAltitude,
                             float maxRange, int passengersCapacity, int fuelCapacity){
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
        log("airplaneST", "Edited airplane [" + plane.getId() +"] \"" + plane.getName() + "\"");
    }

    /**
     * Removes an airplane from the database (respective symbol table, airline and the current airport where it is parked)
     * @param plane is the plane to remove
     */
    public static void removeAirplane(Airplane plane){
        Airline airline = plane.getAirline();
        airline.removePlane(plane);
        airportST.get(plane.getAirportCode()).sendPlane(plane); // goes to the airport where the plane is parked to remove it
        airplaneST.put(plane.getId()-1, null); // ids on the ST starts from 0 and airplanes ids from 1
        log("Airline \"" + airline.getName() + "\"", "Removed airplane \"" + plane.getName() + "\"");
        log("AirplaneST", "Removed airplane \"" + plane.getName() + "\"");
        log("Airport \"" + airportST.get(plane.getAirportCode()) + "\"", "Removed airplane \"" + plane.getName() + "\"");
    }

    public RedBlackBST<Date, Flight> getFlights(){
        return flightST;
    }

    public void newFlight(Flight flight){
        flightST.put(flight.getDate(), flight);
    }

    public void createCoordinatesFile(){
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        for(String code : airportST.keys()){
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

    /**
     * Intermediate method for creating a flight, this will be done by an algorithm of the shortest path when we start
     * implementing Graphs.
     * @param distance distance to the destination
     * @param duration of the flight until the plane reaches the destination
     * @param date when the airplane will leave the origin airport
     * @param passengers number of passengers that will carry
     * @param airplane the airplane that will do the flight
     * @param airportOfOrigin from where the airplane will leave
     * @param airportOfDestination where the airplane will arrive
     * @param flightST ST of all the flights where will be stored the new one
     */
//    public static void newFlight(float distance, Date duration, Date date, int passengers, Airplane airplane,
//                                  Airport airportOfOrigin, Airport airportOfDestination, RedBlackBST<Date, Flight> flightST){
//
//        Flight newFlight = new Flight(distance, duration, date, passengers, airplane, airportOfOrigin, airportOfDestination);
//        flightST.put(newFlight.getDate(), newFlight);
//        log("flightST", "New flight leaving at:" + newFlight.getDate() +
//                "; duration: " + newFlight.getDuration().getDuration() +
//                "; from \"" + newFlight.getAirportOfOrigin().getName() +
//                " ( " + newFlight.getDistance() + "m) " +
//                "\"; to \"" + newFlight.getAirportOfDestination().getName() +
//                "\"; airplane: \"" + newFlight.getAirplane().getName() + "\"");
//    }

    /**
     * searches airports of a certain continent
     *
     * @param airportST Symbol table that stores all the available airports
     * @param search    the continent to search for
     * @return returns all the airports that matched the search
     */
    public static ArrayList<Airport> searchAirportsOfContinent(SeparateChainingHashST<String, Airport> airportST, String search) {
        ArrayList<Airport> airportSearch = new ArrayList<>();
        search = search.toLowerCase();
        for (String code : airportST.keys()) {
            if (airportST.get(code).getContinent().toLowerCase().compareTo(search) == 0) {
                airportSearch.add(airportSearch.size(), airportST.get(code));
            }
        }
        return airportSearch;
    }

    /**
     * Creates a copy of the current program so the next time the program runs there will be an option of loading the previous program
     * or create a new one from the data imported from the files and without flights
     */
    public static void dump(){
        String path = ".//data/backup.txt";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){ // FileWriter with onle one parameter will overwrite the file content each time that is what we want
            bw.write("nome_aeroporto;código_aeroporto;cidade;país;continente;classificação;");
            bw.newLine();
            for(String a : airportST.keys()){
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
            bw.write("nome;nacionalidade;");
            bw.newLine();
            for(String al : airlinesST.keys()){
                Airline airline = airlinesST.get(al);
                bw.write(
                        airline.getName() + ";" +
                        airline.getNationality() + ";"
                );
                bw.newLine();
            }
            bw.write("#");
            bw.newLine();
            bw.write("id_avião;modelo;nome;companhia_aérea;velocidade_cruzeiro;altitude_cruzeiro;distância_máxima;cod_aeroporto;" +
                    "capacidade_de_passageiros;capacidade_do_depósito");
            bw.newLine();
            for(Integer ap : airplaneST.keys()){
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
            bw.write("distancia;custo;duracao;data;passageiros;aviao;aeroportoOrigem;aeroportoDestino;");
            bw.newLine();
            for(Date d : flightST.keys()){
                Flight flight = flightST.get(d);
                bw.write(
                        flight.getDistance() + ";" +
                        flight.getCosts() + ";" +
                        flight.getDuration().getSlashes() + ";" +
                        flight.getDate().getSlashes() + ";" +
                        flight.getPassengers() + ";" +
                        flight.getAirplane().getId() + ";" +
                        flight.getAirportOfOrigin().getCode() + ";" +
                        flight.getAirportOfDestination().getCode() + ";"
                );
                bw.newLine();
            }
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores actions like insertions of airports, airlines and airplanes, editings and removals in a log file
     * @param from tells from wich class the action was made
     * @param changeMade is the action made
     */
    public static void log(String from, String changeMade){
        String path = ".//data//logs.txt";
        if(from.contentEquals("reset")){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
                bw.write("");
                bw.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))){ // passing the boolean true for appending and not rewriting the file
                bw.write(timeStamp + " # From " + from + ": " + changeMade);
                bw.newLine();
                bw.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Utility function to check for a search input to be numeric or not
     *
     * @param str -> search input
     * @return returns true if it is a number and false if not
     */
    public static boolean isNumeric(String str){
        try{
            double d = Integer.parseInt(str);
        }catch(NumberFormatException nfe) {
            try{
                double d = Float.parseFloat(str);
            }catch(NumberFormatException nfe2) {
                return false;
            }
        }
        return true;
    }

    public static void showGraphs(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        SymbolEdgeWeightedDigraph symbolGraph = new SymbolEdgeWeightedDigraph(".//data//graph.txt", ";");
        Graph graph = new SingleGraph("SymbolGraph");
        for (int i = 0; i < symbolGraph.digraph().V(); i++) {
            for(Connection c : symbolGraph.digraph().adj(i)){
                try{
                    graph.addNode(""+c.from());
                }catch (Exception e){
                    // ignore
                }
                try{
                    graph.addNode(""+c.to());
                }catch (Exception e){
                    // ignore
                }
                try{
                    graph.addEdge(""+c.from()+"-"+""+c.to(), ""+c.from(), ""+c.to(), false);
                }catch (Exception e){
                    // ignore
                }
            }
        }
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
            node.setAttribute("ui.class", "marked");
        }
        /*
        "fill-mode" ...
        "fill-color" ...
        "fill-image" ...
        "stroke-mode" ...
        "stroke-color" ...
        "stroke-width" ...
        "shadow-mode" ...
        "shadow-color" ...
        "shadow-width" ...
        "shadow-offset" ...
        "text-mode" ...
        "text-color" ...
        "text-style" ...
        "text-font" ...
        "text-size" ...
        "text-visibility-mode" ...
        "text-visibility" ...
        "text-background-mode" ...
        "text-background-color" ...
        "text-offset" ...
        "text-padding" ...
        "icon-mode" ...
        "icon" ...
        "padding" ...
        "z-index" ...
        "visibility-mode" ...
        "visibility" ...
        "shape" ...
        "size" ...
        "size-mode" ...
        "shape-points" ...
        "text-alignment" ...
        "jcomponent" ...
        "arrow-image" ...
        "arrow-size" ...
        "arrow-shape" ...
        "sprite-orientation" ...
        "canvas-color" ...
        */
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
                        "   text-size: 17px;" +
                        "   size: 20px; " +
                        "}" +
                "edge {" +
                        "   fill-color:#4185d1;" +
                        "   padding: 4px;" +
                        "}";
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.display();
    }

    /* STATICtistics */

    /**
     * Determines the airport (or more than one if the ammount is the same) with the most traffic (number of flights)
     * @param airportST Symbol table that stores all the available airports
     * @return returns the arraylist with all the matches for the most traffic airport, can be more than one with the same ammount
     */
    private static ArrayList<Airport> mostTrafficAirport(SeparateChainingHashST<String, Airport> airportST){
        ArrayList<Airport> airports = new ArrayList<>();
        int max = 0, current;
        for(String code : airportST.keys()){
            current = airportST.get(code).getFlights().size();
            if(current > max){ // sets the current airport as the one with the most traffic
                max = current;
                airports.clear();
                airports.add(0, airportST.get(code));
            }else if(current == max){ // if an airport has the same ammount of traffic than other, will add this new one to the array
                airports.add(airports.size(), airportST.get(code));
            }
        }
        System.out.println("# With " + max + " flights passed through #");
        return airports;
    }

    /**
     * Determines the flight (or more than one if the ammount is the same) with the most passengers transported
     * @param flightST Symbol table that stores all the flights
     * @return returns the arraylist with all the matches for the flight that transported most passengers, can be more than one
     */
    private static ArrayList<Flight> mostPassengersFlight(RedBlackBST<Date, Flight> flightST){
        ArrayList<Flight> flights = new ArrayList<>();
        int max = 0, current;
        for(Date d : flightST.keys()){
            current = flightST.get(d).getPassengers();
            if(current > max){ // sets the current flight as the one with the most passengers transported
                max = current;
                flights.clear();
                flights.add(0, flightST.get(d));
            }else if(current == max){ // if an flight has the same ammount of transported passengers than other, will add this new one to the array
                flights.add(flights.size(), flightST.get(d));
            }
        }
        return flights;
    }

    /**
     * Determines the airport (or more than one if the ammount is the same) wich had the most passengers passing through it
     * @param airportST Symbol table that stores all the available airports
     * @return returns the arraylist with all the matches for the most passengers passed through, can be more than one if the same ammount
     */
    private static ArrayList<Airport> mostPassengersAirport(SeparateChainingHashST<String, Airport> airportST){
        ArrayList<Airport> airports = new ArrayList<>();
        RedBlackBST<Date, Flight> airportFlights;
        int max = 0, nPassengers;
        for(String code : airportST.keys()){
            // ao inserir um voo num aeroporto ter atributo de passageiros e somar n passageiros do novo voo
            nPassengers = 0;
            airportFlights = airportST.get(code).getFlights();
            for(Date d : airportFlights.keys()){
                nPassengers += airportFlights.get(d).getPassengers();
            }
            if(nPassengers > max){ // sets the current airport as the one with the most passengers transported
                max = nPassengers;
                airports.clear();
                airports.add(0, airportST.get(code));
            }else if(nPassengers == max){ // if an airport has the same ammount of transported passengers than other, will add this new one to the array
                airports.add(airports.size(), airportST.get(code));
            }
        }
        System.out.println("# Passengers transported: " + max + " #");
        return airports;
    }


    public void convertTime(double finalBuildTime) {
        int hours = (int) finalBuildTime;
        int minutes = (int) (finalBuildTime * 60) % 60;
        int seconds = (int) (finalBuildTime * (60 * 60)) % 60;

        System.out.print(String.format("%sh %sm %ss ", hours, minutes, seconds));
    }

    public Date convertTimeToDate(double finalBuildTime) {
        int hours = (int) finalBuildTime;
        int minutes = (int) (finalBuildTime * 60) % 60;
        int seconds = (int) (finalBuildTime * (60 * 60)) % 60;

        return new Date(0, 0, 0, hours, minutes, seconds);
    }

    //    imprimir caminho mais barato, curto , monetario, rapido (tempo)
    public void printShortestPath(DijkstraSP dijkstraSP, SymbolEdgeWeightedDigraph symbolGraph, int airportOfDestination, Airplane airplane, String typeOfSearch) {
        for (Connection e : dijkstraSP.pathTo(airportOfDestination)) {

            if (typeOfSearch.compareTo("distance") == 0) {
                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : " + e.weight() + " Km");
            } else if (typeOfSearch.compareTo("monetary") == 0) {
                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : " + euroValue * (double) Math.round(airplane.getAirplaneCost(e) * 100) / 100f + " €");
            } else if (typeOfSearch.compareTo("time") == 0) {
                System.out.print("[" + e.from() + "] " + symbolGraph.nameOf(e.from()) + "-> " + "[" + e.to() + "] " + symbolGraph.nameOf(e.to()) + " : ");
                Utils.getInstance().convertTime(airplane.getFlightDuration(e));
            }
            System.out.println();
        }
        if (typeOfSearch.compareTo("time") == 0) {
            System.out.print("Total Cost: ");
            convertTime(dijkstraSP.distTo(airportOfDestination));
        } else if (typeOfSearch.compareTo("distance") == 0) {
            System.out.println("Total Cost: " + dijkstraSP.distTo(airportOfDestination) + " km");
        } else if (typeOfSearch.compareTo("monetary") == 0) {
            System.out.println("Total Cost: " + euroValue * (double) Math.round(dijkstraSP.distTo(airportOfDestination) * 100) / 100f + " €");
        }
        System.out.println();
    }

    //    imprimir caminho mais rapido (menos ligacoes)
    public void printAShortestPath(BreadthFirstPaths bfs, SymbolEdgeWeightedDigraph symbolGraph, int airportOfDestination) {
        if (bfs.hasPathTo(airportOfDestination)) {
            for (int x : bfs.pathTo(airportOfDestination)) {
                if (x == airportOfDestination) System.out.print(x);
                else System.out.print(x + " - ");
            }
            System.out.println();
//            com traducao de nomes
            for (int x : bfs.pathTo(airportOfDestination)) {
                if (x == airportOfDestination) System.out.print(symbolGraph.nameOf(x));
                else System.out.print(symbolGraph.nameOf(x) + " - ");
            }
        }
        System.out.println("\n");
    }

    //    verificar se grafo é conexo
    public void checkGraphIsConnected(EdgeWeightedDigraph graph) {
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(graph);
        if (kosarajuSharirSCC.count() == 1) {
            System.out.println("Grafo de ligações entre aeroportos é conexo! \n");
        } else {
            System.out.println("Grafo não é conexo! \n");
        }

    }

    //    criar voo
    public void newFlight(BreadthFirstPaths bfs, DijkstraSP dijkstraSP, Date date, int passengers, Airplane airplane,
                          Airport airportOfOrigin, Airport airportOfDestination, int gIdAirportDest, RedBlackBST<Date, Flight> flightST) {

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
        }

//        ir buscar pela conecoes as informacoes dos pessos pretendidos
        for (String code : newFlight.getConnections()) {
            for (Connection e : symbolGraph.G().adj(symbolGraph.indexOf(code))) {
                if (comp + 1 >= newFlight.getConnections().size()) {
                } else if (symbolGraph.nameOf(e.to()).compareTo(newFlight.getConnections().get(comp + 1)) == 0) {
                    distance += e.weight();
                    cost += euroValue * (double) Math.round(airplane.getAirplaneCost(e) * 100) / 100f;
                    timeDuration += airplane.getFlightDuration(e);
                }
            }
            comp++;
        }

//        definir valores totais da viagem
        Date duration = convertTimeToDate(timeDuration);
        newFlight.setDuration(duration);
        newFlight.setCosts(cost);
        newFlight.setDistance(distance);


        flightST.put(newFlight.getDate(), newFlight);
        log("flightST", "New flight leaving at:" + newFlight.getDate() +
                "; duration: " + newFlight.getDuration().getDuration() +
                "; from \"" + newFlight.getAirportOfOrigin().getName() +
                "\"; to \"" + newFlight.getAirportOfDestination().getName() +
                "\"; airplane: \"" + newFlight.getAirplane().getName() + "\"");

    }

//  Recebe um numero e retorna uma SeparateChainingHashST com os aeroportos que tem esse numero de ligacoes
    public SeparateChainingHashST<String, Airport> quantityOfConnections(int number){
        SeparateChainingHashST<String, Airport> results = new SeparateChainingHashST<>();
        for (String key: airportST.keys() ) {
            Airport airport = airportST.get(key);
            if(airportConnections(airport).size() == number){
                results.put(key,airport);
            }
        }
        return results;
    }

    //    Retorna as ligações aéreas que passam num determinado aeroporto
    public ArrayList<String> airportConnections(Airport airport) {
        ArrayList<String> results = new ArrayList<>();
        for (Connection e : symbolGraph.G().adj(symbolGraph.indexOf(airport.getCode()))) {
            results.add(symbolGraph.nameOf(e.to()));
        }
        return results;
    }

//    Retorna informação detalhada do tráfego do aeroporto num determinando período de tempo, retorna uma redblack de blick com o filtro do tempo de um determinado aeroporto
    public RedBlackBST<Date, Flight> flightsBetweenTimesOfAirport(Airport airport, Date start, Date end) {
        RedBlackBST<Date, Flight> results = new RedBlackBST<>();
        for (Date f : airport.getFlights().keys()) {
            Flight flight = airport.getFlights().get(f);
            if (flight.getDate().compareTo(start) == 1 && flight.getDate().compareTo(end) == -1 ) {
                results.put(flight.getDate(),flight);
            }
        }
        return results;
    }


    //    criar um novo grafo perante
    public EdgeWeightedDigraph filterGraph(String searchContinent) {
        EdgeWeightedDigraph newGraph = new EdgeWeightedDigraph(searchAirportsOfContinent(airportST, searchContinent).size());
//        criar tabela de simbolos para mapear os codigos originais da symbolDiagraph nos novos
        ST<Integer, Integer> map = new ST<>();
        int i = 0;
        System.out.println(symbolGraph.G());
//        impirmir lista de adjacencias com filtro
        for (String code : airportST.keys()) {
            if (airportST.get(code).getContinent().toLowerCase().compareTo(searchContinent) == 0) {
                for (Connection e : symbolGraph.G().adj(symbolGraph.indexOf(code))) {
                    map.put(i,symbolGraph.indexOf(code));
                }
                i++;
            }
        }

        System.out.println("\n\nnovo grafo criado: \n");




//        int i = 0;
//        for (String code : airportST.keys()) {
//            System.out.println("pesquisa: " + airportST.get(code).getCode() + " " + airportST.get(code).getContinent() );
//            if (airportST.get(code).getContinent().toLowerCase().compareTo(searchContinent) == 0) {
//                for (Connection e : symbolGraph.G().adj(symbolGraph.indexOf(code))) {
//                    System.out.println("   Conecao: " + e.from() + " " + (airportST.get(symbolGraph.nameOf(e.from())).getCode()));
//                    if (airportST.get(symbolGraph.nameOf(e.from())).getContinent().toLowerCase().compareTo(searchContinent) == 0) {
//                        System.out.println("Conecao a inserir no novo grafo: " + e);
//                        newGraph.addEdge(e);
//                        map.put(i,e.from());
//                    }
//                    i++;
//                }
//            }
//        }

        System.out.println("key "+  "value");
        for (Integer inte: map.keys()) {

            System.out.println( "["+ inte + "]   " + map.get(inte) );
        }
        System.out.println();
        return newGraph;
    }



}
