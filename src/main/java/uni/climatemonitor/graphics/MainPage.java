package uni.climatemonitor.graphics;

import org.json.simple.parser.ParseException;
import uni.climatemonitor.data.ClimateParams;
import uni.climatemonitor.data.GeoData;
import uni.climatemonitor.data.Location;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

public class MainPage {
    private JPanel ParentPnl;
    private JPanel MainPnl;
    private JPanel UserPnl;
    private JPanel LoginPnl;
    private JTextField userLoginTextField;
    private JPasswordField pwdLoginTextField;
    private JButton loginEnterBtn;
    private JButton loginExitButton;
    private JPanel ButtonsPnl;
    private JButton LoginBtn;
    private JButton RegisterBtn;
    private JPanel AboutPnl;
    private JButton aboutButton;
    private JPanel SearchPnl;
    private JTextField typeAPlaceTextField;
    private JButton searchBtn;
    private JLabel Logo;
    private JPanel SearchListPnl;
    private JScrollPane SearchListScrollPnl;
    private JList SearchList;

    /* utilities */
    private DefaultListModel<Location> searchListModel;
    private GeoData geoData = new GeoData();
    public Location clickedElement;

    private final Border userLoginTextFieldBorder = userLoginTextField.getBorder();
    private final Border pwdLoginTextFieldBorder = pwdLoginTextField.getBorder();

    public MainPage() throws ParseException, IOException {
        /* set the logo */
        ImageIcon iconLogo = new ImageIcon(Constants.LOGO_PATH_S);
        Logo.setIcon(iconLogo);

        /* set initial search list and its gui options */
        searchListModel = new DefaultListModel<>();
        for (Location elem : geoData.getGeoLocationsList()) {
            searchListModel.addElement(elem);
        }
        SearchList.setModel(searchListModel);
        SearchList.setVisibleRowCount(16);
        SearchList.setBackground(new Color(238, 238, 238));
        SearchListPnl.setVisible(false);

        /*
            Callbacks for the main page
         */

        /* perform a search action when clicking the search button */
        searchBtn_at_click();
        /* Location search methods:
            - remove the text when selecting the search field
            - analyze text changes
        */
        placeTextField_at_selection();
        placeTextField_at_text_change();
        /* Location list selection */
        searchList_at_selection();
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
                about.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
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
                UtilsSingleton utils = UtilsSingleton.getInstance();
                utils.textFieldEnter(typeAPlaceTextField, Constants.TYPE_A_PLACE_S);
            }

            @Override
            public void focusLost(FocusEvent e) {
                UtilsSingleton utils = UtilsSingleton.getInstance();
                utils.textFieldExit(typeAPlaceTextField, Constants.TYPE_A_PLACE_S);
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
                for (Location l : geoData.getGeoLocationsList()) {
                    if (!l.toString().contains(filter)) {
                        if (searchListModel.contains(l)) {
                            searchListModel.removeElement(l);
                        }
                    } else {
                        if (!searchListModel.contains(l)) {
                            searchListModel.addElement(l);
                        }
                    }
                }
            }

        });
    }


    /**
     * Callback for selection of a location on the location list (with a
     * double click)
     */
    private void searchList_at_selection(){
        SearchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    UtilsSingleton utils = UtilsSingleton.getInstance();
                    clickedElement = (Location) SearchList.getSelectedValue();

                    /*
                    * get the climate parameters for the clicked element filtering
                    * between the climate params structure through the geoname ID (that
                    * is unique); if the climate params for the clicked place does not
                    * exist, it means that no operator has inserted it: in this case a
                    * null object will be passed to the details panel and will be handled
                    * by the details panel itself.
                     */
                    ClimateParams climateParams = geoData.getClimateParamsFor(clickedElement.getGeonameID());

                    utils.DetailsPnl.setUIPnl(clickedElement, climateParams);
                    utils.switchPage("Location Details Page");

                    typeAPlaceTextField.setText("");
                    utils.textFieldExit(typeAPlaceTextField, "Type a place...");
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
                UtilsSingleton utils = UtilsSingleton.getInstance();
                utils.textFieldEnter(userLoginTextField, Constants.USERNAME_S);
            }

            @Override
            public void focusLost(FocusEvent e) {
                UtilsSingleton utils = UtilsSingleton.getInstance();
                utils.textFieldExit(userLoginTextField, Constants.USERNAME_S);
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
                if (Arrays.equals(pwdLoginTextField.getPassword(), Constants.EMPTY_S.toCharArray())) {
                    pwdLoginTextField.setText(Constants.PWD_S);
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
                userLoginTextField.setText(Constants.USERNAME_S);
                userLoginTextField.setForeground(new Color(187,187,187));
                pwdLoginTextField.setText(Constants.PWD_S);
                pwdLoginTextField.setForeground(new Color(187,187,187));
            }
        });
    }

}