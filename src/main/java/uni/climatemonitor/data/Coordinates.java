/*************************************************
 * Coordinates class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;
import static org.apache.lucene.util.SloppyMath.haversinMeters;

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
     * Distance between two coordinates, using fast math module SloppyMath
     *
     * @return distance in meters between the points
     * @see <a href="https://lucene.apache.org/core/8_2_0/core/org/apache/lucene/util/SloppyMath.html">SloppyMath class</a>
     */
    public double distance(Coordinates coordinates) {
        double dist = haversinMeters(this.latitude, this.longitude, coordinates.getLatitude(), coordinates.getLongitude());
        return dist;
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
        String out = lat + "° " + NS + " " + lon + "° " + EW ;
        return out;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}