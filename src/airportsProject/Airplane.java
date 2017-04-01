package airportsProject;

import libs.RedBlackBST;

/**
 * The airplane class represents an airplane object with its characteristics and its connections (to an airport, to flights)
 * Aswell as the usual methods that provides (getters and setters), the class calculates the airplane cost for a specific
 * connection of a flight and it provides information on the flights already done, or still to be done
 */
public class Airplane {

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
     * Consumption of fuel by the airplane considering its capacity and its max range
     * @return returns the ammount of fuel it will spend each 1000 km
     */
    private float planeConsumption() {
        return 1000*(this.getFuelCapacity()/this.getMaxRange());
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
     * @param distance distance of the connection
     * @param windSpeed speed of the wind (and its direction given by the signal)
     * @param altitude altitude of the connection that the airplane must fly
     * @return returns the cost in litres for the connection
     */
    public float getAirplaneCost(float distance, float windSpeed, float altitude) {
        // cost of the wind speed
        float windCost = 0f;
        if(windSpeed < 0){ // against the airplane, adds cost
            windSpeed = Math.abs(windSpeed);
            windCost = -(Main.windCost*windSpeed);
        }else{ // in favor of the airplane, reduces cost
            windCost = Main.windCost*windSpeed;
        }
        // cost of the altitude
        float altitudeDiference = this.cruiseAltitude - altitude; // difference between airplane cruise altitude and the connection altitude
        altitudeDiference %= 1000; // adds cost at each 1000 km difference in height
        if(altitudeDiference > 0){
            return  planeConsumption() + (Main.nValue * altitudeDiference) + (windCost*distance);
        }else if(altitudeDiference < 0){
            return  planeConsumption() + (Main.mValue * altitudeDiference) + (windCost*distance);
        }
        return planeConsumption() + (windCost*distance);
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