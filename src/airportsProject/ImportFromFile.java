package airportsProject;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class ImportFromFile {

    public static void importAirports(SeparateChainingHashST<String, Airport> airportST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if(i != 0){ // first line of the file is to ignore
                String name = fileContent[0];
                String code = fileContent[1];
                String city = fileContent[2];
                String country = fileContent[3];
                String continent = fileContent[4];
                float rating = Float.parseFloat(fileContent[5]);
                Airport newAirport = new Airport(name, code, city, country, continent, rating);
                airportST.put(code, newAirport);
//                System.out.println(newAirport.toString());
            }
            i++;
        }
    }

    public static void importPlanes(RedBlackBST<Integer, Airplane> airplaneST, SeparateChainingHashST<String, Airline> airlineST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if(i != 0){  // first line of the file is to ignore
                int id = Integer.parseInt(fileContent[0]);
                String model = fileContent[1];
                String name = fileContent[2];
                // first we need to add the plane to the respective airline, that needs to exist already
                // and return the respective airline needed to pass to the plane class
                String airlineName = fileContent[3];
                // searches for the airline existence
                Airline thisPlaneAirline = airlineST.get(airlineName);
                if(thisPlaneAirline == null){
                    throw new IllegalArgumentException("argument to get() is null");
                }
                float cruiseSpeed = Float.parseFloat(fileContent[4]);
                float cruiseAltitude = Float.parseFloat(fileContent[5]);
                float maxRange = Float.parseFloat(fileContent[6]);
                String airportCode = fileContent[7];
                int passengersCapacity = Integer.parseInt(fileContent[8]);
                int fuelCapacity = Integer.parseInt(fileContent[9]);
                Airplane newPlane = new Airplane(id, model, name, cruiseSpeed, cruiseAltitude, maxRange, airportCode,
                        passengersCapacity, fuelCapacity, thisPlaneAirline);
                thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
                airplaneST.put(id, newPlane);
//                System.out.println(newPlane.toString());
            }
            i++;
        }
    }

    public static void importAirlines(SeparateChainingHashST<String, Airline> airlineST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if(i != 0){  // first line of the file is to ignore
                Airline newAirline = new Airline(fileContent[0], fileContent[1]);
                airlineST.put(newAirline.getName(), newAirline);
//                System.out.println(newAirline.toString());
            }
            i++;
        }
    }
}
