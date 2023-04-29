/*************************************************
 * MonitoringCenters class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MonitoringCenter {
    private String name;
    private String address;
    private ArrayList<String> monitoredAreas;
    private String jsonFormat =
            """
  {
      "name": "%s",
      "address": "%s",
      "monitored_areas": %s
    }""";

    public MonitoringCenter(HashMap o){
        this.name = o.get("name").toString();
        this.address = o.get("address").toString();
        this.monitoredAreas = unpackStringToStringArray(o.get("monitored_areas").toString());
    }

    private ArrayList<String> unpackStringToStringArray(String s){
        String sWithoutBrackets = s.replaceAll("\\[|\\]", "").trim();
        ArrayList<String> split = new ArrayList<>(Arrays.asList(sWithoutBrackets.split(",")));

        return split;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getMonitoredAreas() {
        return monitoredAreas;
    }

    public void setMonitoredAreas(ArrayList<String> monitoredAreas) {
        this.monitoredAreas = monitoredAreas;
    }

    @Override
    public String toString(){
        return name;
    }

    public String toJson(){
        String out = String.format(
                jsonFormat,
                name,
                address,
                monitoredAreas
        );
        return out;
    }
}
