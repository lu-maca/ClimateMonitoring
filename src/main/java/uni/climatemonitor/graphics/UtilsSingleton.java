package uni.climatemonitor.graphics;

import uni.climatemonitor.data.Operator;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import java.awt.*;

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
    private static UtilsSingleton INSTANCE = null;

    private UtilsSingleton(JPanel pageSelector, DetailsPage detailsPnl){
        PageSelector = pageSelector;
        DetailsPnl = detailsPnl;
        whoisLoggedIn = null;
        isLoggedIn = false;
    }

    public static UtilsSingleton getInstance(){
        return INSTANCE;
    }

    public static UtilsSingleton getInstance(JPanel pageSelector, DetailsPage detailsPnl){
        if (INSTANCE == null){
            INSTANCE = new UtilsSingleton(pageSelector, detailsPnl);
        }
        return INSTANCE;
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
}
