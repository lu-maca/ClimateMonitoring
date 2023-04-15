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
    private static JPanel MainPanel;
    private static JPanel DetailsPanel;
    private static UtilsSingleton INSTANCE = null;

    private UtilsSingleton(JPanel pageSelector, DetailsPage detailsPnl, JPanel details, JPanel main){
        PageSelector = pageSelector;
        DetailsPnl = detailsPnl;
        MainPanel = main;
        DetailsPanel = details;
    }

    public static UtilsSingleton getInstance(){
        return INSTANCE;
    }

    public static UtilsSingleton getInstance(JPanel pageSelector, DetailsPage detailsPnl, JPanel details, JPanel main){
        if (INSTANCE == null){
            INSTANCE = new UtilsSingleton(pageSelector, detailsPnl, details, main);
        }
        return INSTANCE;
    }

    public void switchPage(String pageName){
        CardLayout cl = (CardLayout)(PageSelector.getLayout());
        cl.show(PageSelector, pageName);

        switch (pageName){
            case "Main Page":
                MainPanel.setFocusable(true);
                DetailsPanel.setFocusable(false);
                break;
            case "Location Details Page":
                MainPanel.setFocusable(false);
                DetailsPanel.setFocusable(true);
                break;
        }

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
