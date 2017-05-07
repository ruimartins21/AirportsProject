package airportsProject;

import java.io.Serializable;

/**
 * Utility class to store on a binary file the information about the coordinates of the airports
 */
public class Coordinates implements Serializable{
    private String code;
    private double longitude;
    private double latitude;

    public Coordinates(String code, double longitude, double latitude) {
        this.code = code;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCode() {
        return code;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
