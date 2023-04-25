/*************************************************
 * MonitoringCenters class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import java.util.HashMap;

public class MonitoringCenter {
    private String name;
    private String address;
    private String[] monitoredAreas;

    public MonitoringCenter(HashMap o){
        this.name = o.get("name").toString();
        this.address = o.get("address").toString();
        this.monitoredAreas = unpackStringToStringArray(o.get("monitored_areas").toString());
    }

    private String[] unpackStringToStringArray(String s){
        String sWithoutBrackets = s.replaceAll("\\[|\\]", "");
        String[] split = sWithoutBrackets.split(",");

        return split;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String[] getMonitoredAreas() {
        return monitoredAreas;
    }
}
