/*************************************************
 * ClimateParams class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a set of measurements for an area
 */
public class ClimateParams {
    private String ascii_name;
    private String state;
    private ArrayList<String> wind = new ArrayList<>();
    private ArrayList<String> humidity = new ArrayList<>();
    private ArrayList<String> pressure = new ArrayList<>();
    private ArrayList<String> temperature = new ArrayList<>();
    private ArrayList<String> rainfall = new ArrayList<>();
    private ArrayList<String> glacier_alt = new ArrayList<>();
    private ArrayList<String> glacier_mass = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> who = new ArrayList<>();
    private ArrayList<String> center = new ArrayList<>();
    private ArrayList<String> notes = new ArrayList<>();
    private int tot_measure;
    private String geonameID;

    /* json format */
    private String jsonFormat =
            """
  {
    "state": "%s",
    "geoname_id": %s,
    "ascii_name": "%s",
    "wind": %s,
    "humidity": %s,
    "pressure": %s,
    "temperature": %s,
    "rainfall": %s,
    "glaciers_alt": %s,
    "glaciers_mass": %s,
    "tot_measure": %d,
    "notes": %s,
    "date": %s,
    "center": %s,
    "who": %s
  }""";

    public ClimateParams(){}

    public ClimateParams(HashMap o) {
        this.ascii_name = o.get("ascii_name").toString();
        this.state = o.get("state").toString();
        this.wind = unpackStringToStringArray(o.get("wind").toString());
        this.humidity = unpackStringToStringArray(o.get("humidity").toString());
        this.pressure = unpackStringToStringArray(o.get("pressure").toString());
        this.temperature = unpackStringToStringArray(o.get("temperature").toString());
        this.rainfall = unpackStringToStringArray(o.get("rainfall").toString());
        this.glacier_alt = unpackStringToStringArray(o.get("glaciers_alt").toString());
        this.glacier_mass = unpackStringToStringArray(o.get("glaciers_mass").toString());
        this.who = unpackStringToStringArray(o.get("who").toString());
        this.center = unpackStringToStringArray(o.get("center").toString());
        this.tot_measure = Math.toIntExact((long) o.get("tot_measure"));
        this.notes = unpackStringToStringArray(o.get("notes").toString());
        this.geonameID = o.get("geoname_id").toString();
        this.date = unpackStringToStringArray(o.get("date").toString());
    }

    private ArrayList<String> unpackStringToStringArray(String s){
        String sWithoutBrackets = s.replaceAll("\\[|\\]", "").trim();
        ArrayList<String> split = new ArrayList<>(Arrays.asList(sWithoutBrackets.split(",")));

        return split;
    }

    public ArrayList<String> getGlacier_alt() {
        return glacier_alt;
    }

    public ArrayList<String> getHumidity() {
        return humidity;
    }

    public ArrayList<String> getGlacier_mass() {
        return glacier_mass;
    }

    public ArrayList<String> getPressure() {
        return pressure;
    }

    public ArrayList<String> getRainfall() {
        return rainfall;
    }

    public ArrayList<String> getTemperature() {
        return temperature;
    }

    public ArrayList<String> getWind() {
        return wind;
    }

    public ArrayList<String> getCenter() {
        return center;
    }

    public ArrayList<String> getWho() {
        return who;
    }

    public ArrayList<String> getNotes() { return notes; }

    public int getTot_measure() {
        return tot_measure;
    }

    public String getAscii_name() {
        return ascii_name;
    }

    public String getState() {
        return state;
    }

    public String getGeonameID() {
        return geonameID;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setAscii_name(String ascii_name) {
        this.ascii_name = ascii_name;
    }

    public void setGeonameID(String geonameID) {
        this.geonameID = geonameID;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTot_measure(int tot_measure) {
        this.tot_measure = tot_measure;
    }

    /**
     * Format a json string in order to write it in the climate params file.
     * @return String
     */
    public String toJson() {
        String out = String.format(
                    jsonFormat,
                    state,
                    geonameID,
                    ascii_name,
                    wind,
                    humidity,
                    pressure,
                    temperature,
                    rainfall,
                    glacier_alt,
                    glacier_mass,
                    tot_measure,
                    notes,
                    date,
                    center,
                    who
                );
        return out;
    }

}