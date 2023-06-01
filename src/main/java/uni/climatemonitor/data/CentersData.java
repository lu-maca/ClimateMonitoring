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

/**
 * This class exposes some methods to handle monitoring centers and operators.
 */
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

    /**
     * Add an operator to the registered operators list
     * @param o operator to add
     */
    public void addOperator(Operator o){
        operatorsList.add(o);
    }

    /**
     * Add a monitoring center to the monitoring centers list
     * @param mc monitoring center to add
     */
    public void addMonitoringCenter(MonitoringCenter mc){
        monitoringCentersList.add(mc);
    }

    /**
     * Parse the operators file, in order to initialize the operator list
     * @throws ParseException
     * @throws IOException
     */
    private void getOperators() throws ParseException, IOException {
        operatorsFile.readFile();
        operatorsList = operatorsFile.getOperators();
    }

    /**
     * Parse the monitoring center file, in order to initialize the
     * monitoring center list
     * @throws ParseException
     * @throws IOException
     */
    private void getMonitoringCenters() throws ParseException, IOException {
        monitoringCentersFile.readFile();
        monitoringCentersList = monitoringCentersFile.getMonitoringCenters();
    }

    /**
     * Check the pwd of an operator with a given username in the operators list.
     * @param username username of the operator
     * @param pwd pwd to check
     * @return the instance of {@link Operator} with the corresponding username
     * and pwd, if existing; otherwise return null
     */
    public Operator checkOperatorExistance(String username, char[] pwd){
        String pwd_s = new String(pwd);
        for (Operator o : operatorsList){
            if (o.getUsername().equals(username) && o.getPassword().equals(pwd_s)){
                return o;
            }
        }
        return null;
    }

    /**
     * Get the list of the locations for which an operator is enabled
     * @param operator
     * @return the list of the locations for which an operator is enabled
     */
    public ArrayList<String> getEnabledLocationsForOperator(Operator operator){
        String monitoringCenter = operator.getMonitoringCenter();

        for (MonitoringCenter m : monitoringCentersList){
            if (m.getName().equals(monitoringCenter)){
                return m.getMonitoredAreas();
            }
        }
        return null;
    }

    /**
     * Retrieve the {@link MonitoringCenter} object that has the given name
     * @param name name of the monitoring center
     * @return {@link MonitoringCenter}
     */
    public MonitoringCenter getMonitoringCenterFromName(String name){
        for (MonitoringCenter mc : getMonitoringCentersList()) {
             if (mc.getName().equals(name)) {
                return mc;
            }
        }
        return null;
    }

    /**
     * Getter for the monitoring centers list
     * @return ArrayList<MonitoringCenter>
     */
    public ArrayList<MonitoringCenter> getMonitoringCentersList(){
        return monitoringCentersList;
    }

    /**
     * Update the operators file
     */
    public void updateOperatorsFile(){
        operatorsFile.writeFile();
    }

    /**
     * Update the monitoring centers file
     */
    public void updateMonitoringCentersFile(){
        monitoringCentersFile.writeFile();
    }
}
