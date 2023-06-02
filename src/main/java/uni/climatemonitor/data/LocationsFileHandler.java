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

            BufferedReader in = new BufferedReader(new FileReader(fileName));
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

    public void writeFile() {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            int size = locationsList.size();
            int i = 0;
            for (Location l : locationsList) {
                if (i == size) {
                    break;
                }
                i++;
                myWriter.write(l.toCSV());
                myWriter.write("\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

        }
    }
    
}
