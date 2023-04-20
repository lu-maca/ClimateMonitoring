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

    public GeoData() throws ParseException, IOException {
        geoFile = new LocationsFileHandler(Constants.MONITORING_COORDS_S);
        climateInfoFile = new ClimateParametersFileHandler(Constants.CLIMATE_PARAMS_S);
        geoFile.readFile();
        climateInfoFile.readFile();
    }

    public void addClimateParams(ClimateParams climateParams){
        climateInfoFile.addClimateParams(climateParams);
    }

    public ClimateParams getClimateParamsFor(String geonameID){
        for (ClimateParams cp : climateInfoFile.getClimateParams()){
            if (cp.getGeonameID().equals(geonameID)){
                return cp;
            }
        }
        return null;
    }

    public ArrayList<Location> getGeoLocationsList(){ return geoFile.getLocationsList(); }

    public void updateClimateParamsFile(){
        climateInfoFile.writeFile();
    }

}
