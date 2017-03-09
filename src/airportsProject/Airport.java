package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;

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

    public Airplane sendPlane(Airplane plane) {
        Airplane airplaneToSend = this.airplanes.get(plane.getId());
        this.airplanes.put(plane.getId(), null); // deletes the plane from the ST
        return airplaneToSend;
    }

    public RedBlackBST<Integer, Airplane> getAirplanes() {
        return airplanes;
    }

    public RedBlackBST<Date, Flight> getFlights() {
        return flights;
    }

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