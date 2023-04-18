package uni.climatemonitor.data;
import java.util.Arrays;
import java.util.HashMap;

public class ClimateParams {
    private String ascii_name;
    private String state;
    private String[] wind;
    private String[] humidity;
    private String[] pressure;
    private String[] temperature;
    private String[] rainfall;
    private String[] glacier_alt;
    private String[] glacier_mass;
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
    "notes": "%s"
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
            }
        }
    }

    private String[] unpackStringToStringArray(String s){
        String sWithoutBrackets = s.replaceAll("\\[|\\]", "").trim();
        String[] split = sWithoutBrackets.split(",");

        return split;
    }

    public String[] getGlacier_alt() {
        return glacier_alt;
    }

    public String[] getHumidity() {
        return humidity;
    }

    public String[] getGlacier_mass() {
        return glacier_mass;
    }

    public String[] getPressure() {
        return pressure;
    }

    public String[] getRainfall() {
        return rainfall;
    }

    public String[] getTemperature() {
        return temperature;
    }

    public String[] getWind() {
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

    public void setAscii_name(String ascii_name) {
        this.ascii_name = ascii_name;
    }

    public void setGeonameID(String geonameID) {
        this.geonameID = geonameID;
    }

    public void setGlacier_alt(String[] glacier_alt) {
        this.glacier_alt = glacier_alt;
    }

    public void setGlacier_mass(String[] glacier_mass) {
        this.glacier_mass = glacier_mass;
    }

    public void setHumidity(String[] humidity) {
        this.humidity = humidity;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPressure(String[] pressure) {
        this.pressure = pressure;
    }

    public void setRainfall(String[] rainfall) {
        this.rainfall = rainfall;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTemperature(String[] temperature) {
        this.temperature = temperature;
    }

    public void setTot_measure(int tot_measure) {
        this.tot_measure = tot_measure;
    }

    public void setWind(String[] wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return ascii_name + " " + state + " " +  wind[0] + wind[1] + " " + tot_measure;
    }

    public String toJson() {
        System.out.println(wind.toString());
        System.out.println("[" + wind[0] + "," + wind[1] + "]");
        String windS = "[" + wind[0] + "," + wind[1] + "]";
        String out = String.format(
                jsonFormat,
                state,
                geonameID,
                ascii_name,
                Arrays.toString(wind),
                Arrays.toString(humidity),
                Arrays.toString(pressure),
                Arrays.toString(temperature),
                Arrays.toString(rainfall),
                Arrays.toString(glacier_alt),
                Arrays.toString(glacier_mass),
                tot_measure,
                notes
                );
        return out;
    }

}