package airportsProject;

import java.util.ArrayList;

public class Flight {

    private float distance;
    private float costs;
    private Date duration;
    private Date date;
    private int passengers;
    private Airplane airplane;
    private ArrayList<Connection> connections = new ArrayList<>();
    private Airport airportOfOrigin;
    private Airport airportOfDestination;

    public Flight(float distance, Date duration, Date date,
                  int passengers, Airplane airplane, Airport airportOfOrigin, Airport airportOfDestination) {
        this.distance = distance;
        this.duration = duration;
        this.date = date;
        this.setAirplane(airplane);
        this.setPassengers(passengers); // checks if the number of passengers doesn't exceed the airplane capacity
        this.setAirports(airportOfOrigin, airportOfDestination);
    }

    private float planeConsumption() {
        return 1000*(airplane.getFuelCapacity()/airplane.getMaxRange());
    }

    public void setWindVelocity() {
    }

    public void setFlightCost() {
    }

    private void setPassengers(int passengers) {
        if(passengers <= this.airplane.getPassengersCapacity()){
            this.passengers = passengers;
        }else{
            throw new IllegalArgumentException("Flight: Number of passengers exceeds the airplane capacity");
        }
    }

    private void setAirplane(Airplane plane){
        plane.setAirplaneFlight(this);
        this.airplane = plane;
    }

    private void setAirports(Airport origin, Airport destination){
        origin.newFlight(this);
        Airplane thisAirplane = origin.sendPlane(this.airplane); // the plane will leave this airport
        destination.newFlight(this);
        destination.receivePlane(thisAirplane); // the plane will arrive at this airport
        this.airportOfOrigin = origin;
        this.airportOfDestination = destination;
    }

    public float getDistance() {
    return distance;
  }

    public float getCosts() {
    return costs;
  }

    public Date getDuration() {
    return duration;
  }

    public Date getDate() {
    return date;
  }

    public int getPassengers() {
    return passengers;
  }

    public Airplane getAirplane() {
    return airplane;
  }

    public Airport getAirportOfOrigin() {
    return airportOfOrigin;
  }

    public Airport getAirportOfDestination() {
    return airportOfDestination;
  }

    @Override
    public String toString() {
        return "Flight{" +
                "\n-> distance= " + distance +
                "\n-> costs=" + costs +
                "\n-> duration= " + duration.getDuration() +
                "\n-> date=" + date +
                "\n-> passengers=" + passengers +
                "\n-> airplane=" + airplane +
                "\n-> connections=" + connections +
                "\n-> airportOfOrigin=" + airportOfOrigin +
                "\n-> airportOfDestination=" + airportOfDestination +
                "\n}";
    }
}