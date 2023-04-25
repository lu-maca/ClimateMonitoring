/*************************************************
 * LocationsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import java.io.*;
import java.util.ArrayList;

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
            InputStream input = getClass().getResourceAsStream(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            in.readLine();
            String nextRecord = in.readLine();

            // we are going to read data line by line
            while (nextRecord != null) {
                Location tmpLoc = new Location(nextRecord.split(";"));
                locationsList.add(tmpLoc);
                nextRecord = in.readLine();
            }

            in.close();
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
