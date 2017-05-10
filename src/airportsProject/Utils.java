package airportsProject;

import airportsProject.Exceptions.WrongTypeFileException;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils{
    public static int nValue    = 200;
    public static int mValue    = 250;
    public static int euroValue = 30;  // 30 €/L
    public static int windCost  = 10;   // aditional cost by km/h of wind (adds 10L per km/h to the airplane fuel cost if it is against the airplane, or substracts if in favor)
    public static int mapWidth  = 1536; // width of the world map used
    public static int mapHeight = 768; // height of the world map used

    private static Utils instance = null;

    // paths to default data
    private static final String pathAirports     = ".//data//airports.txt";
    private static final String pathAirplanes    = ".//data//airplanes.txt";
    private static final String pathAirlines     = ".//data//airlines.txt";

    // Symbol Tables
    private static SeparateChainingHashST<String, Airport> airportST  = new SeparateChainingHashST<>();
    private static SeparateChainingHashST<String, Airline> airlinesST = new SeparateChainingHashST<>();
    private static RedBlackBST<Integer, Airplane> airplaneST          = new RedBlackBST<>();
    private static RedBlackBST<Date, Flight> flightST                 = new RedBlackBST<>();

    protected Utils(){
        // prevents instantiation
    }

    public static Utils getInstance(){
        if(instance == null){
            instance = new Utils();
        }
        return instance;
    }

    /**
     * Will fill the symbol tables with the data required
     * @param path -> will be empty if the user chooses to create a new program and will bring a file path if
     *             he chooses to load a program
     */
    public static boolean initProgram(String path){
        if(path.length() > 0){ // load program
            try{
                ImportFromFile.loadProgram(path,airportST,airlinesST,airplaneST,flightST);
                File file = new File(path);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                System.out.println("Last opened in: " + sdf.format(file.lastModified()));
            }catch (WrongTypeFileException wt){
                System.out.println(wt.getMessage());
                return false;
            }
        }else{ // new program
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
        flightST.put(flight1.getDate(), flight1);
        flightST.put(flight2.getDate(), flight2);
        flightST.put(flight3.getDate(), flight3);
        return true;
    }

    public SeparateChainingHashST<String, Airport> getAirports(){
        return airportST;
    }

    public void newAirport(Airport airport){
        airportST.put(airport.getCode(), airport);
    }

//    public void editAirport(String airportCode, String name, float rating){
//        if(airportST.get(airportCode) != null){
//            airportST.get(airportCode).setName(name);
//            airportST.get(airportCode).setRating(rating);
//        }
//        log("airportST", "Edited airport " + airportCode + " - \"" + name + "\"");
//    }

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
     * @return returns false if the airline does not exist and true if the editing was successful
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

    public RedBlackBST<Integer, Airplane> getAirplanes(){
        return airplaneST;
    }

//    public void newAirplane(Airplane airplane){
//        airplaneST.put(airplane.getId()-1, airplane);
//    }

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
    public static void newFlight(float distance, Date duration, Date date, int passengers, Airplane airplane,
                                  Airport airportOfOrigin, Airport airportOfDestination, RedBlackBST<Date, Flight> flightST){

        Flight newFlight = new Flight(distance, duration, date, passengers, airplane, airportOfOrigin, airportOfDestination);
        flightST.put(newFlight.getDate(), newFlight);
        log("flightST", "New flight leaving at:" + newFlight.getDate() +
                "; duration: " + newFlight.getDuration().getDuration() +
                "; from \"" + newFlight.getAirportOfOrigin().getName() +
                " ( " + newFlight.getDistance() + "m) " +
                "\"; to \"" + newFlight.getAirportOfDestination().getName() +
                "\"; airplane: \"" + newFlight.getAirplane().getName() + "\"");
    }

    /**
     * searches airports of a certain country / continent
     * @param airportST Symbol table that stores all the available airports
     * @param search the continent or country to search for
     * @return returns all the airports that matched the search
     */
    private static ArrayList<Airport> searchAirportsOf(SeparateChainingHashST<String, Airport> airportST, String search){
        ArrayList<Airport> airportSearch = new ArrayList<>();
        for (String code: airportST.keys()) {
            if(airportST.get(code).getContinent().toLowerCase().compareTo(search) == 0 || airportST.get(code).getCountry().toLowerCase().compareTo(search) == 0){
                airportSearch.add(airportSearch.size(), airportST.get(code));
            }
        }
        return airportSearch;
    }

    /**
     * Creates a copy of the current program so the next time the program runs there will be an option of loading the previous program
     * or create a new one from the data imported from the files and without flights
     * @param airportST ST of the airports
     * @param airlineST ST of the airlines
     * @param airplaneST ST of the airplanes
     * @param flightST ST of the flights
     */
    public static void dump(SeparateChainingHashST<String, Airport> airportST, SeparateChainingHashST<String, Airline> airlineST,
                            RedBlackBST<Integer, Airplane> airplaneST, RedBlackBST<Date, Flight> flightST){
        String path = ".//data/loadProgram.txt";
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
            for(String al : airlineST.keys()){
                Airline airline = airlineST.get(al);
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

    /**
     * Class used on the 1st phase only, where we will use the terminal to ask the user all the data needed to insert a new airport
     * @param airportST ST where will be stored the new airport
     * @return returns false if there was an error creating the new airport, and true if it was created successfully
     */
    private static boolean addAirportTerminal(SeparateChainingHashST<String, Airport> airportST){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add a new airport:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Code: ");
        String code = scanner.nextLine();
        code = code.toUpperCase();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Country: ");
        String country = scanner.nextLine();
        System.out.print("Continent: ");
        String continent = scanner.nextLine();
        System.out.print("Rating: ");
        float rating = scanner.nextFloat();
        return addAirport(name, code, city, country, continent, rating, airportST);
    }

    /**
     * Class used on the 1st phase only, where we will use the terminal to ask the user all the data needed to insert a new airplane
     * @param airportST ST that has the airport where will be added the new airplane
     * @param airplaneST ST where will be stored the new airplane
     * @param airlineST ST that has the airline that owns the new airplane
     * @return returns false if there was an error creating the new airplane, and true if it was created successfully
     */
    private static boolean addAirplaneTerminal(SeparateChainingHashST<String, Airport> airportST, RedBlackBST<Integer, Airplane> airplaneST,
                                               SeparateChainingHashST<String, Airline> airlineST){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add a new airplane:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Airline name to which belongs: ");
        String airlineName = scanner.nextLine();
        System.out.print("Cruise Speed: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float cruiseSpeed = scanner.nextFloat();
        System.out.print("Cruise Altitude: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float cruiseAltitude = scanner.nextFloat();
        System.out.print("Max Range: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float maxRange = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Airport Code where it is parked: ");
        String airportCode = scanner.nextLine();
        airportCode = airportCode.toUpperCase();
        System.out.print("Passenger capacity: ");
        int passengersCap = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Fuel capacity: ");
        int fuelCap = scanner.nextInt();
//        return addAirplane(model, name, airlineName, cruiseSpeed, cruiseAltitude, maxRange, airportCode, passengersCap, fuelCap,
//                airportST, airplaneST, airlineST);
        return true;
    }

    /**
     * Class used on the 1st phase only, where we will use the terminal to ask the user all the data needed to insert a new airline
     * @param airlineST ST where will be stored the new airline
     * @return returns false if there was an error creating the new airline, and true if it was created successfully
     */
    private static boolean addAirlineTerminal(SeparateChainingHashST<String, Airline> airlineST){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add a new airline:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Nationality: ");
        String nationality = scanner.nextLine();
        return addAirline(name, nationality, airlineST);
    }

    /**
     * Class used on the 1st phase only, where we will use the terminal to ask the user all the data needed to edit an airport
     * @param airportCode code of the airport to edit
     * @param airportST ST where is stored the airport
     * @return returns false if there was an error editing the airport, and true if it was edited and saved successfully
     */
    private static boolean editAirportTerminal(String airportCode, SeparateChainingHashST<String, Airport> airportST){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Edit an airport:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Rating: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float rating = scanner.nextFloat();
//        return editAirport(airportCode, name, rating, airportST);
        return true;
    }

    /**
     * Class used on the 1st phase only, where we will use the terminal to ask the user all the data needed to edit an airplane
     * @param airplaneId ID of the airplane to edit
     * @param airplaneST ST where is stored the airplane
     * @return returns false if there was an error editing the airplane, and true if it was edited and saved successfully
     */
    private static boolean editAirplaneTerminal(int airplaneId, RedBlackBST<Integer, Airplane> airplaneST){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Edit an airplane:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Cruise Speed: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float cruiseSpeed = scanner.nextFloat();
        System.out.print("Cruise Altitude: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float cruiseAltitude = scanner.nextFloat();
        System.out.print("Max Range: ");
        while(!scanner.hasNextFloat()) {
            System.out.print("Please enter a float number");
            scanner.nextLine();
        }
        float maxRange = scanner.nextFloat();
        System.out.print("Passenger capacity: ");
        int passengersCap = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Fuel capacity: ");
        int fuelCap = scanner.nextInt();
//        return editAirplane(airplaneId, model, name, cruiseSpeed, cruiseAltitude, maxRange, passengersCap, fuelCap, airplaneST);
        return true;
    }

    /**
     * Utility function to check for a search input to be numeric or not
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

    /**
     * Class used on the 1st phase only, where we will use the terminal to ask the user all the data needed to edit an airline
     * @param airlineName name of the airline to edit
     * @param airlineST ST where is stored the airline
     * @return returns false if there was an error editing the airline, and true if it was edited and saved successfully
     */
    private static boolean editAirlineTerminal(String airlineName, SeparateChainingHashST<String, Airline> airlineST){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Edit an airline:");
        System.out.print("Name: ");
        String newName = scanner.nextLine();
        return true;
//        return editAirline(airlineName, newName, airlineST);
    }

    /**
     * Receives all the data needed to create a new airplane and creates it
     * Checking first if the airport and the airline connected to it are real
     * @param model model of the airplane
     * @param name name of the airplane
     * @param airlineName airline that owns the airplane
     * @param cruiseSpeed cruise speed of the airplane
     * @param cruiseAltitude cruise altitude of the airplane
     * @param maxRange max range of the airplane
     * @param airportCode airport where the airplane will be parked at first
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
     * Receives all the data needed to create a new airport and creates it
     * @param name name of the airport
     * @param code code of the airport
     * @param city city of the airport
     * @param country country of the airport
     * @param continent continent of the airport
     * @param rating rating of the airport
     * @param airportST ST where will be stored the new airport
     * @return returns false if the airport code already exists and true if the addition was successful
     */
    private static boolean addAirport(String name, String code,  String city, String country,
                                    String continent, Float rating, SeparateChainingHashST<String, Airport> airportST){
        // searches for the airport code existence, if it exists it can't add another one with the same code
        Airport thisPlaneAirport = airportST.get(code);
        if(thisPlaneAirport != null)
            return false;
        Airport newAirport = new Airport(name, code, city, country, continent, rating);
        airportST.put(code, newAirport);
        log("airportST", "New airport \"" + newAirport.getName() + "\"");
        return true;
    }

    /**
     * Receives the data possible to be edited on an airport and changes it using the class setters
     * @param code code of the airport to edit
     * @param name new name of the airport
     * @param rating new rating of the airport
     */
    public void editAirport(String code, String name, Float rating){
        Airport thisPlaneAirport = airportST.get(code);
        airportST.get(code).setName(name);
        airportST.get(code).setRating(rating);
        log("airportST", "Edited airport [" +  airportST.get(code).getCode() +  "] \" Name:" + airportST.get(code).getName() + "\" Rating:" +
                airportST.get(code).getRating());
    }

    /**
     * Receives all the data needed to create a new airline and creates it
     * @param name name of the airline
     * @param nationality nationality of the airline
     * @param airlineST ST where will be stored the new airline
     * @return returns false if an airline with that name already exists and true if the addition was successful
     */
    private static boolean addAirline(String name, String nationality, SeparateChainingHashST<String, Airline> airlineST){
        // searches for an airline with the same name
        Airline airline = airlineST.get(name);
        if(airline != null)
            return false;
        Airline newAirline = new Airline(name, nationality);
        airlineST.put(name, newAirline);
        log("airlineST", "New airline \"" + newAirline.getName() + "\"");
        return true;
    }

    /**
     * Receives the data possible to be edited on an airline and changes it using the class setters
     * @param airlineName previous name of the airline to match it in the ST
     * @param newName new name of the airline
     * @param airlineST ST that holds the airline to edit
     * @return returns false if the airline does not exist and true if the editing was successful
     */
//    private static boolean editAirline(String airlineName, String newName, SeparateChainingHashST<String, Airline> airlineST){
//        // searches for the airline existence
//        Airline airline = airlineST.get(airlineName);
//        if(airline == null)
//            return false;
//        airline.setName(newName);
//        log("airlineST", "Edited airline \"" + airline.getName() + "\" from " + airline.getNationality());
//        return true;
//    }

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
}
