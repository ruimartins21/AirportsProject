package airportsProject;

import java.util.Vector;
import princeton.cs.algs4;

public class Airport {

  private String name;
  private String code;
  private String city;
  private String country;
  private String continent;
  private float rating;
  private RedBlackBST_AED2_1617<Integer, String> logs = new RedBlackBST_AED2_1617<>();

    private Vector  airplanes;
    private Vector  flights;
  
  public void receivePlane( Airplane newPlane) {
  }

  public int numberOfPlanes() {
  return 0;
  }

  public Date sendPlane( Airplane plane) {
  return null;
  }

  public boolean remove() {
  return false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getContinent() {
    return continent;
  }

  public void setContinent(String continent) {
    this.continent = continent;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  /**
 *
 * @element-type Airplane
 */
  public Vector getAirplanes() {
    return airplanes;
  }

  public void setAirplanes(Vector airplanes) {
    this.airplanes = airplanes;
  }

  /**
 *
 * @element-type Flight
 */
  public Vector getFlights() {
    return flights;
  }

  public void setFlights(Vector flights) {
    this.flights = flights;
  }
}