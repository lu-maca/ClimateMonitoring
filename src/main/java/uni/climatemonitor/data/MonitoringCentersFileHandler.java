/*************************************************
 * MonitoringCentersFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
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
        InputStream input = getClass().getResourceAsStream(fileName);
        String text = readFromInputStream(input);
        input.close();
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(text);

        /* for all elements of the list */
        List ll = (List) obj;
        for (int i = 0; i< ll.size(); i++){
            monitoringCenters.add(new MonitoringCenter((HashMap) ll.get(i)));
        }
    }

    @Override
    public void writeFile(){
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("[\n");
            int size = monitoringCenters.size();
            int i = 0;
            for (MonitoringCenter m : monitoringCenters){
                if (i == size){ break; }
                i++;
                myWriter.write(m.toJson());
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
}
