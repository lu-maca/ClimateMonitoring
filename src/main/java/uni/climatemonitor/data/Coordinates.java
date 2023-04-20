package uni.climatemonitor.data;

/**
 * Coordinates handling with some useful methods
 */
public class Coordinates {
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
    @Override
    public String toString(){
        String lat = String.valueOf(Math.abs(latitude));
        String lon = String.valueOf(Math.abs(longitude));
        String NS = "N";
        String EW = "E";
        if (latitude < 0){ NS = "S"; }
        if (longitude < 0){ EW = "W"; }
        String out = "<html>" + lat + "° " + NS + "<br>" + lon + "° " + EW + "</html>";
        return out;
    }
}