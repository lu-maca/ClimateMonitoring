/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import uni.climatemonitor.common.IDatabaseService;
import uni.climatemonitor.common.Operator;
import uni.climatemonitor.generics.Constants;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Singleton for utilities
 *
 * @see <a href="https://www.baeldung.com/java-singleton">Singleton</a>
 */
public final class UtilsSingleton {
    private JPanel PageSelector;
    private DetailsPage DetailsPnl;
    private boolean isLoggedIn;
    private Operator whoisLoggedIn;
    private IDatabaseService dbService;

    private static UtilsSingleton INSTANCE = null;

    private UtilsSingleton() throws IOException {
        whoisLoggedIn = null;
        isLoggedIn = false;
        dbService = null;
    }

    /**
     * This method shall be invoked for in the place of the constructor
     * (this is the core implementation of the singleton)
     * @return
     */
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

    /**
     * Set infos for the mutually exclusive container that can be carried between
     * different classes
     * @param pageSelector
     * @param detailsPnl
     */
    public void setMExCInfo(JPanel pageSelector, DetailsPage detailsPnl){
        PageSelector = pageSelector;
        DetailsPnl = detailsPnl;
    }

    /**
     * switch between pages in the mutually exclusive container
     * @param pageName
     */
    public void switchPage(String pageName){
        CardLayout cl = (CardLayout)(PageSelector.getLayout());
        cl.show(PageSelector, pageName);
    }

    /**
     * Utility for a generic text field, to make it empty when the cursor enter the
     * field if it contains oldString
     * @param f
     * @param oldString
     */
    public void textFieldEnter(JTextField f, String oldString){
        /* if the text in the text field is equal to  oldString
            it changes it to EMPTY string
         */
        if (f.getText().equals(oldString)) {
            f.setText(Constants.EMPTY_S);
        }
        f.setForeground(new Color(0,0,0));
    }

    /**
     * Utility for a generic text field, to reset its value to newString when it loses
     * focus
     * @param f
     * @param newString
     */
    public void textFieldExit(JTextField f, String newString){
        /* if the text in the text field is EMPTY, it changes it to newString
         */
        if (f.getText().equals(Constants.EMPTY_S)) {
            f.setText(newString);
            f.setForeground(new Color(187,187,187));
        }
    }

    /**
     * Getter for the details panel object
     * @return DetailsPage
     */
    public DetailsPage getDetailsPnl() {
        return DetailsPnl;
    }

    /**
     * Check if someone is already logged in
     * @return true if someone is already logged in
     * @throws Exception
     */
    private boolean isSomeoneAlreadyLoggedIn() throws Exception {
        if (isLoggedIn && whoisLoggedIn != null){
            return true;
        } else if (isLoggedIn) {
            throw new Exception("Someone is logged in, but I don't know who it is.");
        }
        return false;
    }

    /**
     * Give the access to operator, registering the access in whoisLoggedIn
     * @param operator
     * @return true if the access is correctly given
     */
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

    /**
     * Logout the user
     */
    public void logoutUser(){
        whoisLoggedIn = null;
        isLoggedIn = false;
    }

    public void setDbService(IDatabaseService dbService) {
        this.dbService = dbService;
    }

    public IDatabaseService getDbService() {
        return dbService;
    }
}
