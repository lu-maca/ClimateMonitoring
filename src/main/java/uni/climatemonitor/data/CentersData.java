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

    private void getOperators() throws ParseException, IOException {
        operatorsFile.readFile();
        operatorsList = operatorsFile.getOperators();
    }

    private void getMonitoringCenters() throws ParseException, IOException {
        monitoringCentersFile.readFile();
        monitoringCentersList = monitoringCentersFile.getMonitoringCenters();
    }

    public boolean checkOperatorExistance(String username, char[] pwd){
        String pwd_s = new String(pwd);
        System.out.println(username + " " + pwd_s);
        for (Operator o : operatorsList){
            System.out.println(o.getUsername() +  " " + o.getPassword());
            if (o.getUsername().equals(username) && o.getPassword().equals(pwd_s)){
                return true;
            }
        }
        return false;
    }
}
