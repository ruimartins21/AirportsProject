package airportsProject;

import airportsProject.Exceptions.AirportNotExistException;

import edu.princeton.cs.algs4.StdOut;
import libs.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static int nValue = 200;
    static int mValue = 250;
    static int euroValue = 30;  // 30 €/L
    static int windCost = 10;   // aditional cost by km/h of wind (adds 10L per km/h to the airplane fuel cost if it is against the airplane, or substracts if in favor)
    static int mapWidth = 1536; // width of the world map used
    static int mapHeight = 768; // height of the world map used

    public static void main(String[] args) {

        SymbolEdgeWeightedDigraph symbolGraph =new SymbolEdgeWeightedDigraph(".//data//graph.txt",";");


        System.out.println(symbolGraph.G());


        DijkstraSP dijkstraSP = new DijkstraSP(symbolGraph.G(),0);

        for (DirectedEdge e : dijkstraSP.pathTo(30)) {
            StdOut.print(e + "   ");
        }
        StdOut.println();
        System.out.println();
        System.out.println();
        System.out.println();



        boolean validChoice = false;
        int choice, airplaneId;
        String airportCode, airlineName;
        ArrayList<Airport> airportResults;
        ArrayList<Flight> flightResults;

        String pathAirports     = ".//data//airports.txt";
        String pathAirplanes    = ".//data//airplanes.txt";
        String pathAirlines     = ".//data//airlines.txt";

        SeparateChainingHashST<String, Airport> airportST  = new SeparateChainingHashST<>();
        SeparateChainingHashST<String, Airline> airlinesST = new SeparateChainingHashST<>();
        RedBlackBST<Integer, Airplane> airplaneST          = new RedBlackBST<>();
        RedBlackBST<Date, Flight> flightST                 = new RedBlackBST<>();

        Airplane airplane;
        Airport airportOfDestination;
        float distance;
        Date duration, flightDate;
        int passengers;

        // New program or load previous program
        Scanner scanner = new Scanner(System.in);
        while(!validChoice){
            System.out.println("# Airport Management #");
            System.out.println("1 - New Program");
            System.out.println("2 - Load Previous Program");
            choice = scanner.nextInt();
            if(choice == 1){
                System.out.println("Creating new program ...");
                log("reset", ""); // clean the logs file
                validChoice = true;
                ImportFromFile.importAirports(airportST, pathAirports);
                ImportFromFile.importAirlines(airlinesST, pathAirlines);
                ImportFromFile.importPlanes(airportST, airplaneST, airlinesST, pathAirplanes);

                // Hardcoded Flights to populate the ST
                distance = 2000;
                duration = new Date(0, 0, 0, 10, 0, 0);
                flightDate = new Date(7, 3, 2017, 12, 50, 30);
                passengers = 380;
                airplane = airplaneST.get(1); // id = 2 -> D. João de Castro
                airportOfDestination = airportST.get("FRA");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);

                distance = 10000;
                duration = new Date(1, 0, 0, 10, 0, 0);
                flightDate = new Date(8, 4, 2017, 23, 1, 30);
                passengers = 380;
                airplane = airplaneST.get(2); // id = 3 -> Wenceslau de Moraes
                airportOfDestination = airportST.get("LAD");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);

                distance = 21500;
                duration = new Date(0, 0, 0, 17, 43, 32);
                flightDate = new Date(9, 2, 2017, 23, 1, 30);
                passengers = 200;
                airplane = airplaneST.get(3); // id = 4 -> D. Francisco de Almeida
                airportOfDestination = airportST.get("LAD");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);

                distance = 500;
                duration = new Date(0, 0, 0, 7, 43, 32);
                flightDate = new Date(10, 3, 2017, 23, 1, 30);
                passengers = 100;
                airplane = airplaneST.get(4); // id = 5 -> Pero Vaz de Caminha
                airportOfDestination = airportST.get("OPO");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);

                distance = 1500;
                duration = new Date(0, 0, 0, 4, 43, 32);
                flightDate = new Date(10, 3, 2017, 23, 1, 30);
                passengers = 100;
                airplane = airplaneST.get(5); // id = 6 -> Luís vaz de camões
                airportOfDestination = airportST.get("NRT");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);

                distance = 1000;
                duration = new Date(0, 0, 0, 4, 43, 32);
                flightDate = new Date(11, 3, 2017, 23, 1, 30);
                passengers = 50;
                airplane = airplaneST.get(5); // id = 6 -> Luís vaz de camões
                airportOfDestination = airportST.get("NRT");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);

                distance = 5000;
                duration = new Date(0, 0, 0, 4, 43, 32);
                flightDate = new Date(27, 1, 1995, 17, 45, 30);
                passengers = 150;
                airplane = airplaneST.get(5); // id = 6 -> Luís vaz de camões
                airportOfDestination = airportST.get("OPO");
                newFlight(distance, duration, flightDate, passengers, airplane, airportST.get(airplane.getAirportCode()), airportOfDestination, flightST);
                // Overwrites previous saved program
                dump(airportST, airlinesST, airplaneST, flightST);
            }else if(choice == 2){
                System.out.println("Loading previous program ...");
                if(ImportFromFile.currentProgram(".//data//currentProgram.txt",airportST,airlinesST,airplaneST,flightST)){
                    File file = new File(".//data//currentProgram.txt");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    System.out.println(" (Last opened in: " + sdf.format(file.lastModified()) + ")");
                    validChoice = true;
                }else{
                    System.out.println("Error opening previous program.");
                }
            }
        }

        /* Interaction Menu */
        while(true){
            validChoice = false;
            System.out.println("\n# Operations Available #");
            System.out.println("0 - Manage information (Insert / Edit / Remove)");
            System.out.println("-----------------------------------");
            System.out.println("* Statistics *");
            System.out.println("1 - Show all information about an airport\n" +
                    "2 - Show all information relative to an airplane\n" +
                    "3 - Show all airports from a certain Country / Continent\n" +
                    "4 - Show all flights with origin/destination on a certain airport\n" +
                    "5 - Show all flights done by an airplane\n" +
                    "6 - Show all flights done in a period of time\n" +
                    "7 - Airport with the most traffic\n" +
                    "8 - Flight that carried more passengers\n" +
                    "9 - Airport that carried more passengers\n" +
                    "10 - Latest flight of an airplane\n" +
                    "-1 - Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 0: // Insert / Edit / Remove
                    System.out.println("1 - Add a new airport\n" +
                                    "2 - Add a new airplane\n" +
                                    "3 - Add a new airline\n" +
                                    "4 - Edit an airport\n" +
                                    "5 - Edit an airplane\n" +
                                    "6 - Edit an airline\n" +
                                    "7 - Remove an airport\n" +
                                    "8 - Remove an airplane\n" +
                                    "9 - Remove an airline\n");
                    int newChoice = scanner.nextInt();
                    if(newChoice >= 1 && newChoice <= 9){
                        switch (newChoice){
                            case 1: // add a new airport
                                if(!addAirportTerminal(airportST)){
                                    System.out.println("Couldn't add the new airport.");
                                }
                                scanner.nextLine();
                                break;
                            case 2: // add a new airplane
                                if(!addAirplaneTerminal(airportST, airplaneST, airlinesST)){
                                    System.out.println("Couldn't add the new airplane.");
                                }
                                scanner.nextLine();
                                break;
                            case 3: // add a new airline
                                if(!addAirlineTerminal(airlinesST)){
                                    System.out.println("Couldn't add the new airline.");
                                }
                                scanner.nextLine();
                                break;
                            case 4: // edit an airport
                                scanner.nextLine();
                                System.out.print("Code of the airport: ");
                                airportCode = scanner.nextLine();
                                airportCode = airportCode.toUpperCase();
                                if(!editAirportTerminal(airportCode, airportST)){
                                    System.out.println("Error editing the airport.");
                                }
                                break;
                            case 5: // edit an airplane
                                scanner.nextLine();
                                System.out.print("ID of the airplane: ");
                                airplaneId = scanner.nextInt();
                                if(!editAirplaneTerminal(airplaneId, airplaneST)){
                                    System.out.println("Error editing the airplane.");
                                }
                                break;
                            case 6: // edit an airline
                                scanner.nextLine();
                                System.out.print("Name of the airline: ");
                                airlineName = scanner.nextLine();
                                if(!editAirlineTerminal(airlineName, airlinesST)){
                                    System.out.println("Error editing the airline.");
                                }
                                break;
                            case 7: // remove an airport
                                scanner.nextLine();
                                System.out.print("Code of the airport: ");
                                airportCode = scanner.nextLine();
                                airportCode = airportCode.toUpperCase();
                                try {
                                    removeAirport(airportST, airplaneST, airportCode);
                                    System.out.println("Airport removed successfully");
                                }catch (AirportNotExistException ae){
                                    System.out.println(ae.getMessage());
                                }
                                break;
                            case 8: // remove an airplane
                                scanner.nextLine();
                                System.out.print("ID of the airplane: ");
                                airplaneId = scanner.nextInt();
                                Airplane airplaneToRemove;
                                if((airplaneToRemove = airplaneST.get(airplaneId)) != null){
                                    removeAirplane(airplaneST, airportST, airplaneToRemove);
                                    System.out.println("Airplane removed successfully");
                                }else{
                                    System.out.println("! There's no airplane with that ID !");
                                }
                                break;
                            case 9: // remove an airline
                                scanner.nextLine();
                                System.out.print("Name of the airline: ");
                                airlineName = scanner.nextLine();
                                Airline airlineToRemove;
                                if((airlineToRemove = airlinesST.get(airlineName)) != null){
                                    removeAirline(airlinesST, airplaneST, airportST, airlineToRemove);
                                    System.out.println("Airline removed successfully");
                                }else{
                                    System.out.println("! There's no airline with that name !");
                                }
                                break;
                        }
                        // any change made will now be saved to the "currentProgram" file so when a new program is launched, when loading it,
                        // the program will have the changes made
                        dump(airportST, airlinesST, airplaneST, flightST);
                    }else{
                        System.out.println("Choose a valid number");
                    }
                    break;
                case 1: // Show all information about an airport
                    System.out.print("Code of the airport: ");
                    airportCode = scanner.nextLine();
                    airportCode = airportCode.toUpperCase();
                    if(airportST.get(airportCode) != null){
                        PrintInfo.airport(airportST, airportCode);
                    }else{
                        System.out.println("! There's no airport with that code !");
                    }
                    break;
                case 2: // Show all information relative to an airplane
                    System.out.print("ID of the airplane: ");
                    airplaneId = scanner.nextInt();
                    if(airplaneST.get(airplaneId) != null){
                        PrintInfo.airplane(airplaneST, airplaneId);
                    }else{
                        System.out.println("! There's no airplane with that ID !");
                    }
                    scanner.nextLine();
                    break;
                case 3: // Show all airports from a certain Country / Continent
                    System.out.print("Country or Continent: ");
                    String countryContinent = scanner.nextLine();
                    countryContinent = countryContinent.toLowerCase();
                    if(!(airportResults = searchAirportsOf(airportST, countryContinent)).isEmpty()){
                        for(Airport a : airportResults){
                            System.out.println("\"" + a.getName() + "\"");
                        }
                    }else{
                        System.out.println("! There's no airport from that country / continent !");
                    }
                    break;
                case 4: // Show all flights with origin/destination on a certain airport
                    System.out.print("Code of the airport: ");
                    airportCode = scanner.nextLine();
                    airportCode = airportCode.toUpperCase();
                    Airport airport;
                    if((airport = airportST.get(airportCode)) != null){
                        PrintInfo.flightsOfThisAirport(airport);
                    }else{
                        System.out.println("! There's no airport with that code !");
                    }
                    break;
                case 5: // Show all flights done by an airplane
                    System.out.print("ID of the airplane: ");
                    airplaneId = scanner.nextInt();
                    Airplane airplaneToSearch;
                    airplaneId -= 1; // ids of the planes are all one number below on the ST
                    if((airplaneToSearch = airplaneST.get(airplaneId)) != null){
                        PrintInfo.allTravelsPlane(airplaneToSearch);
                    }else{
                        System.out.println("! There's no airplane with that ID !");
                    }
                    scanner.nextLine();
                    break;
                case 6: // Show all flights done in a period of time
                    Date startingDate = null, endingDate = null; // it will never send a null date to the method because of the conditions before the call
                    while(!validChoice) {
                        System.out.println("Insert starting date: ");
                        System.out.print("Day: ");
                        int day = scanner.nextInt();
                        System.out.print("Month: ");
                        int month = scanner.nextInt();
                        System.out.print("Year: ");
                        int year = scanner.nextInt();
                        System.out.print("Hours: ");
                        int hours = scanner.nextInt();
                        System.out.print("Minutes: ");
                        int minutes = scanner.nextInt();
                        System.out.print("Seconds: ");
                        int seconds = scanner.nextInt();
                        startingDate = new Date(day, month, year, hours, minutes, seconds);
                        if(startingDate.isValid()){
                            validChoice = true;
                        }else{
                            System.out.println("Date not valid");
                        }
                    }
                    validChoice = false;
                    while(!validChoice) {
                        System.out.println("Insert ending date: ");
                        System.out.print("Day: ");
                        int day = scanner.nextInt();
                        System.out.print("Month: ");
                        int month = scanner.nextInt();
                        System.out.print("Year: ");
                        int year = scanner.nextInt();
                        System.out.print("Hours: ");
                        int hours = scanner.nextInt();
                        System.out.print("Minutes: ");
                        int minutes = scanner.nextInt();
                        System.out.print("Seconds: ");
                        int seconds = scanner.nextInt();
                        endingDate = new Date(day, month, year, hours, minutes, seconds);
                        if(endingDate.isValid()){
                            if(endingDate.beforeDate(startingDate) || endingDate.compareTo(startingDate) == 0){
                                System.out.println("Ending date can't be previous or equal to the starting date");
                            }else{
                                validChoice = true;
                            }
                        }else{
                            System.out.println("Date not valid");
                        }
                    }
                    PrintInfo.flightsBetweenTimes(flightST, startingDate, endingDate);
                    break;
                case 7: // Airport with the most traffic
                    if(!(airportResults = mostTrafficAirport(airportST)).isEmpty()){
                        for(Airport a : airportResults){
                            System.out.println("\"" + a.getName() + "\"");
                        }
                    }else{
                        System.out.println("! There're no airports with traffic !");
                    }
                    break;
                case 8: // Flight that carried more passengers
                    if(!(flightResults = mostPassengersFlight(flightST)).isEmpty()){
                        for(Flight a : flightResults){
                            System.out.println(a);
                        }
                    }else{
                        System.out.println("! There're no flights !");
                    }
                    break;
                case 9: // Airport that carried more passengers
                    if(!(airportResults = mostPassengersAirport(airportST)).isEmpty()){
                        for(Airport a : airportResults){
                            System.out.println("\"" + a.getName() + "\"");
                        }
                    }else{
                        System.out.println("! There're no flights !");
                    }
                    break;
                case 10: // Latest flight of an airplane
                    System.out.print("ID of the airplane: ");
                    while(!scanner.hasNextInt()) {
                        System.out.print("Please enter an integer number");
                        scanner.nextLine();
                    }
                    airplaneId = scanner.nextInt();
                    airplaneId -= 1;
                    if(airplaneST.get(airplaneId) != null){
                        PrintInfo.latestFlightOfAirplane(airplaneST.get(airplaneId));
                    }else{
                        System.out.println("! There's no airplane with that ID !");
                    }
                    break;
                case -1: return;
                default: break;
            }
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
        String path = ".//data/currentProgram.txt";
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
     * @param airplaneST is the symbol table where it is stored in this MainGUI class
     * @param plane is the plane to remove
     */
    private static void removeAirplane(RedBlackBST<Integer, Airplane> airplaneST, SeparateChainingHashST<String, Airport> airportST,
                                       Airplane plane){
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
     * @param airlineST ST with all the airlines
     * @param airplaneST ST with all the airplanes
     * @param airportST ST with all the airports
     * @param airline is the airline to remove
     */
    private static void removeAirline(SeparateChainingHashST<String, Airline> airlineST, RedBlackBST<Integer, Airplane> airplaneST,
                                      SeparateChainingHashST<String, Airport> airportST, Airline airline ){
        for (Integer p: airline.getFleet().keys()) {
            removeAirplane(airplaneST, airportST, airline.getFleet().get(p));
        }
        log("AirlineST","Removed airline \"" + airline.getName() + "\"");
        airlineST.put(airline.getName(),null);
    }

    /**
     * Removes an airport incluiding all the airplanes parked there
     * @param airportST ST with all the airports
     * @param airportCode Code of the airport to remove
     */
    private static void removeAirport(SeparateChainingHashST<String, Airport> airportST, RedBlackBST<Integer, Airplane> airplaneST,
                                      String airportCode) throws AirportNotExistException{
        boolean removed = false;
        for (String a : airportST.keys() ) {
            if(a.compareTo(airportCode) == 0){
                for (Integer p : airportST.get(a).getAirplanes().keys() ) {
                    removeAirplane(airplaneST, airportST, airportST.get(a).getAirplanes().get(p));
                }
                log("AirportST","Removed airport \"" + airportST.get(a).getName() + "\"");
                airportST.put(a, null);
                removed = true;
            }
        }
        if(!removed){
            throw new AirportNotExistException("Airport with code \"" + airportCode + "\" does not exist!\n");
        }
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
        return addAirplane(model, name, airlineName, cruiseSpeed, cruiseAltitude, maxRange, airportCode, passengersCap, fuelCap,
                airportST, airplaneST, airlineST);
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
        return editAirport(airportCode, name, rating, airportST);
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
        return editAirplane(airplaneId, model, name, cruiseSpeed, cruiseAltitude, maxRange, passengersCap, fuelCap, airplaneST);
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
        return editAirline(airlineName, newName, airlineST);
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
     * @param airportST ST that has all the airports to check the consistency of the airport where the airplane will be
     * @param airplaneST ST where will be stored the new airplane
     * @param airlineST ST that has all the airlines to check the consistency of the airline that owns the airplane
     * @return returns false if the airline or the airport in question does not exist
     */
    private static boolean addAirplane(String model, String name, String airlineName,  float cruiseSpeed, float cruiseAltitude,
                                        float maxRange, String airportCode, int passengersCapacity, int fuelCapacity,
                                    SeparateChainingHashST<String, Airport> airportST, RedBlackBST<Integer, Airplane> airplaneST,
                                    SeparateChainingHashST<String, Airline> airlineST){
        int id = 1; // if the airplanes ST is empty this will be the first entry
        if(!airplaneST.isEmpty())
            id = airplaneST.max() + 2; // adds 2 because 1 is for the id to match airplanes ids and another to add the new plane

        // searches for the airline existence
        Airline thisPlaneAirline = airlineST.get(airlineName);
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
     * @param airplaneST ST where will be stored the new airplane
     * @return returns false if the airplane with the ID passed does not exist and true if the editing was successful
     */
    private static boolean editAirplane(int idAirplane, String model, String name,  float cruiseSpeed, float cruiseAltitude,
                                     float maxRange, int passengersCapacity, int fuelCapacity,
                                     RedBlackBST<Integer, Airplane> airplaneST){
        idAirplane -= 1; // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
        // searches for the airplane existence
        Airplane plane = airplaneST.get(idAirplane);
        if(plane == null) {
            return false;
        }
        plane.setModel(model);
        plane.setName(name);
        plane.setCruiseSpeed(cruiseSpeed);
        plane.setCruiseAltitude(cruiseAltitude);
        plane.setMaxRange(maxRange);
        plane.setPassengersCapacity(passengersCapacity);
        plane.setFuelCapacity(fuelCapacity);
        log("airplaneST", "Edited airplane [" + plane.getId() +"] \"" + plane.getName() + "\"");
        return true;
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
     * @param airportST ST holding the airport
     * @return returns false if the airport does not exist and true if the editing was successful
     */
    private static boolean editAirport(String code, String name, Float rating, SeparateChainingHashST<String, Airport> airportST){
        // searches for the airportCode existence
        Airport thisPlaneAirport = airportST.get(code);
        if(thisPlaneAirport == null)
            return false;
        airportST.get(code).setName(name);
        airportST.get(code).setRating(rating);
        log("airportST", "Edited airport [" +  airportST.get(code).getCode() +  "] \" Name:" + airportST.get(code).getName() + "\" Rating:" +
                airportST.get(code).getRating());
        return true;
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
    private static boolean editAirline(String airlineName, String newName, SeparateChainingHashST<String, Airline> airlineST){
        // searches for the airline existence
        Airline airline = airlineST.get(airlineName);
        if(airline == null)
            return false;
        airline.setName(newName);
        log("airlineST", "Edited airline \"" + airline.getName() + "\" from " + airline.getNationality());
        return true;
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
}
