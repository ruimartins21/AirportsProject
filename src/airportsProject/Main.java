package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

import java.util.Hashtable;

public class Main {
    public static void main(String[] args) {

        System.out.println("AIRPORTS");
        System.out.println("-------------------");
        String path = ".//data//airports.txt";

        SeparateChainingHashST<String, Airport> airportST = new SeparateChainingHashST<>();
        ImportFromFile.importAirports(airportST,path);

        System.out.println();
        for (String p : airportST.keys()) {
            System.out.println("id " + p + " " + airportST.get(p).getName());

            System.out.println();


        }



        System.out.println("PLANES");
        System.out.println("-------------------");
        path = ".//data//airplanes.txt";


        RedBlackBST<Integer, Airplane> airplaneST = new RedBlackBST<>();
        ImportFromFile.importPlanes(airplaneST, path);

            System.out.println();
            for (Integer p :airplaneST.keys()) {
                System.out.println("id " + p + " " + airplaneST.get(p).getName());

                System.out.println();


            }



        System.out.println("AIRLINES");
        System.out.println("-------------------");
        path = ".//data//airlines.txt";
        ImportFromFile.importAirlines(path);

    }
}
