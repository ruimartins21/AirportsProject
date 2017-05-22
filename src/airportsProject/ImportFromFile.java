package airportsProject;

import airportsProject.Exceptions.WrongTypeFileException;
import libs.In;
import libs.RedBlackBST;
import libs.SeparateChainingHashST;
import libs.SymbolEdgeWeightedDigraph;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static airportsProject.Utils.*;

public class ImportFromFile {

    /**
     * Imports from the file the airports
     *
     * @param airportST ST that will populate with the airports read from the file
     * @param path      path to the file that contains the airports
     */
    public static void importAirports(SeparateChainingHashST<String, Airport> airportST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if (i != 0) { // first line of the file is to ignore
                String name = fileContent[0];
                String code = fileContent[1];
                String city = fileContent[2];
                String country = fileContent[3];
                String continent = fileContent[4];
                float rating = Float.parseFloat(fileContent[5]);
                Airport newAirport = new Airport(name, code, city, country, continent, rating);
                airportST.put(code, newAirport);
                log("airportST", "Inserted airport \"" + newAirport.getName() + "\"");
            }
            i++;
        }
    }

    /**
     * Imports from the file the airplanes
     *
     * @param airportST  ST needed to link airplanes to an airport because they need to be "parked" somewhere
     * @param airplaneST ST populated with the airplanes read from the file
     * @param airlineST  ST needed to link an airplane to an airline that owns that airplane
     * @param path       path to the file that contains the airplanes
     */
    public static void importPlanes(SeparateChainingHashST<String, Airport> airportST, RedBlackBST<Integer, Airplane> airplaneST, SeparateChainingHashST<String, Airline> airlineST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if (i != 0) {  // first line of the file is to ignore
                int id = Integer.parseInt(fileContent[0]);
                String model = fileContent[1];
                String name = fileContent[2];
                // first we need to add the plane to the respective airline, that needs to exist already
                // and return the respective airline needed to pass to the plane class
                String airlineName = fileContent[3];
                // searches for the airline existence
                Airline thisPlaneAirline = airlineST.get(airlineName);
                if (thisPlaneAirline == null) {
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
                airportST.get(airportCode).receivePlane(newPlane);  // adds this new plane to the respective airport
                thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
                airplaneST.put(id - 1, newPlane); // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
                log("airplaneST", "Inserted airplane \"" + newPlane.getName() + "\"");
            }
            i++;
        }
    }

    /**
     * Imports from the file the airlines
     *
     * @param airlineST ST populated with the airlines read from the file
     * @param path      path to the file that contains the airlines
     */
    public static void importAirlines(SeparateChainingHashST<String, Airline> airlineST, String path) {
        In in = new In(path);
        int i = 0;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if (i != 0) {  // first line of the file is to ignore
                Airline newAirline = new Airline(fileContent[0], fileContent[1]);
                airlineST.put(newAirline.getName(), newAirline);
                log("airlineST", "Inserted airline \"" + newAirline.getName() + "\"");
            }
            i++;
        }
    }

    /**
     * File holding the previous program opened so changes like additions, editions and removals of any info is stored on it
     * aswell as the flights occurred
     *
     * @param path       path to the file
     * @param airportST  will populate this ST with the airports
     * @param airlineST  will populate this ST with the airlines
     * @param airplaneST will populate this ST with the airplanes
     */
    public static void loadProgram(String path, SeparateChainingHashST<String, Airport> airportST, SeparateChainingHashST<String, Airline> airlineST,
                                   RedBlackBST<Integer, Airplane> airplaneST) throws WrongTypeFileException {
        String sb = "";
        if (path.length() != 0 && path.contains("bin")) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
                // need to read in the same order that it was saved
                setAirports((SeparateChainingHashST<String, Airport>) ois.readObject());
                setAirlines((SeparateChainingHashST<String, Airline>) ois.readObject());
                setAirplanes((RedBlackBST<Integer, Airplane>) ois.readObject());
                setFlights((RedBlackBST<Date, Flight>) ois.readObject());
                setSymbolGraph((SymbolEdgeWeightedDigraph) ois.readObject());
                ois.close();
            } catch (Exception ex) {
                throw new WrongTypeFileException("Wrong type of file selected");
            }
        } else {
            In in;
            try {
                in = new In(path);
            } catch (Exception e) {
                throw new WrongTypeFileException("No program saved with that name");
            }

            int i = 0, j = -1;
            boolean validFile = false;
            while (!in.isEmpty()) {
                String[] fileContent = in.readLine().split(";");
                if (fileContent[0].equals("#")) {
                    if (!validFile) validFile = true; // check if the file is valid from the first line of the file
                    i = 0;
                    j++;
                } else {
                    if (!validFile) break;
                    if (i != 0) {  // first line of the file is to ignore
                        if (j == 0) {   // airports
                            String name = fileContent[0];
                            String code = fileContent[1];
                            String city = fileContent[2];
                            String country = fileContent[3];
                            String continent = fileContent[4];
                            float rating = Float.parseFloat(fileContent[5]);
                            Airport newAirport = new Airport(name, code, city, country, continent, rating);
                            airportST.put(code, newAirport);
                        } else if (j == 1) { // airlines
                            Airline newAirline = new Airline(fileContent[0], fileContent[1]);
                            airlineST.put(newAirline.getName(), newAirline);
                        } else if (j == 2) { // airplanes
                            int id = Integer.parseInt(fileContent[0]);
                            String model = fileContent[1];
                            String name = fileContent[2];
                            // first we need to add the plane to the respective airline, that needs to exist already
                            // and return the respective airline needed to pass to the plane class
                            String airlineName = fileContent[3];
                            // searches for the airline existence
                            Airline thisPlaneAirline = airlineST.get(airlineName);
                            if (thisPlaneAirline == null) {
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
                            airportST.get(airportCode).receivePlane(newPlane);  // adds this new plane to the respective airport
                            thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
                            airplaneST.put(id - 1, newPlane); // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
                        } else if (j == 3) { // flights
                            String[] dateText = fileContent[0].split("/");
                            int day = Integer.parseInt(dateText[0]);
                            int month = Integer.parseInt(dateText[1]);
                            int year = Integer.parseInt(dateText[2]);
                            int hour = Integer.parseInt(dateText[3]);
                            int minute = Integer.parseInt(dateText[4]);
                            int second = Integer.parseInt(dateText[5]);
                            Date flightDate = new Date(day, month, year, hour, minute, second);

                            int passengers = Integer.parseInt(fileContent[1]);
                            int airplaneId = Integer.parseInt(fileContent[2]);
                            Airplane airplane = airplaneST.get(airplaneId - 1);
                            Airport airportOfOrigin = airportST.get(fileContent[3]);
                            Airport airportOfDestination = airportST.get(fileContent[4]);
                            String[] connections = fileContent[5].split("\\|");
                            ArrayList<String> conects = new ArrayList<>();
                            conects.addAll(Arrays.asList(connections));
                            Utils.getInstance().newFlight(null, null, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, 0, conects);
                        } else if (j == 4) { // graph
                            if (i == 1) {
                                // ignores this line, it is the number of weights
                            } else {
                                for (int k = 0; k < fileContent.length; k++)
                                    sb = sb + fileContent[k] + ";";
                                sb = sb + "\n";
                            }
                        }
                    }
                    i++;
                }
            }
            if (j < 3) { // it didn't load the correct information
                throw new WrongTypeFileException("Wrong type of file selected");
            } else { // for the graph, it stores all the info on a string first to send to the function now
                SymbolEdgeWeightedDigraph symbolGraph = new SymbolEdgeWeightedDigraph(sb);
                setSymbolGraph(symbolGraph);
            }
        }
    }

    /**
     * Loads the graph from the backup file that is the one being manipulated in the current program
     * @throws WrongTypeFileException when the file does not correspond to the wanted
     */
    public static void loadGraph() throws WrongTypeFileException {
        String sb = "";
        String path = ".//data//backup.txt";
        In in;
        try {
            in = new In(path);
        } catch (Exception e) {
            throw new WrongTypeFileException("No program saved with that name");
        }

        int i = 0, j = -1;
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            if (fileContent[0].equals("#")) {
                j++;
                i = 0;
            } else {
                if(i != 0){
                    if (j == 4) { // graph
                        if (i == 1) {
                            // ignores this line, it is the number of weights
                        } else {
                            for (String aFileContent : fileContent) sb = sb + aFileContent + ";";
                            sb = sb + "\n";
                        }
                    }

                }
                i++;
            }
        }
        // for the graph, it stores all the info on a string first to send to the function now
        if(sb.length() > 0){
            SymbolEdgeWeightedDigraph symbolGraph = new SymbolEdgeWeightedDigraph(sb);
            setSymbolGraph(symbolGraph);
            setMainGraph(symbolGraph);
        }
    }
}
