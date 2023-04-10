package uni.climatemonitor.data;

import uni.climatemonitor.generics.Constants;

import java.util.ArrayList;
import java.util.LinkedList;

public class GeoData {
    private LinkedList<Location> geoLocations;
    private ArrayList<String> geoLocationsStringList = new ArrayList<>();

    public GeoData() {
        /* geographical locations file */
        LocationsFileHandler geoFile = new LocationsFileHandler(Constants.MONITORING_COORDS_S);
        geoFile.readFile();
        geoLocations = geoFile.getLocationsList();
        for (Location loc : geoLocations) {
            geoLocationsStringList.add(loc.getRepresentation());
        }
        System.out.println(geoLocationsStringList);
    }

    public ArrayList<String> getGeoLocationsStringList(){
        return geoLocationsStringList;
    }

}
