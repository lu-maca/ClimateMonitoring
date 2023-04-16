package uni.climatemonitor.data;

import java.util.HashMap;

public class MonitoringCenter {
    private String name;
    private String address;
    private String[] monitoredAreas;

    public MonitoringCenter(HashMap o){
        for (Object key: o.keySet()) {
            switch (key.toString()) {
                case "name":
                    this.name = o.get(key).toString();
                case "address":
                    this.address = o.get(key).toString();
                case "monitored_areas":
                    this.monitoredAreas = unpackStringToStringArray(o.get(key).toString());
            }
        }
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
