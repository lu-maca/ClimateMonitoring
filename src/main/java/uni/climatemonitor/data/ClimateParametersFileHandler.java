package uni.climatemonitor.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
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
        this.climateParamsList.add(climateParams);
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
                i++;
                if (i == size){ break; }
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
