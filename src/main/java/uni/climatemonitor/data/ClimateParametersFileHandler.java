package uni.climatemonitor.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.*;

public class ClimateParametersFileHandler extends FileHandler {
    private ArrayList<ClimateParams> climateParams = new ArrayList<>();

    public ClimateParametersFileHandler(String fileName){
        super(fileName);
    }

    public ArrayList<ClimateParams> getCities() {
        return climateParams;
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
            climateParams.add(new ClimateParams((HashMap) ll.get(i)));
        }

    }

    public static void main(String[] args) throws IOException, ParseException {
        ClimateParametersFileHandler f = new ClimateParametersFileHandler(Constants.CLIMATE_PARAMS_S);
        f.readFile();
    }
}
