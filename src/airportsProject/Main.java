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
        ImportFromFile.importPlanes(airplaneST, airlinesST, pathAirplanes);

        RedBlackBST<Date, Flight> flightST = new RedBlackBST<>();

        Airplane flight1Airplane = airplaneST.get(1);
        Airport flight1AirportOfOrigin = airportST.get("OPO");
        Airport flight1AirportOfDestination = airportST.get("FRA");
        Flight flight1 = new Flight(2000, new Date(0, 0, 0, 10, 0, 0), new Date(7, 3, 2017, 12, 50, 30), 380, flight1Airplane,
                flight1AirportOfOrigin, flight1AirportOfDestination);
        flightST.put(flight1.getDate(), flight1);

        Airplane flight2Airplane = airplaneST.get(2);
        Airport flight2AirportOfOrigin = airportST.get("DXB");
        Airport flight2AirportOfDestination = airportST.get("SCL");
        Flight flight2 = new Flight(10000, new Date(1, 0, 0, 10, 0, 0), new Date(8, 4, 2017, 23, 1, 30), 380, flight2Airplane,
                flight2AirportOfOrigin, flight2AirportOfDestination);
        flightST.put(flight2.getDate(), flight2);

        Airplane flight3Airplane = airplaneST.get(3);
        Airport flight3AirportOfOrigin = airportST.get("CDG");
        Airport flight3AirportOfDestination = airportST.get("LAD");
        Flight flight3 = new Flight(21500, new Date(0, 0, 0, 17, 43, 32), new Date(9, 2, 2017, 23, 1, 30), 200, flight3Airplane,
                flight3AirportOfOrigin, flight3AirportOfDestination);
        flightST.put(flight3.getDate(), flight3);

        Airplane flight4Airplane = airplaneST.get(4);
        Airport flight4AirportOfOrigin = airportST.get("LAD");
        Airport flight4AirportOfDestination = airportST.get("OPO");
        Flight flight4 = new Flight(500, new Date(0, 0, 0, 7, 43, 32), new Date(10, 3, 2017, 23, 1, 30), 100, flight4Airplane,
                flight4AirportOfOrigin, flight4AirportOfDestination);
        flightST.put(flight4.getDate(), flight4);

        Airplane flight6Airplane = airplaneST.get(5);
        Airport flight6AirportOfOrigin = airportST.get("OPO");
        Airport flight6AirportOfDestination = airportST.get("NRT");
        Flight flight6 = new Flight(500, new Date(0, 0, 0, 4, 43, 32), new Date(10, 3, 2017, 23, 1, 30), 100, flight6Airplane,
                flight6AirportOfOrigin, flight6AirportOfDestination);
        flightST.put(flight6.getDate(), flight6);

        Airplane flight5Airplane = airplaneST.get(5); // key 5 = id 6
        Airport flight5AirportOfOrigin = airportST.get("OPO");
        Airport flight5AirportOfDestination = airportST.get("NRT");
        Flight flight5 = new Flight(500, new Date(0, 0, 0, 4, 43, 32), new Date(11, 3, 2017, 23, 1, 30), 100, flight5Airplane,
                flight5AirportOfOrigin, flight5AirportOfDestination);
        flightST.put(flight5.getDate(), flight5);

        Airplane flight7Airplane = airplaneST.get(5);
        Airport flight7AirportOfOrigin = airportST.get("NRT");
        Airport flight7AirportOfDestination = airportST.get("OPO");
        Flight flight7 = new Flight(500, new Date(0, 0, 0, 4, 43, 32), new Date(27, 1, 1995, 17, 45, 30), 100, flight7Airplane,
                flight7AirportOfOrigin, flight7AirportOfDestination);
        flightST.put(flight7.getDate(), flight7);

//        for(Date d : flightST.keys()){
//            System.out.println(flightST.get(d).toString());
//        }

//        removeAirplane(airplaneST, airplaneST.get(0));
//        for (Integer ap : airplaneST.keys()){
//            System.out.println(ap + ": " + airplaneST.get(ap).getName());
//        }

//        System.out.println("AIRLINES");
//        System.out.println("-------------------");
//
//
//        System.out.println();
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
//        PrintInfo.allAirports(airportsSearch(airportST,"Asia"));
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

    /**
     * searches airports of a certain country / continent
     * @param airportST Symbol table that stores all the available airports
     * @param search the continent or country to search for
     * @return returns all the airports that matched the search
     */
    public static ArrayList<Airport> airportsSearch(SeparateChainingHashST<String, Airport> airportST, String search){
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

    /** remove airport
     * airportST
     * remover dos voos ligados a este o aeroporto
     * escrever pa ficheiro log do aeroporto em questao os voos e aeroportos do historico
     * guardar em ficheiro "backup" do aeroporto por onde se pode voltar a cria-lo
     * remover dos avioes estacionados no aeroporto o "code" na classe deles
     */

    /** remove airline
     * airlineST
     * remover todos os avioes ligados a companhia
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
