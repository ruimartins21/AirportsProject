package airportsProject;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

import java.io.File;

public class ImportFromFile {
    private String path;

    public ImportFromFile(String path) {
        setPath(path);
    }

    private void setPath(String path) {
        if (new File(path).exists()) {
            this.path = path;
        }else{
            throw new IllegalArgumentException("Path \"" + path + "\" doesn't exist");
        }
    }

    public static void importAirports(String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
//            System.out.println(Arrays.toString(fileContent));
            // ST nao ordenada
            if(i != 0){ // first line of the file is to ignore
                String name = fileContent[0];
                String code = fileContent[1];
                String city = fileContent[2];
                String country = fileContent[3];
                String continent = fileContent[4];
                float rating = Float.parseFloat(fileContent[5]);
                Airport newAirport = new Airport(name, code, city, country, continent, rating);
                System.out.println(newAirport.toString());
            }
            i++;
        }
    }

    public static void importPlanes(RedBlackBST<Integer, Airplane> airplaneST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
//            System.out.println(Arrays.toString(fileContent));
            // pegar na companhia aerea e noutra funcao inserir a companhia na respetiva table, verificar se ja existe
            // ST ordenada por id
            if(i != 0){  // first line of the file is to ignore
                int id = Integer.parseInt(fileContent[0]);
                String model = fileContent[1];
                String name = fileContent[2];
                // first we need to add the plane to the respective airline, that needs to exist already
                // and return the respective airline needed to pass to the plane class
                Airline thisPlaneAirline = null;
                float cruiseSpeed = Float.parseFloat(fileContent[4]);
                float cruiseAltitude = Float.parseFloat(fileContent[5]);
                float maxRange = Float.parseFloat(fileContent[6]);
                String airportCode = fileContent[7];
                int passengersCapacity = Integer.parseInt(fileContent[8]);
                int fuelCapacity = Integer.parseInt(fileContent[9]);
                Airplane newPlane = new Airplane(id, model, name, cruiseSpeed, cruiseAltitude, maxRange, airportCode,
                        passengersCapacity, fuelCapacity, thisPlaneAirline);
                System.out.println(newPlane.toString());
                airplaneST.put(id, newPlane);
            }
            i++;
        }
    }

    public static void importAirlines(String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
//            System.out.println(Arrays.toString(fileContent));
            if(i != 0){  // first line of the file is to ignore
                Airline newAirline = new Airline(fileContent[0], fileContent[1]);
                System.out.println(newAirline.toString());
            }
            i++;
        }
    }
}
