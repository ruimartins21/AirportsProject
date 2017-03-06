package airportsProject;

import java.util.Vector;

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

    private Airport airport;
    private Vector  airplaneFlight;

  private Airline airline;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getCruiseSpeed() {
    return cruiseSpeed;
  }

  public void setCruiseSpeed(float cruiseSpeed) {
    this.cruiseSpeed = cruiseSpeed;
  }

  public float getCruiseAltitude() {
    return cruiseAltitude;
  }

  public void setCruiseAltitude(float cruiseAltitude) {
    this.cruiseAltitude = cruiseAltitude;
  }

  public float getMaxRange() {
    return maxRange;
  }

  public void setMaxRange(float maxRange) {
    this.maxRange = maxRange;
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

  public void setPassengersCapacity(int passengersCapacity) {
    this.passengersCapacity = passengersCapacity;
  }

  public int getFuelCapacity() {
    return fuelCapacity;
  }

  public void setFuelCapacity(int fuelCapacity) {
    this.fuelCapacity = fuelCapacity;
  }

  public Airport getAirport() {
    return airport;
  }

  public void setAirport(Airport airport) {
    this.airport = airport;
  }

  /**
 *
 * @element-type Flight
 */
  public Vector getAirplaneFlight() {
    return airplaneFlight;
  }

  public void setAirplaneFlight(Vector airplaneFlight) {
    this.airplaneFlight = airplaneFlight;
  }

  public Airline getAirline() {
    return airline;
  }

  public void setAirline(Airline airline) {
    this.airline = airline;
  }
}