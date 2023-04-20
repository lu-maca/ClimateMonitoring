package uni.climatemonitor.data;
/**
 * Coordinates handling with some useful methods
 */
class Coordinates {
    private double latitude;
    private double longitude;

    /**
     * Constructor
     * @param lat
     * @param lon
     */
    public Coordinates(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    /**
     * Get a beautiful representation for coordinates in the form:
     *      N... E...
     */
    public String toString(){
        String lat = String.valueOf(Math.abs(latitude));
        String lon = String.valueOf(Math.abs(longitude));
        String NS = "N";
        String EW = "E";
        if (latitude < 0){ NS = "S"; }
        if (longitude < 0){ EW = "W"; }
        String out = lat + "° " + NS + " " + lon + "° " + EW;
        return out;
    }

}


/**
 * This class implements a container for Locations with associated
 * methods
 */
public class Location {
    private final String geonameID;
    private final String name;
    private final String asciiName;
    private final String state;
    private final Coordinates coordinates;

    /**
     * The constructor expects:
     *
     * @param locationInfo with the following structure:
     *                     Geoname_ID;Name;ASCII_Name;State_Code;State;Coordinates
     */
    public Location(String[] locationInfo) {
        geonameID = locationInfo[0];
        name = locationInfo[1];
        asciiName = locationInfo[2];
        state = locationInfo[3];
        double[] coords = unpackCoordinateString(locationInfo[5]);
        coordinates = new Coordinates(coords[0], coords[1]);
    }

    private double[] unpackCoordinateString(String c) {
        String[] splittedLatLong = c.replaceAll(" ", "").split(",");
        double[] out = new double[2];
        out[0] = Double.parseDouble(splittedLatLong[0]);
        out[1] = Double.parseDouble(splittedLatLong[1]);
        return out;
    }

    /**
     * override for toString
     * @return a representation of the object
     */
    @Override
    public String toString() {
        String out = asciiName + ", " + state;
        return out;
    }

    /**
     * state getter
     */
    public String getState() {
        return state;
    }

    /**
     * ascii_name getter
     */
    public String getAsciiName(){ return asciiName; }

    /**
     * geoname_id getter
     */
    public String getGeonameID(){ return geonameID; }
}
