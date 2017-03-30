package airportsProject;

import libs.RedBlackBST;
import libs.SeparateChainingHashST;

public class PrintInfo {
//    public PrintInfo() { }

    public static void allAirports(SeparateChainingHashST<String, Airport> airportST) {
        System.out.println();
        System.out.format("%92s", "Airports Global Info\n");
        System.out.println();
        System.out.format("%52s%10s%22s%16s%20s%16s%20s", "Name", "Code", "City", "Country","Continent", "Rating", "Current Traffic");
        System.out.println();
        System.out.format("%52s%10s%22s%16s%20s%16s%20s", "----", "----", "----", "-------","---------", "------", "---------------");
        System.out.println();

        for (String a : airportST.keys()) {
            System.out.format("%52s%10s%22s%16s%20s%16s%20s", airportST.get(a).getName(), airportST.get(a).getCode(), airportST.get(a).getCity(), airportST.get(a).getCountry(), airportST.get(a).getContinent(), airportST.get(a).getRating(),21 );
            System.out.println();
        }
    }

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

    public static void airplane(RedBlackBST<Integer, Airplane> airplaneST , int id) {
        for (Integer a : airplaneST.keys()) {
            if(airplaneST.get(a).getId() == id ){
                System.out.println();
                System.out.format("%8s%3s%8s", "Info of [", airplaneST.get(a).getName(), "] airplane");
                System.out.printf("\n\n");
                System.out.printf("Id:\t\t\t\t\t\t%s\nModel:\t\t\t\t\t%s\nName:\t\t\t\t\t%s\nAirline:\t\t\t\t%s  -  %s\nCruise Speed:\t\t\t%s\nCruise Altitude:\t\t%s\n" +
                                "Max Range:\t\t\t\t%s\n" + "Airport Code:\t\t\t%s\nPassengers Capacity:\t%s\nFuel Capacity:\t\t\t%s\n",
                                airplaneST.get(a).getId(), airplaneST.get(a).getModel(), airplaneST.get(a).getName(), airplaneST.get(a).getAirline().getName(),
                                airplaneST.get(a).getAirline().getNationality(), airplaneST.get(a).getCruiseSpeed(), airplaneST.get(a).getCruiseAltitude(),
                                airplaneST.get(a).getMaxRange(), airplaneST.get(a).getAirportCode(), airplaneST.get(a).getPassengersCapacity(),
                                airplaneST.get(a).getFuelCapacity() );
                System.out.println();

            }
        }
    }

    public static void allAirplanes(RedBlackBST<Integer, Airplane> airplaneST ) {
        for (Integer a : airplaneST.keys()) {
                System.out.println();
                System.out.format("%8s%3s%8s", "Info of [", airplaneST.get(a).getName(), "] airplane");
                System.out.printf("\n\n");
                System.out.printf("Id:\t\t\t\t\t\t%s\nModel:\t\t\t\t\t%s\nName:\t\t\t\t\t%s\nAirline:\t\t\t\t%s  -  %s\nCruise Speed:\t\t\t%s\nCruise Altitude:\t\t%s\n" +
                                "Max Range:\t\t\t\t%s\n" + "Airport Code:\t\t\t%s\nPassengers Capacity:\t%s\nFuel Capacity:\t\t\t%s\n",
                        airplaneST.get(a).getId(), airplaneST.get(a).getModel(), airplaneST.get(a).getName(), airplaneST.get(a).getAirline().getName(),
                        airplaneST.get(a).getAirline().getNationality(), airplaneST.get(a).getCruiseSpeed(), airplaneST.get(a).getCruiseAltitude(),
                        airplaneST.get(a).getMaxRange(), airplaneST.get(a).getAirportCode(), airplaneST.get(a).getPassengersCapacity(),
                        airplaneST.get(a).getFuelCapacity() );
                System.out.println();


        }
    }

    // Imprimir todas as viagens realizadas entre um determinado período de tempo
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

            if (flightST.get(f).getDate().compareTo(start) == 1 && flightST.get(f).getDate().compareTo(end) == -1 ) {
                System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", flightST.get(f).getAirportOfOrigin().getCode(), flightST.get(f).getAirportOfDestination().getCode(),
                        flightST.get(f).getDate(), flightST.get(f).getDuration(), flightST.get(f).getDistance(), flightST.get(f).getCosts(),flightST.get(f).getPassengers(),
                        flightST.get(f).getAirplane().getName());
                System.out.println();
            }
        }

    }

    //    Imprimir todas as viagens com origem/destino num determinado aeroporto;
    public static void flightsOfThisAirport(RedBlackBST<Date, Flight> flightST, String airportCode){
        System.out.printf("\n\n");
        System.out.format("%90s%3s", "Travels of ", airportCode);
        System.out.printf("\n\n");
        System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", "Airport Of Origin", "Airport Of Destination", "Date", "Duration",
                "Distance","Costs", "Nº Passengers", "Airplane Name");
        System.out.println();
        System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s",    "----------------",  "---------------------",  "----", "--------",
                "--------", "-----", "-------------", "-------------");
        System.out.printf("\n\n");
        for (Date f : flightST.keys()) {
            if (flightST.get(f).getAirportOfOrigin().getCode().compareTo(airportCode) == 0 || flightST.get(f).getAirportOfDestination().getCode().compareTo(airportCode) == 0)  {
                System.out.format("%20s%25s%26s%26s%11s%8s%15s%35s", flightST.get(f).getAirportOfOrigin().getCode(), flightST.get(f).getAirportOfDestination().getCode(),
                        flightST.get(f).getDate(), flightST.get(f).getDuration(), flightST.get(f).getDistance(), flightST.get(f).getCosts(),flightST.get(f).getPassengers(),
                        flightST.get(f).getAirplane().getName());
                System.out.println();
            }

        }
    }

//    Imprimir todas as viagens realizadas por um determinado avião
    public static void allTravelsPlane(RedBlackBST<Integer, Airplane> airplaneST, int id){
        id -= 1; // Redblack table stores keys from 0 but the airplanes id's starts from 1
        System.out.printf("\n\n");
        System.out.format("%90s%3s", "Travels made by Airplane: [" + airplaneST.get(id).getId() + "] ", airplaneST.get(id).getName());
        System.out.printf("\n\n");
        System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s", "Airport Of Origin", "Airport Of Destination", "Date", "Duration",
                "Distance","Costs", "Nº Passengers", "Airplane Name");
        System.out.println();
        System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s",    "-----------------",  "---------------------",  "----", "--------",
                "--------", "-----", "-------------", "-------------");
        System.out.printf("\n\n");

        for (Integer a : airplaneST.keys()) {
            if (airplaneST.get(a).getId() == id+1) {
                for (Date d: airplaneST.get(a).getAirplaneFlights().keys()) {
                    System.out.format("%20s%25s%26s%26s%11s%8s%16s%35s", airplaneST.get(a).getAirplaneFlights().get(d).getAirportOfOrigin().getCode(),
                            airplaneST.get(a).getAirplaneFlights().get(d).getAirportOfDestination().getCode(),airplaneST.get(a).getAirplaneFlights().get(d).getDate(),
                            airplaneST.get(a).getAirplaneFlights().get(d).getDuration(), airplaneST.get(a).getAirplaneFlights().get(d).getDistance(),
                            airplaneST.get(a).getAirplaneFlights().get(d).getCosts(),airplaneST.get(a).getAirplaneFlights().get(d).getPassengers(),
                            airplaneST.get(a).getAirplaneFlights().get(d).getAirplane().getName());
                    System.out.println();
                }
            }
        }

    }
}
