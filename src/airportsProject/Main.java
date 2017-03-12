package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
        flightST.put(new Date(7, 3, 2017, 12, 50, 30), flight1);

        Airplane flight2Airplane = airplaneST.get(2);
        Airport flight2AirportOfOrigin = airportST.get("DXB");
        Airport flight2AirportOfDestination = airportST.get("SCL");
        Flight flight2 = new Flight(10000, new Date(1, 0, 0, 10, 0, 0), new Date(8, 4, 2017, 23, 1, 30), 150, flight2Airplane,
                flight2AirportOfOrigin, flight2AirportOfDestination);
        flightST.put(new Date(8, 4, 2017, 23, 1, 30), flight2);

        Airplane flight3Airplane = airplaneST.get(3);
        Airport flight3AirportOfOrigin = airportST.get("CDG");
        Airport flight3AirportOfDestination = airportST.get("LAD");
        Flight flight3 = new Flight(21500, new Date(0, 0, 0, 17, 43, 32), new Date(9, 11, 2017, 23, 1, 30), 200, flight3Airplane,
                flight3AirportOfOrigin, flight3AirportOfDestination);
        flightST.put(new Date(9, 11, 2017, 23, 1, 30), flight3);

        Airplane flight4Airplane = airplaneST.get(4);
        Airport flight4AirportOfOrigin = airportST.get("LAD");
        Airport flight4AirportOfDestination = airportST.get("OPO");
        Flight flight4 = new Flight(500, new Date(0, 0, 0, 7, 43, 32), new Date(10, 9, 2017, 23, 1, 30), 100, flight4Airplane,
                flight4AirportOfOrigin, flight4AirportOfDestination);
        flightST.put(new Date(10, 9, 2017, 23, 1, 30), flight4);

        Airplane flight6Airplane = airplaneST.get(5);
        Airport flight6AirportOfOrigin = airportST.get("OPO");
        Airport flight6AirportOfDestination = airportST.get("NRT");
        Flight flight6 = new Flight(500, new Date(0, 0, 0, 4, 43, 32), new Date(12, 9, 2017, 23, 1, 30), 100, flight6Airplane,
                flight6AirportOfOrigin, flight6AirportOfDestination);
        flightST.put(new Date(11, 9, 2016, 23, 1, 30), flight6);

//        viagens ja feitas

        Airplane flight5Airplane = airplaneST.get(5); // key 5 = id 6
        Airport flight5AirportOfOrigin = airportST.get("OPO");
        Airport flight5AirportOfDestination = airportST.get("NRT");
        Flight flight5 = new Flight(500, new Date(0, 0, 0, 4, 43, 32), new Date(11, 9, 2016, 23, 1, 30), 100, flight5Airplane,
                flight5AirportOfOrigin, flight5AirportOfDestination);
        flightST.put(new Date(11, 9, 2016, 23, 1, 30), flight5);

        Airplane flight7Airplane = airplaneST.get(5);
        Airport flight7AirportOfOrigin = airportST.get("NRT");
        Airport flight7AirportOfDestination = airportST.get("OPO");
        Flight flight7 = new Flight(500, new Date(0, 0, 0, 4, 43, 32), new Date(27, 1, 1995, 17, 45, 30), 100, flight7Airplane,
                flight7AirportOfOrigin, flight7AirportOfDestination);
        flightST.put(new Date(27, 1, 1995, 17, 45, 30), flight7);

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

        PrintInfo.allAirports(airportST);
        PrintInfo.airport(airportST, "NRT");
        PrintInfo.airplane(airplaneST , 1);
        PrintInfo.allAirports(airportsSearch(airportST,"Asia"));
        PrintInfo.flightsBetweenTimes(flightST,new Date(1, 1, 2017, 23, 59, 10),new Date(21, 9, 2017, 21, 21, 21));
        PrintInfo.flightsThisAirport(flightST,"OPO");
        PrintInfo.allTravelsPlane(airplaneST,6);
        PrintInfo.allAirplanes(airplaneST);


    }

//    pesquisa aeroportos de um país	ou	continente ou cidade
    public static SeparateChainingHashST<String, Airport> airportsSearch(SeparateChainingHashST<String, Airport> airportST, String search){
        SeparateChainingHashST<String, Airport> airportSearch = new SeparateChainingHashST<>();
        for (String a: airportST.keys()) {
            if(airportST.get(a).getContinent().equals(search) || airportST.get(a).getCity().equals(search)){
                airportSearch.put(airportST.get(a).getCode(),airportST.get(a));
            }
        }
        return airportSearch;
    }




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

    private static void removeAirplane(RedBlackBST<Integer, Airplane> airplaneST , Airplane plane){
        Airline airline = plane.getAirline();
        airline.removePlane(plane);
        airplaneST.put(plane.getId()-1, null); // ids on the ST starts from 0 and airplanes ids from 1
        log("Airline \"" + airline.getName() + "\"", "Removed airplane \"" + plane.getName() + "\"");
        log("AirplaneST", "Removed airplane \"" + plane.getName() + "\"");
    }
}
