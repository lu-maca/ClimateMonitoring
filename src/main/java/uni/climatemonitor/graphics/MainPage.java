/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import uni.climatemonitor.generics.Constants;
import uni.climatemonitor.common.Location;
import uni.climatemonitor.common.MonitoringCenter;
import uni.climatemonitor.common.Coordinates;
import uni.climatemonitor.common.Operator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the main page of the UI
 */
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
    private JButton LogoutBtn;
    private JLabel WelcomeBackLbl;
    private JPanel LoginBtnsPnl;
    private JPanel LogoutBtnPnl;
    private JPanel UserMessagesPnl;
    private JPanel UserGeneralPnl;
    private JPanel UserInfoPnl;
    private JLabel NameLbl;
    private JTextField NameTextField;
    private JTextField SurnameTextFIeld;
    private JLabel SurnameLbl;
    private JLabel TaxCodeLbl;
    private JTextField TaxCodeTextField;
    private JLabel EmailLbl;
    private JTextField EmailTextField;
    private JLabel UsernameLbl;
    private JTextField UsernameTextField;
    private JLabel PwdLbl;
    private JTextField PwdTextField;
    private JPanel MonitoringCenterPnl;
    private JLabel MonitoringCenterNameLbl;
    private JTextField AddressTextField;
    private JLabel AddressLbl;
    private JPanel RegistrationPnl;
    private JButton ConfirmRegisterBtn;
    private JPanel RegistrationPnlButtonsPnl;
    private JButton ExitFromRegistrationBtn;
    private JComboBox MonitoringCenterComboBox;
    private JTextField NewNameTextField;
    private JLabel NewNameLbl;
    private JPanel IsNewPnl;
    private JCheckBox IsNewCheckBox;
    private JLabel NewMonCenterLbl;
    private JPanel CenterCreationOptionPnl;
    private JPanel ExistingMonCenterPnl;
    private JList NewMonitoredAreasList;
    private JPanel NewMonitoredAreasPnl;
    private JLabel SelectedAreasLbl;
    private JButton AddNewBtn;

    /* utilities for locations */
    private DefaultListModel<Location> searchListModel = new DefaultListModel<>();
    private Location clickedElement;

    /* utilities for registration */
    ArrayList<MonitoringCenter> mcs;
    private DefaultComboBoxModel<MonitoringCenter> monitoringCenterComboBoxModel = new DefaultComboBoxModel<>();
    private DefaultListModel<Location> newSelectedAreasModel = new DefaultListModel<>();
    private boolean isRegistrationModeActive = false;

    private final Border userLoginTextFieldBorder = userLoginTextField.getBorder();
    private final Border pwdLoginTextFieldBorder = pwdLoginTextField.getBorder();

    /* utils */
    UtilsSingleton utils;

    public MainPage() {
        utils = UtilsSingleton.getInstance();

        /* set the logo */
        URL imgURL = getClass().getResource(Constants.LOGO_PATH_S);
        ImageIcon image = new ImageIcon(imgURL);
        Logo.setIcon(image);

        /* set initial search list and its gui options */
        SearchList.setModel(searchListModel);
        SearchList.setVisibleRowCount(16);
        SearchList.setBackground(new Color(238, 238, 238));
        SearchListPnl.setVisible(false);

        /* set initial combo box model for the creation of new monitoring centers */
        monitoringCenterComboBoxModel = new DefaultComboBoxModel<>();

        MonitoringCenterComboBox.setModel(monitoringCenterComboBoxModel);
        newSelectedAreasModel = new DefaultListModel<>();
        NewMonitoredAreasList.setModel(newSelectedAreasModel);
        NewMonitoredAreasPnl.setVisible(false);


        /* set initial login */
        LogoutBtnPnl.setVisible(false);

        /* set initial registration */
        RegistrationPnl.setVisible(false);

        /*
            Callbacks for the main page
         */

        /* main panel visibility check; if not visible, reset everything needs to be reset here*/
        MainPnl_at_visibility_change();

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
        /* registration panel management */
        registerBtn_at_click();
        /* new center options */
        isNewCheckBox_at_selection();
        /* remove previously selected area */
        newMonitoredAreasList_at_selection();
        /* confirm registration */
        confirmRegisterBtn_at_click();
        /* registration panel exit */
        exitFromRegistrationBtn_at_click();
        /* restore main page to initial condition when logout button is pushed */
        logoutBtn_at_click();
        /* add new area */
        addNewBtn_at_click();
    }

    /*************************************************************

     UTILS

     */

    /**
     * Show the hidden message on the low-right corner
     * @param message
     */
    private void showMessage(String message) {
        WelcomeBackLbl.setText(message);
        UserMessagesPnl.setVisible(true);
    }

    /**
     * set/reset the log mode (logged in/logged out, accordingly to the parameter)
     * @param isLoggedInModeActive
     */
    private void setLoggedInMode(boolean isLoggedInModeActive){
        LoginBtnsPnl.setVisible(!isLoggedInModeActive);
        LogoutBtnPnl.setVisible(isLoggedInModeActive);
        UserMessagesPnl.setVisible(isLoggedInModeActive);
    }

    /**
     * Reset the registration form when it is closed
     */
    public void resetRegistrationForm(){
        /* new operator options reset */
        NameTextField.setText("");
        SurnameTextFIeld.setText("");
        TaxCodeTextField.setText("");
        EmailTextField.setText("");
        UsernameTextField.setText("");
        PwdTextField.setText("");
        NewNameTextField.setText("");

        /* new monitoring center options reset */
        ExistingMonCenterPnl.setVisible(true);
        CenterCreationOptionPnl.setVisible(false);
        NewMonitoredAreasPnl.setVisible(false);
        IsNewCheckBox.setSelected(false);

        /* reset registration mode */
        isRegistrationModeActive = false;

        /* restore the main page in the initial condition */
        ButtonsPnl.setVisible(true);
        RegistrationPnl.setVisible(false);

        /* reset new monitoring center options */
        NewNameTextField.setText("");
        AddressTextField.setText("");
        newSelectedAreasModel.clear();
    }

    /*************************************************************

     CALLBACKS

     */

    /**
     * At visibility change of the main panel, perform some reset actions,
     * for example:
     *  - remove the visibility of the suggestion list
     *  - remove the login popup
     */
    private void MainPnl_at_visibility_change(){
        MainPnl.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e)
            {
                JComponent component = (JComponent)e.getSource();
                if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                        &&  component.isShowing()){
                    SearchListPnl.setVisible(false);
                    LoginPnl.setVisible(false);
                    ButtonsPnl.setVisible(true);
                }
            }
        });
    };


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
     * This is the callback for a selection of the place text edit field. It
     * removes the "Type a place..." text.
     *
     */
    private void placeTextField_at_selection(){
        typeAPlaceTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                utils.textFieldEnter(typeAPlaceTextField, Constants.TYPE_A_PLACE_S);
                typeAPlaceTextField.getDocument().addDocumentListener(searchFieldListener);
            }

            @Override
            public void focusLost(FocusEvent e) {
                typeAPlaceTextField.getDocument().removeDocumentListener(searchFieldListener);
                utils.textFieldExit(typeAPlaceTextField, Constants.TYPE_A_PLACE_S);
            }
        });
    }


    /**
     * Definition of the action listener for the search field
     */
    private DocumentListener searchFieldListener = new DocumentListener() {
        @Override
        public void changedUpdate(DocumentEvent e) { }

        @Override
        public void insertUpdate(DocumentEvent e){
            if (typeAPlaceTextField.getText().length() > 1) {
                suggest();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e){
            suggest();
        }

        public void suggest() {
            String searched = typeAPlaceTextField.getText();
            /* filter places:
                if the searched string does not contain numbers,
                it is a simple search, but if contains numbers (or +/-)
                it is a lat-long search
             */
            final Pattern pattern = Pattern.compile("(\\d+\\.\\d+)° (N|S) (\\d+\\.\\d+)° (E|W)");
            Matcher matcher = pattern.matcher(searched);
            if ( ! matcher.matches() ) {
                filterModel(searched);
            } else {
                /* get the coordinates value with signs */
                double latSign = (matcher.group(2).equals("N")) ? 1.0 : -1.0;
                double lat = latSign * Double.parseDouble(matcher.group(1));
                double longSign = (matcher.group(4).equals("E")) ? 1.0 : -1.0;
                double lon = longSign * Double.parseDouble(matcher.group(3));

                Coordinates searchedCoordinates = new Coordinates(lat, lon);

                filterModelByCoordinates(searchedCoordinates);
            }

            /* set visibility */
            if (! searchListModel.isEmpty() && searched.length() > 0){
                SearchListPnl.setVisible(true);
            } else {
                SearchListPnl.setVisible(false);
                SearchList.clearSelection();
            }

        }

        /**
         * Filter by name
         * @param filter
         */
        private void filterModel(String filter) {
            ArrayList<Location> filteredLocations;
            try {
                filteredLocations = utils.getDbService().filterLocationsByName(filter);
            } catch (RemoteException e) {
                return;
            }
            /* clear the search list model and populate it */
            searchListModel.clear();
            for (Location l : filteredLocations) {
                searchListModel.addElement(l);
            }
        }

        /**
         * Filter by coordinates
         * @param coordinates
         */
        private void filterModelByCoordinates(Coordinates coordinates) {
            ArrayList<Location> filteredLocations;
            try {
                filteredLocations = utils.getDbService().filterLocationsByCoordinates(coordinates);
            } catch (RemoteException e) {
                return;
            }
            /* clear the search list model and populate it */
            searchListModel.clear();
            for (Location l : filteredLocations){
                searchListModel.addElement(l);
            }
        }
    };


    /**
     * This is the callback for a change in the location text edit field
     */
    private void placeTextField_at_text_change(){

    }


    /**
     * Callback for selection of a location on the location list (with a
     * double click)
     */
    private void searchList_at_selection(){
        SearchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                clickedElement = (Location) SearchList.getSelectedValue();
                if (clickedElement == null) { return; }

                /* if registration mode is active, add the clicked element to the list of selected areas,
                * else open the details page */
                if (! isRegistrationModeActive) {
                    utils.getDetailsPnl().setUIPnl(clickedElement);
                    utils.switchPage("Location Details Page");
                } else {
                    if (! newSelectedAreasModel.contains(clickedElement)) {
                        newSelectedAreasModel.addElement(clickedElement);
                    }
                    SearchListPnl.setVisible(false);
                }

                /* remove the document listener to avoid infinite loops */
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
     * Callback for Register button push. It shall open the registration panel
     * without showing the options for new centers.
     */
    private void registerBtn_at_click(){
        RegisterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get all monitoring centers
                monitoringCenterComboBoxModel.removeAllElements();
                try {
                    mcs = utils.getDbService().getAllMonitoringCenters();
                } catch (RemoteException ee) {
                    mcs = new ArrayList<>();
                }
                for (MonitoringCenter mc : mcs){
                    monitoringCenterComboBoxModel.addElement(mc);
                }
                MonitoringCenterComboBox.setModel(monitoringCenterComboBoxModel);

                ButtonsPnl.setVisible(false);
                CenterCreationOptionPnl.setVisible(false);
                RegistrationPnl.setVisible(true);
                boolean noMonitoringCenters = false;
                try {
                    noMonitoringCenters = utils.getDbService().isMonitoringCentersTableEmpty();
                } catch (RemoteException ex) {
                }
                MonitoringCenterComboBox.setEnabled(! noMonitoringCenters);
            }
        });
    }


    /**
     * Callback for new monitoring center check box: if it is selected, the new monitoring
     * center options (name and address shall be added); remove them otherwise.
     */
    private void isNewCheckBox_at_selection(){
        IsNewCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExistingMonCenterPnl.setVisible(! IsNewCheckBox.isSelected());
                CenterCreationOptionPnl.setVisible(IsNewCheckBox.isSelected());
                NewMonitoredAreasPnl.setVisible(IsNewCheckBox.isSelected());
                isRegistrationModeActive = IsNewCheckBox.isSelected();
            }
        });
    }


    /**
     * Callback for selection of a location on the new monitored areas list (with a
     * double click): remove the area from the list
     */
    private void newMonitoredAreasList_at_selection(){
        NewMonitoredAreasList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    clickedElement = (Location) SearchList.getSelectedValue();
                    newSelectedAreasModel.removeElement(clickedElement);
                }
            }
        });
    }


    /**
     * Callback for Confirm registration button:
     *  - if is a new center, add the operator to the operator table
     *     and add the operator to the monitoring centers table
     *  - if is a new center, add operator and center
     */
    private void confirmRegisterBtn_at_click(){
        ConfirmRegisterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (! areUserDataValid() || ! areCenterDataValid()){
                    return;
                }

                /* update operators table */
                String name = NameTextField.getText() + " " + SurnameTextFIeld.getText();
                String tax_code = TaxCodeTextField.getText();
                String email = EmailTextField.getText();
                String username = UsernameTextField.getText();
                String password = PwdTextField.getText();
                MonitoringCenter selected_mc = (MonitoringCenter) MonitoringCenterComboBox.getSelectedItem();
                String monitoring_center_name = IsNewCheckBox.isSelected()? NewNameTextField.getText() : selected_mc.toString();
                String monitoring_center_id = IsNewCheckBox.isSelected()? "0" : selected_mc.getId();
                MonitoringCenter monitoring_center = new MonitoringCenter(monitoring_center_name, AddressTextField.getText(), monitoring_center_id);
                Operator newOperator = new Operator(name, tax_code, email, username, password, monitoring_center);

                /* update monitoring centers table (only if a new center is added) */
                if (IsNewCheckBox.isSelected()) {
                    ArrayList<String> newMonitoredAreasArrayList = new ArrayList<>();
                    for (int i = 0; i < newSelectedAreasModel.getSize(); i++) {
                        String geonameID = newSelectedAreasModel.getElementAt(i).getGeonameID();

                        newMonitoredAreasArrayList.add(geonameID);
                    }

                    try {
                        monitoring_center_id = utils.getDbService().pushMonitoringCenter(monitoring_center, newMonitoredAreasArrayList);
                        if (monitoring_center_id.equals("-1")) {
                            showMessage(String.format("Something went wrong! Try again.", newOperator.getName()));
                            return;
                        }
                    } catch (RemoteException ex) {
                        showMessage(String.format("Something went wrong! Try again.", newOperator.getName()));
                        resetRegistrationForm();
                        return;
                    }
                }

                monitoring_center = new MonitoringCenter(monitoring_center_name, AddressTextField.getText(), monitoring_center_id);
                newOperator = new Operator(name, tax_code, email, username, password, monitoring_center);

                try {
                    utils.getDbService().pushOperator(newOperator);
                } catch (RemoteException ex) {
                    showMessage(String.format("Something went wrong! Try again.", newOperator.getName()));
                    resetRegistrationForm();
                    return;
                }

                /* exit from the registration panel */
                showMessage(String.format("Welcome, %s! Login to operate.", newOperator.getName()));
                resetRegistrationForm();
            }

            private boolean areUserDataValid(){
                /* check if data are valid */
                if  (
                        NameTextField.getText().isEmpty() &&
                        SurnameTextFIeld.getText().isEmpty() &&
                        TaxCodeTextField.getText().isEmpty() &&
                        EmailTextField.getText().isEmpty() &&
                        UsernameTextField.getText().isEmpty()
                ){
                    showMessage("Fill all the fields!");
                    return false;
                }

                /* check if the username already exists, if a remote exception is thrown,
                * simply return false */
                try {
                    if (utils.getDbService().operatorExists(UsernameTextField.getText()) != null) {
                        showMessage("Username already existing");
                        return false;
                    }
                } catch (RemoteException e) {
                    return false;
                }

                /* and if pwd contains number/special char */
                String password = PwdTextField.getText();
                if(password.length() < 8) {
                    showMessage("Password must be longer than 8 chars!");
                    return false;
                } else {
                    Pattern letter = Pattern.compile("[a-zA-z]");
                    Pattern digit = Pattern.compile("[0-9]");
                    Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

                    Matcher hasLetter = letter.matcher(password);
                    Matcher hasDigit = digit.matcher(password);
                    Matcher hasSpecial = special.matcher(password);

                    if (! (hasLetter.find() && hasDigit.find() && hasSpecial.find())){
                        showMessage("Password must contain numbers and special chars!");
                        return false;
                    }
                    return true;
                }
            }

            private boolean areCenterDataValid(){
                /* data center data are always valid if the center already exists */
                if (! IsNewCheckBox.isSelected() && MonitoringCenterComboBox.isEnabled()){ return true; }

                /* instead, when it's new, check the data */
                if (NewNameTextField.getText().isEmpty() || AddressTextField.getText().isEmpty()){
                    showMessage("Assign a non empty name to the center!");
                    return false;
                } else if (newSelectedAreasModel.isEmpty()) {
                    showMessage("Choose at least one monitored area!");
                    return false;
                }
                return true;
            }

        });
    }



    /**
     * Callback for Exit button push in the registration panel. Come back to the main page
     *
     */
    private void exitFromRegistrationBtn_at_click(){
        ExitFromRegistrationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetRegistrationForm();
            }
        });
    }


    /**
     * Callback for the monitoring center name combo box.
     * Filter on the list of existing monitoring centers: if the inserted
     * monitoring center already exists, the confirm registration button
     * shall be enabled, otherwise it shall enable the address text field.
     */
    private void monitoringCenterComboBox_at_selection(){

    }


    /**
     * This is the callback for a selection of the username text edit field.
     * It removes the "Username" text.
     */
    private void userLoginTextField_at_selection(){
        userLoginTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                utils.textFieldEnter(userLoginTextField, Constants.USERNAME_S);
            }

            @Override
            public void focusLost(FocusEvent e) {
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
                String username = userLoginTextField.getText();
                char[] pwd = pwdLoginTextField.getPassword();

                Operator operator;
                /* if the user exists and nobody else is logged in, operators features are shown */
                try {
                    operator = utils.getDbService().operatorExists(username);
                } catch (RemoteException ex) {
                    operator = null;
                }

                if (operator != null && utils.giveAccessTo(operator)) {
                    WelcomeBackLbl.setText("Welcome back, " + operator);
                    LoginPnl.setVisible(false);
                    ButtonsPnl.setVisible(true);
                    setLoggedInMode(true);
                } else {
                    userLoginTextField.setBorder(new LineBorder(Color.RED, 3));
                    pwdLoginTextField.setBorder(new LineBorder(Color.RED, 3));
                }
            }
        });
    }


    /**
     * Callback for exit button in the login form
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


    /**
     * Callback for logout button for logged in users
     */
    private void logoutBtn_at_click() {
        LogoutBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /* reset all the info for logged in users */
                utils.logoutUser();

                /* gui elements */
                setLoggedInMode(false);
                ButtonsPnl.setVisible(true);
                /* restore initial configuration for username and pwd */
                userLoginTextField.setText(Constants.USERNAME_S);
                userLoginTextField.setForeground(new Color(187,187,187));
                pwdLoginTextField.setText(Constants.PWD_S);
                pwdLoginTextField.setForeground(new Color(187,187,187));
            }
        });
    }

    /**
     * Callback for add new area button, opens a new dialog where you can set the info
     *
     */
    private void addNewBtn_at_click(){
        AddNewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewArea();
            }
        });
    }
}
