package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class Main {
    public static void main(String[] args) {

        System.out.println("AIRPORTS");
        System.out.println("-------------------");
        String path = ".//data//airports.txt";

        SeparateChainingHashST<String, Airport> airportST = new SeparateChainingHashST<>();
        ImportFromFile.importAirports(airportST,path);

        System.out.println();
        for (String a : airportST.keys()) {
            System.out.println("\"" + a + "\" -> " + airportST.get(a).getName());
            System.out.println();
        }

        System.out.println("AIRLINES");
        System.out.println("-------------------");
        path = ".//data//airlines.txt";

        SeparateChainingHashST<String, Airline> airlinesST = new SeparateChainingHashST<>();
        ImportFromFile.importAirlines(airlinesST, path);

        System.out.println();
        for (String c : airlinesST.keys()) {
            System.out.println(airlinesST.get(c).getName() + " from " + airlinesST.get(c).getNationality());
            System.out.println();
        }

        System.out.println("PLANES");
        System.out.println("-------------------");
        path = ".//data//airplanes.txt";

        RedBlackBST<Integer, Airplane> airplaneST = new RedBlackBST<>();
        ImportFromFile.importPlanes(airplaneST, airlinesST,  path);

        System.out.println();
        for (Integer p :airplaneST.keys()) {
            System.out.println("id " + p + " -> " + airplaneST.get(p).getName());
            System.out.println("airline: " + airplaneST.get(p).getAirline().getName());
            System.out.println();
        }

        System.out.println("AIRLINES & PLANES");
        System.out.println("-------------------");
        for (String ap : airlinesST.keys()) {
            System.out.println(airlinesST.get(ap).getName() + " has " + airlinesST.get(ap).getNumPlanes() + " planes");
            System.out.println("+++++++++++++++");
            for(Airplane airplane : airlinesST.get(ap).getAirplanes()){
                System.out.println(airplane.toString());
            }

        }
    }
}
