package airportsProject;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import libs.RedBlackBST;

import java.io.IOException;

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
    private double latitude = -1;
    private double longitude = -1;

    public Airport(String name, String code, String city, String country, String continent, float rating) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.rating = rating;
        setCoordinates();
    }

    private void setCoordinates(){
        final Geocoder geocoder = new Geocoder();
        String location = this.city + ", " + this.country;
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("en").getGeocoderRequest();
        try {
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            if(geocoderResponse.getResults().isEmpty()){
                System.out.println("Invalid Location");
                return;
            }
            this.latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
            this.longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
            this.latitude  = (Main.mapHeight) * (90 - latitude) / 180;
            this.longitude = (Main.mapWidth) * (180 + longitude) / 360;
        }catch (IOException e){
            System.out.println("Maps Coordinates: Internet connection required.");
        }
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
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