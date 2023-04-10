package uni.climatemonitor.data;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.LinkedList;

/**
 * Coordinates handling with some useful methods
 */
class Coordinates {
    private double latitude;
    private double longitude;

    /**
     * Constructor
     * @param lat
     * @param lon
     */
    public Coordinates(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    /**
     * Get a beautiful representation for coordinates in the form:
     *      N... E...
     */
    public String toString(){
        String lat = String.valueOf(Math.abs(latitude));
        String lon = String.valueOf(Math.abs(longitude));
        String NS = "N";
        String EW = "E";
        if (latitude < 0){ NS = "S"; }
        if (longitude < 0){ EW = "W"; }
        String out = lat + "° " + NS + " " + lon + "° " + EW;
        return out;
    }

}

/**
 * This class implements a container for Locations with associated
 * methods
 */
class Location {
    private String geonameID;
    private String name;
    private String asciiName;
    private String state;
    private Coordinates coordinates;
    private String representation;

    /**
     * The constructor expects:
     * @param locationInfo with the following structure:
     *                     Geoname_ID;Name;ASCII_Name;State_Code;State;Coordinates
     */
    public Location(String[] locationInfo){
        geonameID = locationInfo[0];
        name = locationInfo[1];
        asciiName = locationInfo[2];
        state = locationInfo[4] + " (" + locationInfo[3] + ")";
        double[] coords = unpackCoordinateString(locationInfo[5]);
        coordinates = new Coordinates(coords[0], coords[1]);
        representation = createRepresentation();
    }

    private double[] unpackCoordinateString(String c) {
        String[] splittedLatLong = c.replaceAll(" ", "").split(",");
        double[] out = new double[2];
        out[0] = Double.parseDouble(splittedLatLong[0]);
        out[1] = Double.parseDouble(splittedLatLong[1]);
        return out;
    }

    private String createRepresentation(){
        String out = asciiName + ", " + state + ", " + coordinates.toString();
        return out;
    }

    /**
     * Getter for representation
     */
    public String getRepresentation(){
        return representation;
    }
}

/**
 * Implements methods to handle monitoring_coordinates.data
 */
public class LocationsFileHandler extends FileHandler {
    public LinkedList<Location> locationsList = new LinkedList<>();

    public LocationsFileHandler(String fileName){
        super(fileName);
    }

    /**
     * Getter for locationList
     * @return locationList, the list of locations
     */
    public LinkedList<Location> getLocationsList(){
        return locationsList;
    }

    /**
     * This method reads the CSV file and stores locations in a list of
     * Location objects.
     * @see <a href="https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv/">...</a>
     */
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
                locationsList.add(new Location(nextRecord));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * test
     */
//    public static void main(String[] args) {
//        LocationsFileHandler f = new LocationsFileHandler("./data/monitoring_coordinates.data");
//        f.readFile();
//        LinkedList<Location> l = f.getLocationsList();
//
//        for (Location i : l){
//            System.out.println(i.getRepresentation());
//        }
//    }
}
