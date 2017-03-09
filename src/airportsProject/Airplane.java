package airportsProject;

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
    private Flight  airplaneFlight;
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

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public float getCruiseSpeed() {
        return cruiseSpeed;
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

    public Flight getAirplaneFlight() {
        return airplaneFlight;
    }

    public void setAirplaneFlight(Flight airplaneFlight) {
        this.airplaneFlight = airplaneFlight;
    }

    public Airline getAirline() {
        return airline;
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