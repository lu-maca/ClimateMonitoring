package uni.climatemonitor.data;

import uni.climatemonitor.generics.Constants;

import java.util.ArrayList;
import java.util.Map;

public class GeoData {
    private ArrayList<Location> geoLocations;
    private Map<String, ArrayList<Location>> geoStateMap;
    private ArrayList<String> geoLocationsStringList = new ArrayList<>();

    public GeoData() {
        /* geographical locations file */
        getGeographicalLocations();
    }

    private void getGeographicalLocations(){
        LocationsFileHandler geoFile = new LocationsFileHandler(Constants.MONITORING_COORDS_S);
        geoFile.readFile();
        geoLocations = geoFile.getLocationsList();
        geoStateMap = geoFile.getStateMap();

        /* create useful list that contains strings representation of locations */
        for (Location loc : geoLocations) {
            geoLocationsStringList.add(loc.getRepresentation());
        }
    }

    public ArrayList<String> getGeoLocationsStringList(){
        return geoLocationsStringList;
    }

    public Location searchLocationFromName(String searchedString){
        /* name has the following structure:
            ascii_name, state (ST), coordinates
           We use the state as first filter to access geoStateMap key
           and searching the ascii_name between the values associated to this key
         */

        String[] splitString = searchedString.split(",");
        String stateName = splitString[1].trim();
        String ascii_name = splitString[0].trim();

        ArrayList<Location> tmpStateArray = geoStateMap.get(stateName);
        for (Location l : tmpStateArray){
            if (l.getAsciiName().equals(ascii_name)){
                return l;
            }
        }
        return null;
    }
}
