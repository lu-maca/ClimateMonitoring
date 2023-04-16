package uni.climatemonitor.graphics;

import uni.climatemonitor.Main;
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
    public DetailsPage DetailsPnl;
    private static UtilsSingleton INSTANCE = null;

    private UtilsSingleton(JPanel pageSelector, DetailsPage detailsPnl){
        PageSelector = pageSelector;
        DetailsPnl = detailsPnl;
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


}
