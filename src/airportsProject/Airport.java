package airportsProject;

import libs.RedBlackBST;

/**
 * The airport class has information (and provides it by getters aswell) on itself and on the airplanes that are parked
 * on a certain airport aswell as an history of flights that passed through it, being the airport the origin, the destination,
 * or simply a scale of a flight between two other airports
 */
public class Airport {

    private String name;
    private String code;
    private String city;
    private String country;
    private String continent;
    private float rating;
    private RedBlackBST<Date, Flight> flights = new RedBlackBST<>();
    private RedBlackBST<Integer, Airplane> airplanes = new RedBlackBST<>();

    public Airport(String name, String code, String city, String country, String continent, float rating) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.rating = rating;
    }

    public void receivePlane(Airplane newPlane) {
        // the plane has to update the current airport where it is
        newPlane.setAirportCode(this.code);
        this.airplanes.put(newPlane.getId(), newPlane);
    }

    public void sendPlane(Airplane plane) {
        this.airplanes.put(plane.getId(), null); // deletes the plane from the ST
    }

    public RedBlackBST<Integer, Airplane> getAirplanes() {
        return airplanes;
    }

    public RedBlackBST<Date, Flight> getFlights() { return flights; }

    public void newFlight(Flight flight) {
        this.flights.put(flight.getDate(), flight);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getContinent() {
        return continent;
    }

    public float getRating() {
        return rating;
    }

    public void setName(String name){this.name = name;}

    public void setRating(Float rating){this.rating = rating;}

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", continent='" + continent + '\'' +
                ", rating=" + rating +
                '}';
    }
}