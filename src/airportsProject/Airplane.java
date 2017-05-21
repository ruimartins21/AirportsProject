package airportsProject;

import libs.RedBlackBST;

import java.io.Serializable;

import static airportsProject.Utils.euroValue;
import static airportsProject.Utils.extraValue;
import static airportsProject.Utils.windCost;

/**
 * The airplane class represents an airplane object with its characteristics and its connections (to an airport, to flights)
 * Aswell as the usual methods that provides (getters and setters), the class calculates the airplane cost for a specific
 * connection of a flight and it provides information on the flights already done, or still to be done
 */
public class Airplane implements Serializable {

    private int id;
    private String model;
    private String name;
    private float cruiseSpeed;
    private float cruiseAltitude;
    private float maxRange;
    private String airportCode;
    private int passengersCapacity;
    private int fuelCapacity;
    private RedBlackBST<Date, Flight> airplaneFlights = new RedBlackBST<>();
    private Airline airline;

    public Airplane(int id, String model, String name, float cruiseSpeed, float cruiseAltitude, float maxRange,
                    String airportCode, int passengersCapacity, int fuelCapacity, Airline airline) {
        this.id = id;
        this.model = model;
        this.name = name;
        this.cruiseSpeed = cruiseSpeed;
        this.cruiseAltitude = cruiseAltitude;
        this.maxRange = maxRange;
        this.airportCode = airportCode;
        this.passengersCapacity = passengersCapacity;
        this.fuelCapacity = fuelCapacity;
        this.airline = airline;
    }

    public int getId() { return id; }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public float getCruiseSpeed() {
        return cruiseSpeed;
    }

    public RedBlackBST<Date, Flight> getAirplaneFlights() {
        return airplaneFlights;
    }

    public float getCruiseAltitude() {
        return cruiseAltitude;
    }

    public float getMaxRange() {
        return maxRange;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public int getPassengersCapacity() {
        return passengersCapacity;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setModel(String model){this.model = model;}

    public void setName(String name){this.name = name;}

    public void setCruiseSpeed(Float cruiseSpeed){this.cruiseSpeed = cruiseSpeed;}

    public void setCruiseAltitude(Float cruiseAltitude){this.cruiseAltitude = cruiseAltitude;}

    public void setMaxRange(Float maxRange){this.maxRange = maxRange;}

    public void setPassengersCapacity(int passengersCapacity){this.passengersCapacity = passengersCapacity;}

    public void setFuelCapacity(int fuelCapacity){this.fuelCapacity = fuelCapacity;}

    /**
     * Gets the latest flight
     * this means, the latest flight occurred and not a flight still schedule to happen
     * Compares to the current date on the RedBlack the highest key BEFORE the key given (current date): floor function
     * @return returns that flight
     */
    public Flight getLatestFlight() {
        Date today = new Date();
        if(this.airplaneFlights.size() > 0)
            if(this.airplaneFlights.floor(today) != null)
                return this.airplaneFlights.get(this.airplaneFlights.floor(today));
        return null;
    }

    /**
     * Adds a flight that the airplane will do
     * @param currentFlight the flight to add
     */
    public void setAirplaneFlight(Flight currentFlight) {
        this.airplaneFlights.put(currentFlight.getDate(), currentFlight);
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline){
        this.airline = airline;
    }

    /**
     * Cost in fuel litres for the airplane considering various factors like
     * -> distance of the flight
     * -> wind speed of the current connection
     * -> altitude or aereal tunnel of the connection
     * with this weights in consideration, it will calculate:
     * if the altitude of the connection is higher than the cruise altitude of the airplane the cost will be higher
     * than the cost if it travelled in its cruise altitude and the same happens if the altitude is lower, it will cost more.
     * the wind speed will affect aswell the cost in the way that if the wind is against the plane will require more power for it to push
     * therefore will add to the fuel cost, contrary to that, if the wind is in favor it will help the plane and it can spend less fuel
     * @param connection is the current connection to calculate the cost
     * @return returns the cost in litres for the connection
     */

    public double getAirplaneCost(Connection connection) {
        if(connection.weight() > this.maxRange){
            return Double.POSITIVE_INFINITY;
        }

        double consum_extra = 0.0, l_at_1000 = 0.0, liters, altitudeDiference;

        altitudeDiference = this.cruiseAltitude - connection.getAltitude(); // difference between airplane cruise altitude and the connection altitude

        if(altitudeDiference != 0)
            consum_extra =  extraValue *  Math.abs(altitudeDiference) / 1000;

        consum_extra = consum_extra - (windCost+connection.getWindSpeed());

        l_at_1000 += consum_extra;

        liters = connection.weight()* l_at_1000/1000;

        return Math.round(liters*euroValue*100)/100;
    }


    public double getFlightDuration(Connection connection){
        return connection.weight() / this.cruiseSpeed;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", cruiseSpeed=" + cruiseSpeed +
                ", cruiseAltitude=" + cruiseAltitude +
                ", maxRange=" + maxRange +
                ", airportCode='" + airportCode + '\'' +
                ", passengersCapacity=" + passengersCapacity +
                ", fuelCapacity=" + fuelCapacity +
                '}';
    }
}