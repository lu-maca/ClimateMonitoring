package uni.climatemonitor.graphics;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow extends JFrame {
    /* constants */
    private static final String TYPE_A_PLACE_S = "Type a place...";
    private static final String EMPTY_S = "";
    private static final String USERNAME_S = "Username";
    private static final String PWD_S = "Password";

    /* GUI elements */
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
    private JLabel Logo;
    private JButton loginExitButton;
    private JList SearchList;
    private JPanel SearchListPnl;

    /* utilities */
    private DefaultListModel<String> searchListModel;

    private final Border userLoginTextFieldBorder = userLoginTextField.getBorder();
    private final Border pwdLoginTextFieldBorder = pwdLoginTextField.getBorder();

    /**
     * Constructor for the MainWindow object.
     */
    public MainWindow() {
        setTitle(Constants.APP_NAME_S);
        setSize(1100,600);
        setMinimumSize(new Dimension(1100,600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /* set the logo */
        ImageIcon iconLogo = new ImageIcon(Constants.LOGO_PATH_S);
        Logo.setIcon(iconLogo);
        ImageIcon earthIcon = new ImageIcon(Constants.EARTH_LOGO_PATH_S);
        setIconImage(earthIcon.getImage());

        /* set initial search list and its gui options */
        searchListModel = new DefaultListModel<>();
        for (String elem : Constants.prova) {
            searchListModel.addElement(elem);
        }
        SearchList.setModel(searchListModel);
        SearchList.setBackground(new Color(238, 238, 238));

        SearchListPnl.setVisible(false);

        setContentPane(MainWindow);
        setVisible(true);

        /* perform a search action when clicking the search button */
        searchBtn_at_click();
        /* Location search methods:
            - remove the text when selecting the search field
            - analyze text changes
        */
        placeTextField_at_selection();
        placeTextField_at_text_change();
        /* open the about popup clicking on the About button */
        aboutBtn_at_click();
        /* open the login panel clicking on the Login button */
        loginBtn_at_click();
        /* remove the text when selecting the username field */
        userLoginTextField_at_selection();
        /* remove the text when selecting the pwd field */
        pwdLoginTextField_at_selection();
        /* login panel management:
             - click ESC keyboard button
             - click ENTER keyboard button
             - ...
         */
        loginEnterBtn_at_click();
        loginExitBtn_at_click();

    }

    /*************************************************************

     UTILS

     */

    private void textFieldEnter(JTextField f, String oldString){
        /* if the text in the text field is equal to  oldString
            it changes it to EMPTY string
         */
        if (f.getText().equals(oldString)) {
            f.setText(EMPTY_S);
        }
        f.setForeground(new Color(0,0,0));
    }

    private void textFieldExit(JTextField f, String newString){
        /* if the text in the text field is EMPTY, it changes it to newString
         */
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
     * This is the callback for a change in the location text edit field
     */
    private void placeTextField_at_text_change(){
        typeAPlaceTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { }

            @Override
            public void insertUpdate(DocumentEvent e){ suggest(); }

            @Override
            public void removeUpdate(DocumentEvent e){ suggest(); }

            public void suggest() {
                String searched = typeAPlaceTextField.getText();
                if (searched.length() == 1){
                    SearchListPnl.setVisible(true);
                } else if (searched.length() == 0){
                    SearchListPnl.setVisible(false);
                    SearchList.clearSelection();
                }
                /* filter places */
                filterModel(searched);
            }

            public void filterModel(String filter) {
                for (String s : Constants.prova) {
                    if (!s.contains(filter)) {
                        if (searchListModel.contains(s)) {
                            searchListModel.removeElement(s);
                        }
                    } else {
                        if (!searchListModel.contains(s)) {
                            searchListModel.addElement(s);
                        }
                    }
                }
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
                userLoginTextField.setBorder(userLoginTextFieldBorder);
                pwdLoginTextField.setBorder(pwdLoginTextFieldBorder);
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
                boolean isValid = false;
                if (isValid) {
                    /* if the user exists... */
                    LoginPnl.setVisible(false);
                } else {

                    userLoginTextField.setBorder(new LineBorder(Color.RED, 3));
                    pwdLoginTextField.setBorder(new LineBorder(Color.RED, 3));
                }
            }
        });
    }


    /**
     *
     */
    private void loginExitBtn_at_click() {
        loginExitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPnl.setVisible(true);
                LoginPnl.setVisible(false);
                ButtonsPnl.setVisible(true);

                /* restore initial configuration for username and pwd */
                userLoginTextField.setText(USERNAME_S);
                userLoginTextField.setForeground(new Color(187,187,187));
                pwdLoginTextField.setText(PWD_S);
                pwdLoginTextField.setForeground(new Color(187,187,187));
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
