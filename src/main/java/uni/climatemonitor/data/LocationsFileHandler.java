package uni.climatemonitor.data;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements methods to handle monitoring_coordinates.data
 */
public class LocationsFileHandler extends FileHandler {
    private ArrayList<Location> locationsList = new ArrayList<>();
    private Map<String, ArrayList<Location>> stateMap = new HashMap<String, ArrayList<Location>>();

    public LocationsFileHandler(String fileName){
        super(fileName);
    }

    /**
     * Getter for locationList
     * @return locationList, the list of locations
     */
    public ArrayList<Location> getLocationsList(){
        return locationsList;
    }

    /**
     * getter for stateMap
     */
    public Map<String, ArrayList<Location>> getStateMap(){
        return stateMap;
    }

    /**
     * This method reads the CSV file and stores locations in a list of
     * Location objects.
     * @see <a href="https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv/">...</a>
     */
    @Override
    public void readFile(){

        try {
            FileReader filereader = new FileReader(fileName);

            CSVParser csvParser = new CSVParserBuilder().withSeparator(';')
                                        .build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                                        .withCSVParser(csvParser)
                                        .withSkipLines(1)
                                        .build();
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                Location tmpLoc = new Location(nextRecord);
                locationsList.add(tmpLoc);
                /* create the state map */
                ArrayList<Location> tmpLocList = stateMap.get(tmpLoc.getState());
                if (tmpLocList == null){
                    tmpLocList = new ArrayList<Location>();
                    stateMap.put(tmpLoc.getState(), tmpLocList);
                }
                tmpLocList.add(tmpLoc);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * test
     */
    public static void main(String[] args) {
        LocationsFileHandler f = new LocationsFileHandler("./data/monitoring_coordinates.data");
        f.readFile();
        ArrayList<Location> l = f.getLocationsList();
        Map<String, ArrayList<Location>> m = f.getStateMap();

        for (Map.Entry<String, ArrayList<Location>> entry : m.entrySet()){
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }
}
