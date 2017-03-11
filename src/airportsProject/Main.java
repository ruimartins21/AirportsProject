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

//        PrintInfo.allAirports(airportST);
//        PrintInfo.airport(airportST, "NRT");
//        PrintInfo.airplane(airplaneST , 1);

        RedBlackBST<Date, Flight> flightST = new RedBlackBST<>();

        Airplane flight1Airplane = airplaneST.get(0);
        Airport flight1AirportOfOrigin = airportST.get("OPO");
        Airport flight1AirportOfDestination = airportST.get("FRA");
        Flight flight1 = new Flight(2000, new Date(0, 0, 0, 10, 0, 0), new Date(12, 3, 2017, 12, 50, 0), 380, flight1Airplane,
                flight1AirportOfOrigin, flight1AirportOfDestination);
        flightST.put(new Date(12, 3, 2017, 12, 50, 0), flight1);

        Airplane flight2Airplane = airplaneST.get(5);
        Airport flight2AirportOfOrigin = airportST.get("DXB");
        Airport flight2AirportOfDestination = airportST.get("SCL");
        Flight flight2 = new Flight(10000, new Date(1, 0, 0, 10, 0, 0), new Date(2, 1, 2017, 23, 1, 10), 150, flight2Airplane,
                flight2AirportOfOrigin, flight2AirportOfDestination);
        flightST.put(new Date(2, 1, 2017, 23, 1, 10), flight2);

        for(Date d : flightST.keys()){
            System.out.println(flightST.get(d).toString());
        }

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
    }

//    pesquisa avioes de um país	ou	continente ou cidade
    private SeparateChainingHashST<String, Airport> airplanesSearch(SeparateChainingHashST<String, Airport> airportST){


        return airportST;
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
