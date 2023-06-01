/*************************************************
 * ClimateParametersFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Handler for Climate Parameters file
 */
public class ClimateParametersFileHandler extends FileHandler {
    private ArrayList<ClimateParams> climateParamsList = new ArrayList<>();

    public ClimateParametersFileHandler(String fileName){
        super(fileName);
    }

    /**
     * Getter for the list of climate params
     * @return ArrayList<ClimateParams>
     */
    public ArrayList<ClimateParams> getClimateParams() {
        return climateParamsList;
    }

    /**
     * Add new measure to climate params list
     * @param climateParams
     */
    public void addClimateParams(ClimateParams climateParams) {
        climateParamsList.add(climateParams);
    }

    /**
     * Read the climate params yaml file
     * @see <a href="https://www.baeldung.com/java-snake-yaml">...</a>
     */
    @Override
    public void readFile() throws ParseException, IOException {
        Path path = Path.of(fileName);
        String text = Files.readString(path);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(text);

        /* for all elements of the list */
        List ll = (List) obj;
        for (int i = 0; i< ll.size(); i++){
            climateParamsList.add(new ClimateParams((HashMap) ll.get(i)));
        }
    }

    @Override
    public void writeFile(){
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("[\n");
            int size = climateParamsList.size();
            int i = 0;
            for (ClimateParams cp : climateParamsList){
                if (i == size){ break; }
                i++;
                myWriter.write(cp.toJson());
                myWriter.write(",");
                myWriter.write("\n");
            }
            myWriter.write("\n]");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, ParseException {
        ClimateParametersFileHandler f = new ClimateParametersFileHandler(Constants.CLIMATE_PARAMS_S);
        f.readFile();
        f.writeFile();
    }
}
