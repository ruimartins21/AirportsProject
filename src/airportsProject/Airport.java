package airportsProject;

import java.util.ArrayList;

public class Airport {

    private String name;
    private String code;
    private String city;
    private String country;
    private String continent;
    private float rating;
    private ArrayList<Airplane> airplanes = new ArrayList<>();
    private ArrayList<Flight> flights = new ArrayList<>();

    public Airport(String name, String code, String city, String country, String continent, float rating) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.rating = rating;
    }

    public void receivePlane(Airplane newPlane) {
        // adds the new plane to the list of planes currently on this airport
        this.airplanes.add(newPlane);
        // the plane now has to update the current airport code it has
        newPlane.setAirportCode(this.code);
    }

    public Airplane sendPlane(Airplane plane) {
        int index = this.airplanes.indexOf(plane);
        return this.airplanes.remove(index);
    }

    public ArrayList<Airplane> getAirplanes() {
        return airplanes;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void newFlight(Flight flight) {
        this.flights.add(flight);
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