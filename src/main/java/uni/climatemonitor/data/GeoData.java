package uni.climatemonitor.data;

import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GeoData {
    /* files */
    private LocationsFileHandler geoFile;
    private ClimateParametersFileHandler climateInfoFile;
    /* locations and climate params */
    private ArrayList<Location> geoLocationsList;
    private ArrayList<ClimateParams> climateParamsList;

    public GeoData() throws ParseException, IOException {
        geoFile = new LocationsFileHandler(Constants.MONITORING_COORDS_S);
        climateInfoFile = new ClimateParametersFileHandler(Constants.CLIMATE_PARAMS_S);

        /* climate parameters */
        getClimateParams();

        /* geographical locations file */
        getGeographicalLocations();
    }

    private void getClimateParams() throws ParseException, IOException {
        climateInfoFile.readFile();
        climateParamsList = climateInfoFile.getCities();
    }

    private void getGeographicalLocations(){
        geoFile.readFile();
        geoLocationsList = geoFile.getLocationsList();
    }

    public ClimateParams getClimateParamsFor(String geonameID){
        for (ClimateParams cp : climateParamsList){
            if (cp.getGeonameID().equals(geonameID)){
                return cp;
            }
        }
        return null;
    }

    public ArrayList<Location> getGeoLocationsList(){ return geoLocationsList; }

}
