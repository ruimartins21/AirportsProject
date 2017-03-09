package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

/**
 * Created by ruimartins on 09/03/2017.
 */
public class PrintInfo {
    public PrintInfo() { }

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
            if (airportST.get(a).getCode().contentEquals(code)) {
                System.out.println();
                System.out.format("%8s%3s%8s", "Info of ", airportST.get(a).getCode(), "airport");
                System.out.println();
                System.out.format("%11s", "---");
                System.out.println();
                System.out.printf("Name:\t\t%s\nCode:\t\t%s\nCity:\t\t%s\nCountry:\t%s\nContinent:\t%s\nRating:\t\t%s\n", airportST.get(a).getName(), airportST.get(a).getCode(), airportST.get(a).getCity(), airportST.get(a).getCountry(), airportST.get(a).getContinent(), airportST.get(a).getRating());
                System.out.println();
                System.out.println("Companies:");

//                Falta info sobre o trafego, partidas e chegadas no aeroporto
//                historico daquele aeroporto

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
}
