package airportsProject;

import libs.DirectedEdge;

/**
 * This class provides information on the connection between two airports on a certain flight
 * not necessarily from origin to destination
 * it has weights like the altitude and wind speed that affects the cost of the flight
 */
public class Connection extends DirectedEdge{
    private float altitude;
    private float windSpeed;

    public Connection(int v, int w, double weight, float altitude, float windSpeed) {
        super(v, w, weight);
        this.altitude = altitude;
        this.windSpeed = windSpeed;
//        this.altitude = 1080;
//        this.windSpeed = -10;
    }

    public float getAltitude() {
        return altitude;
    }

    public float getWindSpeed() {
        return windSpeed;
    }
}
