package uni.climatemonitor.data;
import java.util.HashMap;

public class ClimateParams {
    private String ascii_name;
    private String state;
    private float[] wind;
    private float[] humidity;
    private float[] pressure;
    private float[] temperature;
    private float[] rainfall;
    private float[] glacier_alt;
    private float[] glacier_mass;
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
                    this.wind = unpackStringToFloat(o.get(key).toString());
                    break;
                case "humidity":
                    this.humidity = unpackStringToFloat(o.get(key).toString());
                    break;
                case "pressure":
                    this.pressure = unpackStringToFloat(o.get(key).toString());
                    break;
                case "temperature":
                    this.temperature = unpackStringToFloat(o.get(key).toString());
                    break;
                case "rainfall":
                    this.rainfall = unpackStringToFloat(o.get(key).toString());
                    break;
                case "glacier_alt":
                    this.glacier_alt = unpackStringToFloat(o.get(key).toString());
                    break;
                case "glacier_mass":
                    this.glacier_mass = unpackStringToFloat(o.get(key).toString());
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

    private float[] unpackStringToFloat(String s){
        String sWithoutBrackets = s.replaceAll("\\[|\\]", "");
        String[] split = sWithoutBrackets.split(",");
        float[] out = new float[2];
        out[0] = Float.parseFloat(split[0]);
        out[1] = Float.parseFloat(split[1]);
        return out;
    }

    public float[] getGlacier_alt() {
        return glacier_alt;
    }

    public float[] getHumidity() {
        return humidity;
    }

    public float[] getGlacier_mass() {
        return glacier_mass;
    }

    public float[] getPressure() {
        return pressure;
    }

    public float[] getRainfall() {
        return rainfall;
    }

    public float[] getTemperature() {
        return temperature;
    }

    public float[] getWind() {
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