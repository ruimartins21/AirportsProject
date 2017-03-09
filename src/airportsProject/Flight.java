package airportsProject;

import java.util.ArrayList;

public class Flight {

//    private float altitude;
    private float distance;
    private float costs;
//    private float windSpeed;
    private Date duration;
    private Date date;
    private int passengers;
    private Airplane airplane;
    private ArrayList<Connection> connections = new ArrayList<>();
    private Airport airportOfOrigin;
    private Airport airportOfDestination;

    public Flight(float altitude, float distance, float costs, float windSpeed, Date duration, Date date,
                  int passengers, Airplane airplane, Airport airportOfOrigin, Airport airportOfDestination) {
//        this.altitude = altitude;
        this.distance = distance;
        this.costs = costs;
//        this.windSpeed = windSpeed;
        this.duration = duration;
        this.date = date;
        this.passengers = passengers; // verificar se o aviao pertendido consegue levar o nº de passageiros
        this.airplane = airplane;
        this.airportOfOrigin = airportOfOrigin;
        this.airportOfDestination = airportOfDestination;
    }

    public float setPlaneConsumption(float range, int fuelCapacity) {
    return 0.0f;
  }

    public void setWindVelocity() {
    }

    public void setFlightCost() {
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
}