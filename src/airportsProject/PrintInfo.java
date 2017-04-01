package airportsProject;

import libs.RedBlackBST;
import libs.SeparateChainingHashST;

public class PrintInfo {
    /**
     * Prints the complete ST of the airports
     * @param airportST the ST with all the airports
     */
    public static void allAirports(SeparateChainingHashST<String, Airport> airportST) {
        System.out.println();
        System.out.format("%92s", "Airports Global Info\n");
        System.out.println();
        System.out.format("%52s%10s%22s%16s%20s%16s", "Name", "Code", "City", "Country","Continent", "Rating");
        System.out.println();
        System.out.format("%52s%10s%22s%16s%20s%16s", "----", "----", "----", "-------","---------", "------");
        System.out.println();
        for (String a : airportST.keys()) {
            Airport airport = airportST.get(a);
            System.out.format("%52s%10s%22s%16s%20s%16s", airport.getName(), a, airport.getCity(), airport.getCountry(),
                    airport.getContinent(), airport.getRating());
            System.out.println();
        }
    }

    /**
     * Prints a certain airport
     * @param airportST ST with all the airports
     * @param code code of the airport to show
     */
    public static void airport(SeparateChainingHashST<String, Airport> airportST, String code) {
        for (String a : airportST.keys()) {
            if (airportST.get(a).getCode().compareTo(code) == 0) {
                Airport airport = airportST.get(a);
                System.out.println();
                System.out.format("%8s%3s%8s", "Info of ", airport.getCode(), "airport");
                System.out.println();
                System.out.format("%11s", "---");
                System.out.println();
                System.out.printf("Name:\t\t%s\nCode:\t\t%s\nCity:\t\t%s\nCountry:\t%s\nContinent:\t%s\nRating:\t\t%s\n", airport.getName(), airport.getCode(), airport.getCity(), airport.getCountry(), airport.getContinent(), airport.getRating());
                System.out.println();
                System.out.println("Parked Planes:");
                for(Integer k : airport.getAirplanes().keys()){
                    System.out.println("-> \"" + airport.getAirplanes().get(k).getName() + "\" from \"" +
                            airport.getAirplanes().get(k).getAirline().getName() + "\" airline");
                }
                System.out.println();
                System.out.println("Flights leaving the airport:");
                for(Date d : airport.getFlights().keys()){
                    if(airport.getFlights().get(d).getAirportOfOrigin().equals(airport)) {
                        System.out.println("-> Plane \"" + airport.getFlights().get(d).getAirplane().getName() + "\" leaving to \"" +
                                airport.getFlights().get(d).getAirportOfDestination().getName() + "\" airport at " + airport.getFlights().get(d).getDate());
                    }
                }
                System.out.println();
                System.out.println("Flights arriving the airport:");
                for(Date d : airport.getFlights().keys()){
                    if(!airport.getFlights().get(d).getAirportOfOrigin().equals(airport)) {
                        System.out.println("-> Plane \"" + airport.getFlights().get(d).getAirplane().getName() + "\" arriving from \"" +
                                airport.getFlights().get(d).getAirportOfOrigin().getName() + "\" airport at " + airport.getFlights().get(d).getDate());
                    }
                }
            }
        }
    }

    /**
     * Prints a certain airplane
     * @param airplaneST ST with all the airplanes
     * @param id id of the plane to show
     */
    public static void airplane(RedBlackBST<Integer, Airplane> airplaneST , int id) {
        for (Integer a : airplaneST.keys()) {
            if(airplaneST.get(a).getId() == id){
                Airplane airplane = airplaneST.get(a);
                System.out.println();
                System.out.format("%8s%3s%8s", "Info of [", airplane.getName(), "] airplane");
                System.out.printf("\n\n");
                System.out.printf("Id:\t\t\t\t\t\t%s\nModel:\t\t\t\t\t%s\nName:\t\t\t\t\t%s\nAirline:\t\t\t\t%s  -  %s\nCruise Speed:\t\t\t%s\nCruise Altitude:\t\t%s\n" +
                                "Max Range:\t\t\t\t%s\n" + "Airport Code:\t\t\t%s\nPassengers Capacity:\t%s\nFuel Capacity:\t\t\t%s\n",
                        airplane.getId(), airplane.getModel(), airplane.getName(), airplane.getAirline().getName(),
                        airplane.getAirline().getNationality(), airplane.getCruiseSpeed(), airplane.getCruiseAltitude(), airplane.getMaxRange(),
                        airplane.getAirportCode(), airplane.getPassengersCapacity(), airplane.getFuelCapacity() );
                System.out.println();

            }
        }
    }

    /**
     * Prints all the existing airplanes
     * @param airplaneST ST with all the airplanes
     */
    public static void allAirplanes(RedBlackBST<Integer, Airplane> airplaneST ) {
        for (Integer a : airplaneST.keys()) {
            Airplane airplane = airplaneST.get(a);
            System.out.println();
            System.out.format("%8s%3s%8s", "Info of [", airplane.getName(), "] airplane");
            System.out.printf("\n\n");
            System.out.printf("Id:\t\t\t\t\t\t%s\nModel:\t\t\t\t\t%s\nName:\t\t\t\t\t%s\nAirline:\t\t\t\t%s  -  %s\nCruise Speed:\t\t\t%s\nCruise Altitude:\t\t%s\n" +
                            "Max Range:\t\t\t\t%s\n" + "Airport Code:\t\t\t%s\nPassengers Capacity:\t%s\nFuel Capacity:\t\t\t%s\n",
                    airplane.getId(), airplane.getModel(), airplane.getName(), airplane.getAirline().getName(),
                    airplane.getAirline().getNationality(), airplane.getCruiseSpeed(), airplane.getCruiseAltitude(),
                    airplane.getMaxRange(), airplane.getAirportCode(), airplane.getPassengersCapacity(), airplane.getFuelCapacity() );
            System.out.println();
        }
    }

