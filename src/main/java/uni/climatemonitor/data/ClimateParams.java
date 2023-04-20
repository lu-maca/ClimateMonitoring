package uni.climatemonitor.data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    private int tot_measure;
    private String geonameID;
    private String notes;
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
    "notes": "%s",
    "date": %s
  }""";

    public ClimateParams(){}

    public ClimateParams(HashMap o){
        for (Object key: o.keySet()){
            switch (key.toString()) {
                case "ascii_name":
                    this.ascii_name = o.get(key).toString();
                    break;
                case "state":
                    this.state = o.get(key).toString();
                    break;
                case "wind":
                    this.wind = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "humidity":
                    this.humidity = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "pressure":
                    this.pressure = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "temperature":
                    this.temperature = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "rainfall":
                    this.rainfall = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "glaciers_alt":
                    this.glacier_alt = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "glaciers_mass":
                    this.glacier_mass = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "tot_measure":
                    this.tot_measure = Math.toIntExact((long) o.get(key));
                    break;
                case "notes":
                    this.notes = o.get(key).toString();
                    break;
                case "geoname_id":
                    this.geonameID = o.get(key).toString();
                    break;
                case "date":
                    this.date = unpackStringToStringArray(o.get(key).toString());
                    break;
            }
        }
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

    public int getTot_measure() {
        return tot_measure;
    }

    public String getAscii_name() {
        return ascii_name;
    }

    public String getNotes() {
        return notes;
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

    public ArrayList<String> getBeautifulDate() {
        ArrayList<String> beautifulDate = new ArrayList<>();
        for (String s : date) {
            beautifulDate.add(s.replaceAll("\"", ""));
        }
        return beautifulDate;
    }

    public void setAscii_name(String ascii_name) {
        this.ascii_name = ascii_name;
    }

    public void setGeonameID(String geonameID) {
        this.geonameID = geonameID;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTot_measure(int tot_measure) {
        this.tot_measure = tot_measure;
    }

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
                date
                );
        return out;
    }

}