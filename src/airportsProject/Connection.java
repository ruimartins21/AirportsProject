package airportsProject;

import libs.DirectedEdge;

import java.io.Serializable;

/**
 * This class provides information on the connection between two airports on a certain flight
 * not necessarily from origin to destination
 * it has weights like the altitude and wind speed that affects the cost of the flight
 */
public class Connection extends DirectedEdge implements Serializable {
    private float altitude;
    private float windSpeed;

    public Connection(int airportOrigin, int airportDestination, double distance, float altitude, float windSpeed) {
        super(airportOrigin, airportDestination, distance);
        this.altitude = altitude;
        this.windSpeed = windSpeed;
    }

    public float getAltitude() {
        return altitude;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

}