    /**
     * Prints all the flights between a period of time
     * @param flightST ST of all the existing flights
     * @param start starting date
     * @param end ending date
     */
    public static void flightsBetweenTimes(RedBlackBST<Date, Flight> flightST, Date start, Date end){
        System.out.printf("\n\n");
        System.out.format("%75s%3s%3s%8s", "Travels between ", start, " and ", end);
        System.out.printf("\n\n");
        System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", "Airport Of Origin", "Airport Of Destination", "Date", "Duration",
                "Distance","Costs", "Nº Passengers", "Airplane Name");
        System.out.println();
        System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s",    "----------------",  "---------------------",  "----", "--------",
                "--------", "-----", "-------------", "-------------");
        System.out.printf("\n\n");
        for (Date f : flightST.keys()) {
            Flight flight = flightST.get(f);
            if (flight.getDate().compareTo(start) == 1 && flight.getDate().compareTo(end) == -1 ) {
                System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", flight.getAirportOfOrigin().getCode(), flight.getAirportOfDestination().getCode(),
                        flight.getDate(), flight.getDuration(), flight.getDistance(), flight.getCosts(),flight.getPassengers(),
                        flight.getAirplane().getName());
                System.out.println();
            }
        }

    }

    /**
     * Prints all the flights that passed, are passing or will pass through a certain airport
     * @param airport airport to search
     */
    public static void flightsOfThisAirport(Airport airport){
        System.out.printf("\n\n");
        System.out.format("%70s%3s", "Travels of ", airport.getCode());
        System.out.printf("\n\n");
        System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", "Airport Of Origin", "Airport Of Destination", "Date", "Duration",
                "Distance","Costs", "Nº Passengers", "Airplane Name");
        System.out.println();
        System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s",    "----------------",  "---------------------",  "----", "--------",
                "--------", "-----", "-------------", "-------------");
        System.out.printf("\n\n");
        RedBlackBST<Date, Flight> airportFlights = airport.getFlights();
        for (Date f : airportFlights.keys()) {
            Flight flight = airportFlights.get(f);
            System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", flight.getAirportOfOrigin().getCode(), flight.getAirportOfDestination().getCode(),
                    flight.getDate(), flight.getDuration(), flight.getDistance(), flight.getCosts(),flight.getPassengers(), flight.getAirplane().getName());
            System.out.println();
        }
    }

    /**
     * Prints all the flights done by a certain airplane
     * @param airplane the airplane from which to show its flights
     */
    public static void allTravelsPlane(Airplane airplane){
        System.out.printf("\n\n");
        System.out.format("%90s%3s", "Travels made by Airplane: [" + airplane.getId() + "] ", airplane.getName());
        System.out.printf("\n\n");
        System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s", "Airport Of Origin", "Airport Of Destination", "Date", "Duration",
                "Distance","Costs", "Nº Passengers", "Airplane Name");
        System.out.println();
        System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s",    "-----------------",  "---------------------",  "----", "--------",
                "--------", "-----", "-------------", "-------------");
        System.out.printf("\n\n");
        RedBlackBST<Date, Flight> airplaneFlights = airplane.getAirplaneFlights();
        for (Date d : airplaneFlights.keys()) {
            Flight flight = airplaneFlights.get(d);
            System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s", flight.getAirportOfOrigin().getCode(),
                    flight.getAirportOfDestination().getCode(),flight.getDate(), flight.getDuration(), flight.getDistance(),
                    flight.getCosts(),flight.getPassengers(), flight.getAirplane().getName());
            System.out.println();
        }
    }

    /**
     * Gets the latest flight of a given airplane
     * @param airplane is the airplane from which to get the flight
     */
    public static void latestFlightOfAirplane(Airplane airplane){
        System.out.printf("\n\n");
        System.out.format("%70s%3s", "Last travel made by Airplane: [" + airplane.getId() + "] ", airplane.getName());
        System.out.printf("\n\n");
        System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s", "Airport Of Origin", "Airport Of Destination", "Date", "Duration",
                "Distance", "Costs", "Nº Passengers", "Airplane Name");
        System.out.println();
        System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s",    "-----------------",  "---------------------",  "----", "--------",
                "--------", "-----", "-------------", "-------------");
        System.out.printf("\n\n");
        Flight latestFlight = airplane.getLatestFlight();
        if(latestFlight != null){
            System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s", latestFlight.getAirportOfOrigin().getCode(),
                    latestFlight.getAirportOfDestination().getCode(),latestFlight.getDate(), latestFlight.getDuration(), latestFlight.getDistance(),
                    latestFlight.getCosts(), latestFlight.getPassengers(), latestFlight.getAirplane().getName());
            System.out.println();
        }
    }
}
