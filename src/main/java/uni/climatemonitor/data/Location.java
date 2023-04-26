/*************************************************
 * Location class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

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
        String out = asciiName + ", " + state + ", " + coordinates.toString();
        return out;
    }

    /**
     * return a representation of the object without coordinates
     */
    public String toStringNoCoordinates() {
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

    /**
     * coordinates getter
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
}
