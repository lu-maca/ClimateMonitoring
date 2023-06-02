/*************************************************
 * GeoData class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class exposes some methods for the handling of geophysical data
 * (geographical and climate params)
 */
public class GeoData {
    /* files */
    private LocationsFileHandler geoFile;
    private ClimateParametersFileHandler climateInfoFile;

    public GeoData() throws ParseException, IOException {
        geoFile = new LocationsFileHandler(Constants.MONITORING_COORDS_S);
        climateInfoFile = new ClimateParametersFileHandler(Constants.CLIMATE_PARAMS_S);
        geoFile.readFile();
        climateInfoFile.readFile();
    }

    /**
     * Add the given climate param measurament to the list of params.
     * @param climateParams
     */
    public void addClimateParams(ClimateParams climateParams){
        climateInfoFile.addClimateParams(climateParams);
    }

    /**
     * Add the given location to the list of locations.
     * @param loc
     */
    public void addLocation(Location loc){
        geoFile.addLocation(loc);
    }

    /**
     * Get climate params for the area with given geoname ID
     * @param geonameID
     * @return {@link ClimateParams}
     */
    public ClimateParams getClimateParamsFor(String geonameID){
        for (ClimateParams cp : climateInfoFile.getClimateParams()){
            if (cp.getGeonameID().equals(geonameID)){
                return cp;
            }
        }
        return null;
    }

    /**
     * Get {@link Location} instance for the given geoname ID
     * @param id
     * @return {@link Location}
     */
    public Location getLocationFromGeoID(String id) {
        for (Location l : getGeoLocationsList()){
            if (l.getGeonameID().equals(id)) { return l; }
        }
        return null;
    }

    /**
     * Get the list of all the locations
     * @return ArrayList<Location>
     */
    public ArrayList<Location> getGeoLocationsList(){ return geoFile.getLocationsList(); }

    /**
     * Update the file of climate parameters
     */
    public void updateClimateParamsFile(){
        climateInfoFile.writeFile();
    }

    /**
     * Update the file of locations
     */
    public void updateLocationsFile(){
        geoFile.writeFile();
    }


}
