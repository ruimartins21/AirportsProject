package airportsProject;

public class Flight {

    private float altitude;
    private float distance;
    private float costs;
    private float windSpeed;
    private Date duration;
    private Date date;
    private int passengers;
    private Airplane airplane;
    private Airport airportOfOrigin;
    private Airport airportOfDestination;

    public Flight(float altitude, float distance, float costs, float windSpeed, Date duration, Date date,
                  int passengers, Airplane airplane, Airport airportOfOrigin, Airport airportOfDestination) {
        this.altitude = altitude;
        this.distance = distance;
        this.costs = costs;
        this.windSpeed = windSpeed;
        this.duration = duration;
        this.date = date;
        this.passengers = passengers;
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


    public float getAltitude() {
    return altitude;
  }

    public void setAltitude(float altitude) {
    this.altitude = altitude;
  }

    public float getDistance() {
    return distance;
  }

    public void setDistance(float distance) {
    this.distance = distance;
  }

    public float getCosts() {
    return costs;
  }

    public void setCosts(float costs) {
    this.costs = costs;
  }

    public float getWindSpeed() {
    return windSpeed;
  }

    public void setWindSpeed(float windSpeed) {
    this.windSpeed = windSpeed;
  }

    public Date getDuration() {
    return duration;
  }

    public void setDuration(Date duration) {
    this.duration = duration;
  }

    public Date getDate() {
    return date;
  }

    public void setDate(Date date) {
    this.date = date;
  }

    public int getPassengers() {
    return passengers;
  }

    public void setPassengers(int passengers) {
    this.passengers = passengers;
  }

    public Airplane getAirplane() {
    return airplane;
  }

    public void setAirplane(Airplane airplane) {
    this.airplane = airplane;
  }

    public Airport getAirportOfOrigin() {
    return airportOfOrigin;
  }

    public void setAirportOfOrigin(Airport airportOfOrigin) {
    this.airportOfOrigin = airportOfOrigin;
  }

    public Airport getAirportOfDestination() {
    return airportOfDestination;
  }

    public void setAirportOfDestination(Airport airportOfDestination) {
        this.airportOfDestination = airportOfDestination;
    }
}