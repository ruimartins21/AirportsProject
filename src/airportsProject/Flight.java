package airportsProject;

/**
 * Class responsible for the flight information together with its connections
 * It is capable of giving informations on the airplane that will do the respective flight
 * to set an airplane to do it, to set a cost for the flight, to set the number of passengers travelling
 * and setting the airports where the airplane will stop aswell the airport from where it will depart
 * (that is the airport where the airplane is parked)
 */
public class Flight {

    private float distance;
    private float costs;
    private Date duration;
    private Date date;
    private int passengers;
    private Airplane airplane;
//    private ArrayList<Connection> connections = new ArrayList<>();
    private Connection connection = new Connection(0,0,0,930,400);
    private Airport airportOfOrigin;
    private Airport airportOfDestination;

    public Flight(float distance, Date duration, Date date,
                  int passengers, Airplane airplane, Airport airportOfOrigin, Airport airportOfDestination) {
        this.distance = distance;
        this.duration = duration;
        this.date = date;
        this.airplane = airplane;
        this.setAirports(airportOfOrigin, airportOfDestination);
        this.setAirplane();
        this.setPassengers(passengers);
        this.setFlightCost();
    }

    /**
     * hard coded for the 1st phase, this method will set a link between two airports
     * this connection can be between the origin and the destination or not
     */
    private void setConnection(){
//        this.connection = new Connection();
    }

    /**
     * Cost is defined by the shortest path algorithm that will take in consideration various weights like distance, altitude and wind speed
     * being used as it should in the 2nd phase
     */
    public void setFlightCost() {
        // airplane returns how many liters will spend on the flight
        this.costs = this.airplane.getAirplaneCost(this.distance, this.connection.getWindSpeed(),this.connection.getAltitude());

    }

    /**
     * converts the cost given in litres to an actual cost for the airline or the airport to pay
     * @return returns the cost in euros
     */
    public float getFlightCostEuros(){
        return Main.euroValue * this.costs;
    }

    /**
     * checks if the airplane used in the flight supports the ammount of passengers setted to be in this flight
     * @param passengers number of passengers to be on the flight
     */
    private void setPassengers(int passengers) {
        if(passengers <= this.airplane.getPassengersCapacity()){
            this.passengers = passengers;
        }else{
            throw new IllegalArgumentException("Flight: Number of passengers exceeds the airplane capacity");
        }
    }

    /**
     * Sets an airplane to do the flight
     * the airplane will store this new flight on its history, will change the airport where it's "parked" to the destination one
     * and the airport of origin will then "send" de airplane removing it from its list, and the destionation airport will "receive" it
     * adding it to its list of parked airplanes
     * this happens instantly because we don't have a "time" scale here
     */
    private void setAirplane(){
        this.airplane.setAirplaneFlight(this);
        this.airplane.setAirportCode(this.airportOfDestination.getCode());
        this.airportOfDestination.receivePlane(this.airplane);
        this.airportOfOrigin.sendPlane(this.airplane);
    }

    /**
     * adds a new flight to the history of the airport on those where the airplane will pass
     * on the 2nd phase will happen that a flight might need to stop on other airports before the destination for various reasons
     * this airports where it will stop will too receive the flight to its history
     * @param origin the airport of origin
     * @param destination the airport of destination
     */
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