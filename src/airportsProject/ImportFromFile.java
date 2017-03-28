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
                Main.log("airportST", "Inserted airport \"" + newAirport.getName() + "\"");
            }
            i++;
        }
    }

    public static void importPlanes(SeparateChainingHashST<String, Airport> airportST, RedBlackBST<Integer, Airplane> airplaneST, SeparateChainingHashST<String, Airline> airlineST, String path) {
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
                airportST.get(airportCode).receivePlane(newPlane);  // adds this new plane to the respective airport
                thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
                airplaneST.put(id-1, newPlane); // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
                Main.log("airplaneST", "Inserted airplane \"" + newPlane.getName() + "\"");
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
                Main.log("airlineST", "Inserted airline \"" + newAirline.getName() + "\"");
            }
            i++;
        }
    }

    public static boolean currentProgram( String path,SeparateChainingHashST<String, Airport> airportST,SeparateChainingHashST<String, Airline> airlineST,
                                          RedBlackBST<Integer, Airplane> airplaneST, RedBlackBST<Date, Flight> flightST) {
        In in = new In();
        try{
            in = new In(path);
        }catch (Exception e){
            System.out.println("! No previous program saved !");
        }

        int i = 0,j=0;
        boolean hasContent = false;
        while (!in.isEmpty()) {
            hasContent = true;
            String[] fileContent = in.readLine().split(";");
            if(fileContent[0].equals("#")){
                i = 0;
                j++;
            }else {
                if(i != 0) {  // first line of the file is to ignore
                    if(j==0){   // airports
                        String name = fileContent[0];
                        String code = fileContent[1];
                        String city = fileContent[2];
                        String country = fileContent[3];
                        String continent = fileContent[4];
                        float rating = Float.parseFloat(fileContent[5]);
                        Airport newAirport = new Airport(name, code, city, country, continent, rating);
                        airportST.put(code, newAirport);
                    }else if(j==1){ // airlines
                        Airline newAirline = new Airline(fileContent[0], fileContent[1]);
                        airlineST.put(newAirline.getName(), newAirline);
                    }else if(j==2){ // airplanes
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
                        airportST.get(airportCode).receivePlane(newPlane);  // adds this new plane to the respective airport
                        thisPlaneAirline.addPlane(newPlane); // adds this new plane to the respective airline
                        airplaneST.put(id-1, newPlane); // keys on the ST starts with 0 and ids of the planes starts with 1 so "id-1" for the keys
                    }else if(j==3){ // flights
                        float distance = Float.parseFloat(fileContent[0]);
                        float costs = Float.parseFloat(fileContent[1]);

                        String[] durationText = fileContent[2].split("/");
                        int durationDay = Integer.parseInt(durationText[0]);
                        int durationMonth = Integer.parseInt(durationText[1]);
                        int durationYear = Integer.parseInt(durationText[2]);
                        int durationHour = Integer.parseInt(durationText[3]);
                        int durationMinute = Integer.parseInt(durationText[4]);
                        int durationSecond = Integer.parseInt(durationText[5]);
                        Date duration = new Date(durationDay,durationMonth,durationYear,durationHour,durationMinute,durationSecond);

                        String[] dateText = fileContent[3].split("/");
                        int day = Integer.parseInt(dateText[0]);
                        int month = Integer.parseInt(dateText[1]);
                        int year = Integer.parseInt(dateText[2]);
                        int hour = Integer.parseInt(dateText[3]);
                        int minute = Integer.parseInt(dateText[4]);
                        int second = Integer.parseInt(dateText[5]);
                        Date flightDate = new Date(day,month,year,hour,minute,second);

                        int passengers = Integer.parseInt(fileContent[4]);
                        int airplaneId = Integer.parseInt(fileContent[5]);
                        Airplane airplane = airplaneST.get(airplaneId);
                        Airport airportOfOrigin = airportST.get(fileContent[6]);
                        Airport airportOfDestination = airportST.get(fileContent[7]);

                        Main.newFlight(distance, duration, flightDate, passengers, airplane, airportOfOrigin, airportOfDestination, flightST);
                    }
                }
                i++;
            }
        }
        return hasContent;
    }
}
