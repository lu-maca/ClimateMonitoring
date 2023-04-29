/*************************************************
 * CentersData class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;

import java.io.IOException;
import java.util.ArrayList;

public class CentersData {
    /* files */
    private OperatorsFileHandler operatorsFile;
    private MonitoringCentersFileHandler monitoringCentersFile;
    /* monitoring centers and operators */
    private ArrayList<Operator> operatorsList;
    private ArrayList<MonitoringCenter> monitoringCentersList;

    public CentersData() throws ParseException, IOException {
        operatorsFile = new OperatorsFileHandler(Constants.REGISTERED_OPS_S);
        monitoringCentersFile = new MonitoringCentersFileHandler(Constants.MONITORING_CENTERS_S);

        /* monitoring centers */
        getMonitoringCenters();

        /* operators */
        getOperators();
    }

    public void addOperator(Operator o){
        operatorsList.add(o);
    }

    public void addMonitoringCenter(MonitoringCenter mc){
        monitoringCentersList.add(mc);
    }

    private void getOperators() throws ParseException, IOException {
        operatorsFile.readFile();
        operatorsList = operatorsFile.getOperators();
    }

    private void getMonitoringCenters() throws ParseException, IOException {
        monitoringCentersFile.readFile();
        monitoringCentersList = monitoringCentersFile.getMonitoringCenters();
    }

    public Operator checkOperatorExistance(String username, char[] pwd){
        String pwd_s = new String(pwd);
        for (Operator o : operatorsList){
            if (o.getUsername().equals(username) && o.getPassword().equals(pwd_s)){
                return o;
            }
        }
        return null;
    }

    public ArrayList<String> getEnabledLocationsForOperator(Operator operator){
        String monitoringCenter = operator.getMonitoringCenter();

        for (MonitoringCenter m : monitoringCentersList){
            if (m.getName().equals(monitoringCenter)){
                return m.getMonitoredAreas();
            }
        }
        return null;
    }

    public MonitoringCenter getMonitoringCenterFromName(String name){
        for (MonitoringCenter mc : getMonitoringCentersList()) {
            if (mc.getName().equals(name)) {
                return mc;
            }
        }
        return null;
    }

    public ArrayList<MonitoringCenter> getMonitoringCentersList(){
        return monitoringCentersList;
    }

    public void updateOperatorsFile(){
        operatorsFile.writeFile();
    }

    public void updateMonitoringCentersFile(){
        monitoringCentersFile.writeFile();
    }
}
