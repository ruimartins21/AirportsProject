package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class Main {
    public static void main(String[] args) {

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
        PrintInfo.airplane(airplaneST , 1);



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
    public  SeparateChainingHashST<String, Airport> airplanesSearch(SeparateChainingHashST<String, Airport> airportST){


        return airportST;
    }
}
