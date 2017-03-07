package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;

public class Main {
    public static void main(String[] args) {

        System.out.println("AIRPORTS");
        System.out.println("-------------------");
        String path = ".//data//airports.txt";
        ImportFromFile.importAirports(path);

        System.out.println("PLANES");
        System.out.println("-------------------");
        path = ".//data//airplanes.txt";

//        public static void exercicioConsumirDados() {

        RedBlackBST<Integer, Airplane> airplaneST = new RedBlackBST<>();;
        ImportFromFile.importPlanes(airplaneST, path);

            System.out.println();
            for (Integer p :airplaneST.keys()) {
                System.out.println("id " + p + " " + airplaneST.get(p).getName());

                System.out.println();


            }
//        }


        System.out.println("AIRLINES");
        System.out.println("-------------------");
        path = ".//data//airlines.txt";
        ImportFromFile.importAirlines(path);

    }
}
