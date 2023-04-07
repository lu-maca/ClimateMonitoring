package uni.climatemonitor.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.Dialog.ModalityType;
import java.util.Arrays;

public class MainWindow extends JFrame {
    private static final String TYPE_A_PLACE_S = "Type a place...";
    private static final String EMPTY_S = "";
    private static final String USERNAME_S = "Username";
    private static final String PWD_S = "Password";

    private JPanel MainWindow;
    private JPanel MainPnl;
    private JButton LoginBtn;
    private JButton RegisterBtn;
    private JPanel UserPnl;
    private JPanel AboutPnl;
    private JButton aboutButton;
    private JTextField typeAPlaceTextField;
    private JButton searchBtn;
    private JPanel SearchPnl;
    private JPanel LoginPnl;
    private JTextField userLoginTextField;
    private JPasswordField pwdLoginTextField;
    private JPanel ButtonsPnl;
    private JButton loginEnterBtn;

    /**
     * Constructor for the MainWindow object.
     */
    public MainWindow() {
        setTitle("Climate Monitor");
        setSize(1000,600);
        setMinimumSize(new Dimension(1000,600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(MainWindow);
        setVisible(true);

        /* perform a search action when clicking the search button */
        searchBtn_at_click();
        /* remove the text when selecting the search field */
        placeTextField_at_selection();
        /* open the about popup clicking on the About button */
        aboutBtn_at_click();
        /* open the login panel clicking on the Login button */
        loginBtn_at_click();
        /* remove the text when selecting the username field */
        userLoginTextField_at_selection();
        /* remove the text when selecting the pwd field */
        pwdLoginTextField_at_selection();
        /* manage the login Enter button click */
        loginEnterBtn_at_click();
    }

    /*************************************************************

     UTILS

     */

    private static void textFieldEnter(JTextField f, String oldString){
        if (f.getText().equals(oldString)) {
            f.setText(EMPTY_S);
        }
        f.setForeground(new Color(0,0,0));
    }

    private static void textFieldExit(JTextField f, String newString){
        if (f.getText().equals(EMPTY_S)) {
            f.setText(newString);
            f.setForeground(new Color(187,187,187));
        }
    }

    /*************************************************************

     CALLBACKS

     */

    /**
     * Callback for About button push. Open the dialog with some info
     * about the authors and the project.
     *
     */
    private void aboutBtn_at_click(){
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog about = new AboutDialog();
                about.setModalityType(ModalityType.APPLICATION_MODAL);
                about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                about.setVisible(true);
            }
        });
    }


    /**
     * This is the callback of a push event on the Search button
     *
     */
    private void searchBtn_at_click(){
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* perform here some action */
            }
        });
    }


    /**
     * This is the callback for a selection of the place text edit field. It
     * removes the "Type a place..." text.
     *
     */
    private void placeTextField_at_selection(){
        typeAPlaceTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textFieldEnter(typeAPlaceTextField, TYPE_A_PLACE_S);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textFieldExit(typeAPlaceTextField, TYPE_A_PLACE_S);
            }
        });
    }


    /**
     * Callback for Login button. Show the login panel
     */
    private void loginBtn_at_click(){
        LoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonsPnl.setVisible(false);
                LoginPnl.setVisible(true);
            }
        });
    }


    /**
     * This is the callback for a selection of the username text edit field.
     * It removes the "Username" text.
     */
    private void userLoginTextField_at_selection(){
        userLoginTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textFieldEnter(userLoginTextField, USERNAME_S);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textFieldExit(userLoginTextField, USERNAME_S);
            }
        });
    }


    /**
     * This is the callback for a selection of the pwd text edit field.
     * It removes the "********" text.
     */
    private void pwdLoginTextField_at_selection(){
        pwdLoginTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                pwdLoginTextField.setText("");
                pwdLoginTextField.setForeground(new Color(0,0,0));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Arrays.equals(pwdLoginTextField.getPassword(), EMPTY_S.toCharArray())) {
                    pwdLoginTextField.setText(PWD_S);
                    pwdLoginTextField.setForeground(new Color(187,187,187));
                }
            }
        });
    }


    /**
     * This is the callback of a push event on the Search button
     *
     */
    private void loginEnterBtn_at_click(){
        loginEnterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* perform here some action:
                    if the user exists and the pwd is the correct one,
                    remove all the panel related to log in/register and
                    display some new buttons for specific tasks.
                    Otherwise, show again the login/register buttons
                    and warn the user for the wrong login request
                 */
            }
        });
    }

    /**
     * main method
     *
     */
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

}
