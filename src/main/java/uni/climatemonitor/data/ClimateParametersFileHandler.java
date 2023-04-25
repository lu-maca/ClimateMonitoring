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
import java.util.*;

public class ClimateParametersFileHandler extends FileHandler {
    private ArrayList<ClimateParams> climateParamsList = new ArrayList<>();

    public ClimateParametersFileHandler(String fileName){
        super(fileName);
    }

    public ArrayList<ClimateParams> getClimateParams() {
        return climateParamsList;
    }

    public void addClimateParams(ClimateParams climateParams) {
        climateParamsList.add(climateParams);
    }

    /**
     * Read the climate params yaml file
     * @see <a href="https://www.baeldung.com/java-snake-yaml">...</a>
     */
    @Override
    public void readFile() throws ParseException, IOException {
        InputStream input = getClass().getResourceAsStream(fileName);
        String text = readFromInputStream(input);
        input.close();
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
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, ParseException {
        ClimateParametersFileHandler f = new ClimateParametersFileHandler(Constants.CLIMATE_PARAMS_S);
        f.readFile();
        f.writeFile();
    }
}
