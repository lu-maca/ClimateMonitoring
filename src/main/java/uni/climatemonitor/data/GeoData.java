package uni.climatemonitor.data;

import uni.climatemonitor.generics.Constants;

import java.util.ArrayList;
import java.util.Map;

public class GeoData {
    private ArrayList<Location> geoLocationsRawList;
    private Map<String, ArrayList<Location>> geoStateMap;

    public GeoData() {
        /* geographical locations file */
        getGeographicalLocations();
    }

    private void getGeographicalLocations(){
        LocationsFileHandler geoFile = new LocationsFileHandler(Constants.MONITORING_COORDS_S);
        geoFile.readFile();
        geoLocationsRawList = geoFile.getLocationsList();
        geoStateMap = geoFile.getStateMap();

    }

    public ArrayList<Location> getGeoLocationsRawList(){ return geoLocationsRawList; }

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
