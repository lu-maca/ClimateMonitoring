/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import org.json.simple.parser.ParseException;
import uni.climatemonitor.data.CentersData;
import uni.climatemonitor.data.GeoData;
import uni.climatemonitor.data.Operator;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Singleton for utilities
 *
 * @see <a href="https://www.baeldung.com/java-singleton">...</a>
 */
public final class UtilsSingleton {
    private JPanel PageSelector;
    private DetailsPage DetailsPnl;
    private boolean isLoggedIn;
    private Operator whoisLoggedIn;
    private GeoData geoData;
    private CentersData centersData;

    private static UtilsSingleton INSTANCE = null;

    private UtilsSingleton() throws ParseException, IOException {
        geoData = new GeoData();
        centersData  = new CentersData();
        whoisLoggedIn = null;
        isLoggedIn = false;
    }

    public static UtilsSingleton getInstance(){
        if (INSTANCE == null){
            try {
                INSTANCE = new UtilsSingleton();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    public void setMExCInfo(JPanel pageSelector, DetailsPage detailsPnl){
        PageSelector = pageSelector;
        DetailsPnl = detailsPnl;
    }


    public void switchPage(String pageName){
        CardLayout cl = (CardLayout)(PageSelector.getLayout());
        cl.show(PageSelector, pageName);
    }

    public void textFieldEnter(JTextField f, String oldString){
        /* if the text in the text field is equal to  oldString
            it changes it to EMPTY string
         */
        if (f.getText().equals(oldString)) {
            f.setText(Constants.EMPTY_S);
        }
        f.setForeground(new Color(0,0,0));
    }

    public void textFieldExit(JTextField f, String newString){
        /* if the text in the text field is EMPTY, it changes it to newString
         */
        if (f.getText().equals(Constants.EMPTY_S)) {
            f.setText(newString);
            f.setForeground(new Color(187,187,187));
        }
    }

    public DetailsPage getDetailsPnl() {
        return DetailsPnl;
    }

    private boolean isSomeoneAlreadyLoggedIn() throws Exception {
        if (isLoggedIn && whoisLoggedIn != null){
            return true;
        } else if (isLoggedIn) {
            throw new Exception("Someone is logged in, but I don't know who it is.");
        }
        return false;
    }

    public boolean giveAccessTo(Operator operator){
        try{
            if (isSomeoneAlreadyLoggedIn()){
                return false;
            }
            whoisLoggedIn = operator;
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Operator getWhoisLoggedIn() {
        return whoisLoggedIn;
    }

    public void logoutUser(){
        whoisLoggedIn = null;
        isLoggedIn = false;
    }

    public CentersData getCentersData() {
        return centersData;
    }

    public GeoData getGeoData() {
        return geoData;
    }
}
