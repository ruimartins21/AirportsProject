package airportsProject;

public class Flight {

    private float distance;
    private float costs;
    private Date duration;
    private Date date;
    private int passengers;
    private Airplane airplane;
//    private ArrayList<Connection> connections = new ArrayList<>();
    private Connection connection;
    private Airport airportOfOrigin;
    private Airport airportOfDestination;

    public Flight(float distance, Date duration, Date date,
                  int passengers, Airplane airplane, Airport airportOfOrigin, Airport airportOfDestination) {
        this.distance = distance;
        this.duration = duration;
        this.date = date;
        this.airplane = airplane;
        this.setAirports(airportOfOrigin, airportOfDestination);
        this.setAirplane(airplane);
        this.setPassengers(passengers); // checks if the number of passengers doesn't exceed the airplane capacity
    }

    private void setConnection(){
//        this.connection = new Connection();
    }

    private float planeConsumption() {
        return 1000*(airplane.getFuelCapacity()/airplane.getMaxRange());
    }

//  custo definido pela conecao da viagem mais barata. apos sleecionado pela coneccao a viagem mais barata (é guardado em costs)
    public void setFlightCost() {
        this.costs = this.airplane.getAirplaneCost(this.distance, this.connection.getWindSpeed(),this.connection.getAltitude());

    }


    private void setPassengers(int passengers) {
        if(passengers <= this.airplane.getPassengersCapacity()){
            this.passengers = passengers;
        }else{
            throw new IllegalArgumentException("Flight: Number of passengers exceeds the airplane capacity");
        }
    }

    private void setAirplane(Airplane plane){
        this.airplane.setAirplaneFlight(this);
        this.airplane.setAirportCode(this.airportOfDestination.getCode());
        this.airportOfDestination.receivePlane(this.airplane);
        this.airportOfOrigin.sendPlane(this.airplane);
    }

    private void setAirports(Airport origin, Airport destination){
        origin.newFlight(this);
        origin.sendPlane(this.airplane); // the plane will leave this airport
        destination.newFlight(this);
        destination.receivePlane(this.airplane); // the plane will arrive at this airport
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
//                "\n-> connections=" + connections +
                "\n-> airportOfOrigin=" + airportOfOrigin +
                "\n-> airportOfDestination=" + airportOfDestination +
                "\n}";
    }
}