package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // clean the logs file each program run
        log("reset", "");

        String pathAirports     = ".//data//airports.txt";
        String pathAirplanes    = ".//data//airplanes.txt";
        String pathAirlines     = ".//data//airlines.txt";

        SeparateChainingHashST<String, Airport> airportST = new SeparateChainingHashST<>();
        ImportFromFile.importAirports(airportST,pathAirports);

        SeparateChainingHashST<String, Airline> airlinesST = new SeparateChainingHashST<>();
        ImportFromFile.importAirlines(airlinesST, pathAirlines);

        RedBlackBST<Integer, Airplane> airplaneST = new RedBlackBST<>();
        ImportFromFile.importPlanes(airportST,airplaneST, airlinesST, pathAirplanes);

        RedBlackBST<Date, Flight> flightST = new RedBlackBST<>();
        Airplane airplane;
        Airport airportOfOrigin;
        Airport airportOfDestination;
        float distance;
        Date duration, flightDate;
        int passengers;

        distance = 2000;
        duration = new Date(0, 0, 0, 10, 0, 0);
        flightDate = new Date(7, 3, 2017, 12, 50, 30);
        passengers = 380;
        airplane = airplaneST.get(1); // id = 2 -> D. João de Castro
        airportOfOrigin = airportST.get("OPO");
        airportOfDestination = airportST.get("FRA");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

        distance = 10000;
        duration = new Date(1, 0, 0, 10, 0, 0);
        flightDate = new Date(8, 4, 2017, 23, 1, 30);
        passengers = 380;
        airplane = airplaneST.get(2); // id = 3 -> Wenceslau de Moraes
        airportOfOrigin = airportST.get("DXB");
        airportOfDestination = airportST.get("LAD");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

        distance = 21500;
        duration = new Date(0, 0, 0, 17, 43, 32);
        flightDate = new Date(9, 2, 2017, 23, 1, 30);
        passengers = 200;
        airplane = airplaneST.get(3); // id = 4 -> D. Francisco de Almeida
        airportOfOrigin = airportST.get("CDG");
        airportOfDestination = airportST.get("LAD");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

        distance = 500;
        duration = new Date(0, 0, 0, 7, 43, 32);
        flightDate = new Date(10, 3, 2017, 23, 1, 30);
        passengers = 100;
        airplane = airplaneST.get(4); // id = 5 -> Pero Vaz de Caminha
        airportOfOrigin = airportST.get("LAD");
        airportOfDestination = airportST.get("OPO");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

        distance = 1500;
        duration = new Date(0, 0, 0, 4, 43, 32);
        flightDate = new Date(10, 3, 2017, 23, 1, 30);
        passengers = 100;
        airplane = airplaneST.get(5); // id = 6 -> Luís vaz de camões
        airportOfOrigin = airportST.get("OPO");
        airportOfDestination = airportST.get("NRT");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

        distance = 1000;
        duration = new Date(0, 0, 0, 4, 43, 32);
        flightDate = new Date(11, 3, 2017, 23, 1, 30);
        passengers = 50;
        airplane = airplaneST.get(5); // id = 6 -> Luís vaz de camões
        airportOfOrigin = airportST.get("OPO");
        airportOfDestination = airportST.get("NRT");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

        distance = 5000;
        duration = new Date(0, 0, 0, 4, 43, 32);
        flightDate = new Date(27, 1, 1995, 17, 45, 30);
        passengers = 150;
        airplane = airplaneST.get(5); // id = 6 -> Luís vaz de camões
        airportOfOrigin = airportST.get("NRT");
        airportOfDestination = airportST.get("OPO");
        newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);

//        for(Date d : flightST.keys()){
//            System.out.println(flightST.get(d).toString());
//        }

//        removeAirplane(airplaneST, airportST, airplaneST.get(21));
//        for (Integer ap : airplaneST.keys()){
//            System.out.println(ap + ": " + airplaneST.get(ap).getName());
//        }

//        System.out.println("AIRLINES");
//        System.out.println("-------------------");


//        PrintInfo.allAirplanes(airplaneST);
//        System.out.println();
//        removeAirline(airlinesST,airplaneST,airportST,"TAP Air Portugal");
//        for (String c : airlinesST.keys()) {
//            System.out.println(airlinesST.get(c).getName() + " from " + airlinesST.get(c).getNationality());
//            System.out.println();
//        }
//
//        System.out.println("PLANES");
//        System.out.println("-------------------");
//        path = ".//data//airplanes.txt";
//

