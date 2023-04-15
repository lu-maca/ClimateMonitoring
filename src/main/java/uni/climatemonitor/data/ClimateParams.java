package uni.climatemonitor.data;
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
                case "glacier_alt":
                    this.glacier_alt = unpackStringToStringArray(o.get(key).toString());
                    break;
                case "glacier_mass":
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
        String sWithoutBrackets = s.replaceAll("\\[|\\]", "");
        String[] split = sWithoutBrackets.split(",");
        String[] out = new String[2];
        out[0] = split[0];
        out[1] = split[1];
        return out;
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

    @Override
    public String toString() {
        return ascii_name + " " + state + " " +  wind[0] + wind[1] + " " + tot_measure;
    }
}