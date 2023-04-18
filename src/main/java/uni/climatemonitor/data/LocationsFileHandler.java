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
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile(){}

    /**
     * test
     */
    public static void main(String[] args) {

    }
}