//
//        System.out.println();
//        for (Integer p :airplaneST.keys()) {
//            System.out.println("id " + p + " -> " + airplaneST.get(p).getName());
//            System.out.println("airline: " + airplaneST.get(p).getAirline().getName());
//            System.out.println();
//        }
//
//        System.out.println("AIRLINES & PLANES");
//        System.out.println("-------------------");
//        for (String ap : airlinesST.keys()) {
//            System.out.println(airlinesST.get(ap).getName() + " has " + airlinesST.get(ap).getNumPlanes() + " planes");
//            System.out.println("+++++++++++++++");
//            for(Airplane airplane : airlinesST.get(ap).getAirplanes()){
//                System.out.println(airplane.toString());
//            }
//
//        }


//        rui

//        PrintInfo.allAirports(airportST);
//        PrintInfo.airport(airportST, "NRT");
//        PrintInfo.airplane(airplaneST , 1);
//        PrintInfo.allAirports(searchAirportsOf(airportST,"Asia"));
//        PrintInfo.flightsBetweenTimes(flightST,new Date(1, 1, 2017, 23, 59, 10),new Date(21, 9, 2017, 21, 21, 21));
//        PrintInfo.flightsThisAirport(flightST,"OPO");
//        PrintInfo.allTravelsPlane(airplaneST,6);
//        PrintInfo.allAirplanes(airplaneST);

        // ricardo
//        ArrayList<Airport> result = mostTrafficAirport(airportST);
//        ArrayList<Flight> result = mostPassengersFlight(flightST);
//        ArrayList<Airport> result = mostPassengersAirport(airportST);
//        System.out.println("++++++++++++");
//        result.forEach((f)->{
//            System.out.println(f.toString());
//        });

    }

    private static void newFlight(float distance, Date duration, Date date, int passengers, Airplane airplane,
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
            if(airportST.get(code).getContinent().equals(search) || airportST.get(code).getCountry().equals(search)){
                airportSearch.add(airportSearch.size(), airportST.get(code));
            }
        }
        return airportSearch;
    }

    /**
     * Stores actions like insertions of airports, airlines and airplanes and removals in a log file
     * @param from tells from wich class the action is made
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
     * @param airplaneST is the symbol table where it is stored in this Main class
     * @param plane is the plane to remove
     */
    private static void removeAirplane(RedBlackBST<Integer, Airplane> airplaneST,SeparateChainingHashST<String, Airport> airportST,
                                       Airplane plane){
        Airline airline = plane.getAirline();
        log("Airline \"" + airline.getName() + "\"", "Removed airplane \"" + plane.getName() + "\"");
        log("AirplaneST", "Removed airplane \"" + plane.getName() + "\"");
        log("Airport \"" + airportST.get(plane.getAirportCode()) + "\"", "Removed airplane \"" + plane.getName() + "\"");
        airline.removePlane(plane);
        airportST.get(plane.getAirportCode()).sendPlane(plane); // goes to the airport where the plane is parked to remove it
        airplaneST.put(plane.getId()-1, null); // ids on the ST starts from 0 and airplanes ids from 1
    }

    /**
     * Removes an airline including all its airplanes
     * @param airlineST ST with all the airlines
     * @param airplaneST ST with all the airplanes
     * @param airportST ST with all the airports
     * @param airlineName is the airline to remove
     */
    private static void removeAirline(SeparateChainingHashST<String, Airline> airlineST, RedBlackBST<Integer, Airplane> airplaneST, SeparateChainingHashST<String,
            Airport> airportST, String airlineName ){
        for (String a: airlineST.keys()) {
            if(airlineST.get(a).getName().equals(airlineName)){
                for (Integer p: airlineST.get(a).getFleet().keys()) {
                    removeAirplane(airplaneST, airportST,airlineST.get(a).getFleet().get(p));
                }
                log("AirlineST","Removed airline \"" + airlineST.get(a).getName() + "\"");
                airlineST.put(airlineST.get(a).getName(),null);
            }
        }
    }

    /** remove airport
     * airportST
     * remover dos voos ligados a este o aeroporto
     * escrever pa ficheiro log do aeroporto em questao os voos e aeroportos do historico
     * guardar em ficheiro "backup" do aeroporto por onde se pode voltar a cria-lo
     * remover dos avioes estacionados no aeroporto o "code" na classe deles
     */


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
