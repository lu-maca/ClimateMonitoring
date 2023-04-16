package uni.climatemonitor.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitoringCentersFileHandler extends FileHandler{
    private ArrayList<MonitoringCenter> monitoringCenters = new ArrayList<>();

    public MonitoringCentersFileHandler(String fileName){
        super(fileName);
    }

    public ArrayList<MonitoringCenter> getMonitoringCenters() {
        return monitoringCenters;
    }

    @Override
    public void readFile() throws ParseException, IOException {
        Path path = Path.of(fileName);
        String text = Files.readString(path);

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(text);

        /* for all elements of the list */
        List ll = (List) obj;
        for (int i = 0; i< ll.size(); i++){
            monitoringCenters.add(new MonitoringCenter((HashMap) ll.get(i)));
        }
    }
}
