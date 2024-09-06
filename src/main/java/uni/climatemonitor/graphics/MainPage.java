/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import uni.climatemonitor.generics.Constants;
import uni.climatemonitor.common.Location;
import uni.climatemonitor.common.MonitoringCenter;
import uni.climatemonitor.common.Coordinates;
import uni.climatemonitor.common.Operator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
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
    private JList AvailableLocationsList;
    private JScrollPane AvailableListPnl;
    private JPanel AvailableLocationsPnl;
    private JLabel AvailableLocationsLabel;

    /* utilities for locations */
    private DefaultListModel<Location> searchListModel = new DefaultListModel<>();
    private Location clickedElement;
    private DefaultListModel<Location> operatorLocationsListModel = new DefaultListModel<>();


    /* utilities for registration */
    ArrayList<MonitoringCenter> mcs;
    private DefaultComboBoxModel<MonitoringCenter> monitoringCenterComboBoxModel = new DefaultComboBoxModel<>();
    private DefaultListModel<Location> newSelectedAreasModel = new DefaultListModel<>();
    private boolean isRegistrationModeActive = false;
    private Border userLoginTextFieldBorder;
    private Border pwdLoginTextFieldBorder;

    /* utils */
    UtilsSingleton utils;

    public MainPage() {
        utils = UtilsSingleton.getInstance();

        userLoginTextFieldBorder = userLoginTextField.getBorder();
        pwdLoginTextFieldBorder = pwdLoginTextField.getBorder();

        /* set the logo */
        URL imgURL = getClass().getResource(Constants.LOGO_PATH_S);
        ImageIcon image = new ImageIcon(imgURL);
        Logo.setIcon(image);

        /* set initial search list and its gui options */
        SearchList.setModel(searchListModel);
        SearchList.setVisibleRowCount(16);
        SearchList.setBackground(new Color(238, 238, 238));
        SearchListPnl.setVisible(false);

        /* set lists backgrounds and lengths */
        AvailableLocationsList.setBackground(new Color(238, 238, 238));
        AvailableLocationsList.setVisibleRowCount(16);
        NewMonitoredAreasList.setBackground(new Color(238, 238, 238));

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
        /* available list at selection */
        availableLocations_at_selection();
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
     *
     *        UTILS
     *
     *************************************************************/

    /**
     * update available locations
     */
    public void updateAvailableLocations(Location newLocation) {
        operatorLocationsListModel.addElement(newLocation);
    }

    /**
     * Show the hidden message on the low-right corner
     *
     * @param message
     */
    private void showMessage(String message) {
        WelcomeBackLbl.setText(message);
        UserMessagesPnl.setVisible(true);
    }

    /**
     * set/reset the log mode (logged in/logged out, accordingly to the parameter)
     *
     * @param isLoggedInModeActive
     */
    private void setLoggedInMode(boolean isLoggedInModeActive) {
        LoginBtnsPnl.setVisible(!isLoggedInModeActive);
        LogoutBtnPnl.setVisible(isLoggedInModeActive);
        UserMessagesPnl.setVisible(isLoggedInModeActive);
    }

    /**
     * Reset the registration form when it is closed
     */
    private void resetRegistrationForm() {
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
     * - remove the visibility of the suggestion list
     * - remove the login popup
     */
    private void MainPnl_at_visibility_change() {
        MainPnl.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                JComponent component = (JComponent) e.getSource();
                if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                        && component.isShowing()) {
                    SearchListPnl.setVisible(false);
                    LoginPnl.setVisible(false);
                    ButtonsPnl.setVisible(true);
                }
            }
        });
    }

    ;


    /**
     * Callback for About button push. Open the dialog with some info
     * about the authors and the project.
     */
    private void aboutBtn_at_click() {
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
     */
    private void placeTextField_at_selection() {
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
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (typeAPlaceTextField.getText().length() > 1) {
                suggest();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            suggest();
        }

        public void suggest() {
            String searched = typeAPlaceTextField.getText();
            /* filter places:
                if the searched string does not contain numbers,
                it is a simple search, but if contains numbers (or +/-)
                it is a lat-long search
             */
            final Pattern pattern = Pattern.compile("(\\d+\\.\\d+) (N|S)° (\\d+\\.\\d+)° (E|W)");
            Matcher matcher = pattern.matcher(searched);
            if (!matcher.matches()) {
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
            if (!searchListModel.isEmpty() && searched.length() > 0) {
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
            for (Location l : filteredLocations) {
                searchListModel.addElement(l);
            }
        }
    };


    /**
     * This is the callback for a change in the location text edit field
     */
    private void placeTextField_at_text_change() {

    }


    /**
     * Callback for selection of a location on the location list (with a
     * double click)
     */
    private void searchList_at_selection() {
        SearchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    clickedElement = (Location) SearchList.getSelectedValue();
                    if (clickedElement == null) {
                        return;
                    }

                    /* if registration mode is active, add the clicked element to the list of selected areas,
                     * else open the details page */
                    if (!isRegistrationModeActive) {
                        utils.getDetailsPnl().setUIPnl(clickedElement);
                        utils.switchPage("Location Details Page");
                    } else {
                        if (!newSelectedAreasModel.contains(clickedElement)) {
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
    private void loginBtn_at_click() {
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
    private void registerBtn_at_click() {
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
                for (MonitoringCenter mc : mcs) {
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
                MonitoringCenterComboBox.setEnabled(!noMonitoringCenters);
            }
        });
    }


    /**
     * Callback for new monitoring center check box: if it is selected, the new monitoring
     * center options (name and address shall be added); remove them otherwise.
     */
    private void isNewCheckBox_at_selection() {
        IsNewCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExistingMonCenterPnl.setVisible(!IsNewCheckBox.isSelected());
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
    private void newMonitoredAreasList_at_selection() {
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
     * - if is a new center, add the operator to the operator table
     * and add the operator to the monitoring centers table
     * - if is a new center, add operator and center
     */
    private void confirmRegisterBtn_at_click() {
        ConfirmRegisterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!areUserDataValid() || !areCenterDataValid()) {
                    return;
                }

                /* update operators table */
                String name = NameTextField.getText() + " " + SurnameTextFIeld.getText();
                String tax_code = TaxCodeTextField.getText();
                String email = EmailTextField.getText();
                String username = UsernameTextField.getText();
                String password = PwdTextField.getText();
                MonitoringCenter selected_mc = (MonitoringCenter) MonitoringCenterComboBox.getSelectedItem();
                String monitoring_center_name = IsNewCheckBox.isSelected() ? NewNameTextField.getText() : selected_mc.toString();
                String monitoring_center_id = IsNewCheckBox.isSelected() ? "0" : selected_mc.getId();
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

            private boolean areUserDataValid() {
                /* check if data are valid */
                if (
                        NameTextField.getText().isEmpty() &&
                                SurnameTextFIeld.getText().isEmpty() &&
                                TaxCodeTextField.getText().isEmpty() &&
                                EmailTextField.getText().isEmpty() &&
                                UsernameTextField.getText().isEmpty()
                ) {
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
                if (password.length() < 8) {
                    showMessage("Password must be longer than 8 chars!");
                    return false;
                } else {
                    Pattern letter = Pattern.compile("[a-zA-z]");
                    Pattern digit = Pattern.compile("[0-9]");
                    Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

                    Matcher hasLetter = letter.matcher(password);
                    Matcher hasDigit = digit.matcher(password);
                    Matcher hasSpecial = special.matcher(password);

                    if (!(hasLetter.find() && hasDigit.find() && hasSpecial.find())) {
                        showMessage("Password must contain numbers and special chars!");
                        return false;
                    }
                    return true;
                }
            }

            private boolean areCenterDataValid() {
                /* data center data are always valid if the center already exists */
                if (!IsNewCheckBox.isSelected() && MonitoringCenterComboBox.isEnabled()) {
                    return true;
                }

                /* instead, when it's new, check the data */
                if (NewNameTextField.getText().isEmpty() || AddressTextField.getText().isEmpty()) {
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
     */
    private void exitFromRegistrationBtn_at_click() {
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
    private void monitoringCenterComboBox_at_selection() {

    }


    /**
     * This is the callback for a selection of the username text edit field.
     * It removes the "Username" text.
     */
    private void userLoginTextField_at_selection() {
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
    private void pwdLoginTextField_at_selection() {
        pwdLoginTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                pwdLoginTextField.setText("");
                pwdLoginTextField.setForeground(new Color(0, 0, 0));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Arrays.equals(pwdLoginTextField.getPassword(), Constants.EMPTY_S.toCharArray())) {
                    pwdLoginTextField.setText(Constants.PWD_S);
                    pwdLoginTextField.setForeground(new Color(187, 187, 187));
                }
            }
        });
    }


    /**
     * This is the callback of a push event on the Search button
     */
    private void loginEnterBtn_at_click() {
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
                String pwd = new String(pwdLoginTextField.getPassword());

                Operator operator;
                /* if the user exists and nobody else is logged in, operators features are shown */
                try {
                    operator = utils.getDbService().operatorExists(username);
                } catch (RemoteException ex) {
                    operator = null;
                }

                if (operator != null && !pwd.equals(operator.getPassword())) {
                    userLoginTextField.setBorder(new LineBorder(Color.RED, 3));
                    pwdLoginTextField.setBorder(new LineBorder(Color.RED, 3));
                } else if (operator != null && utils.giveAccessTo(operator)) {
                    // add list of available places
                    try {
                        ArrayList<Location> availableLocations = utils.getDbService().getLocationsFromMonitoringCenter(operator.getMonitoringCenter().getId());
                        operatorLocationsListModel.clear();
                        for (Location availableLocation : availableLocations) {
                            operatorLocationsListModel.addElement(availableLocation);
                        }
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(MainPnl, "Something wrong happened, try again!", "",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // create the scrollable list
                    AvailableLocationsPnl.setVisible(true);
                    AvailableLocationsList.setModel(operatorLocationsListModel);

                    WelcomeBackLbl.setText("Welcome back, " + operator);
                    LoginPnl.setVisible(false);
                    ButtonsPnl.setVisible(true);
                    setLoggedInMode(true);
                }
            }
        });
    }

    /**
     * callback to available places list selection
     */
    private void availableLocations_at_selection() {
        AvailableLocationsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    clickedElement = (Location) AvailableLocationsList.getSelectedValue();
                    if (clickedElement == null) {
                        return;
                    }
                    utils.getDetailsPnl().setUIPnl(clickedElement);
                    utils.switchPage("Location Details Page");
                }
            }
        });
    }


    /**
     * Callback for exit button in the login form
     */
    private void loginExitBtn_at_click() {
        loginExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPnl.setVisible(true);
                LoginPnl.setVisible(false);
                ButtonsPnl.setVisible(true);

                /* restore initial configuration for username and pwd */
                userLoginTextField.setText(Constants.USERNAME_S);
                userLoginTextField.setForeground(new Color(187, 187, 187));
                pwdLoginTextField.setText(Constants.PWD_S);
                pwdLoginTextField.setForeground(new Color(187, 187, 187));
            }
        });
    }


    /**
     * Callback for logout button for logged in users
     */
    private void logoutBtn_at_click() {
        LogoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* reset all the info for logged in users */
                utils.logoutUser();

                /* gui elements */
                AvailableLocationsPnl.setVisible(false);
                setLoggedInMode(false);
                ButtonsPnl.setVisible(true);
                /* restore initial configuration for username and pwd */
                userLoginTextField.setText(Constants.USERNAME_S);
                userLoginTextField.setForeground(new Color(187, 187, 187));
                pwdLoginTextField.setText(Constants.PWD_S);
                pwdLoginTextField.setForeground(new Color(187, 187, 187));
            }
        });
    }

    /**
     * Callback for add new area button, opens a new dialog where you can set the info
     */
    private void addNewBtn_at_click() {
        AddNewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewArea();
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        ParentPnl = new JPanel();
        ParentPnl.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ParentPnl.setPreferredSize(new Dimension(1200, 600));
        MainPnl = new JPanel();
        MainPnl.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        MainPnl.setForeground(new Color(-12960964));
        ParentPnl.add(MainPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        MainPnl.add(spacer1, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(200, -1), null, 0, false));
        UserPnl = new JPanel();
        UserPnl.setLayout(new BorderLayout(0, 0));
        UserPnl.setVisible(true);
        MainPnl.add(UserPnl, new GridConstraints(0, 2, 3, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LoginPnl = new JPanel();
        LoginPnl.setLayout(new GridLayoutManager(4, 1, new Insets(10, 0, 0, 10), -1, -1));
        LoginPnl.setVisible(false);
        UserPnl.add(LoginPnl, BorderLayout.SOUTH);
        userLoginTextField = new JTextField();
        userLoginTextField.setForeground(new Color(-2960686));
        userLoginTextField.setMargin(new Insets(2, 6, 2, 6));
        userLoginTextField.setSelectedTextColor(new Color(-5460810));
        userLoginTextField.setText("Username");
        userLoginTextField.setVisible(true);
        LoginPnl.add(userLoginTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pwdLoginTextField = new JPasswordField();
        pwdLoginTextField.setForeground(new Color(-2960686));
        pwdLoginTextField.setMargin(new Insets(2, 5, 2, 2));
        pwdLoginTextField.setSelectedTextColor(new Color(-5460810));
        pwdLoginTextField.setText("password");
        pwdLoginTextField.setVisible(true);
        LoginPnl.add(pwdLoginTextField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        LoginPnl.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loginEnterBtn = new JButton();
        loginEnterBtn.setActionCommand("LoginEnter");
        loginEnterBtn.setContentAreaFilled(false);
        loginEnterBtn.setInheritsPopupMenu(false);
        loginEnterBtn.setOpaque(false);
        loginEnterBtn.setText("Login");
        panel1.add(loginEnterBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginExitButton = new JButton();
        loginExitButton.setContentAreaFilled(false);
        loginExitButton.setText("Exit");
        panel1.add(loginExitButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UserGeneralPnl = new JPanel();
        UserGeneralPnl.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 10), -1, -1));
        UserGeneralPnl.setVisible(true);
        UserPnl.add(UserGeneralPnl, BorderLayout.CENTER);
        ButtonsPnl = new JPanel();
        ButtonsPnl.setLayout(new GridLayoutManager(2, 2, new Insets(0, 20, 0, 0), -1, -1));
        UserGeneralPnl.add(ButtonsPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LoginBtnsPnl = new JPanel();
        LoginBtnsPnl.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        ButtonsPnl.add(LoginBtnsPnl, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LoginBtn = new JButton();
        LoginBtn.setBorderPainted(false);
        LoginBtn.setContentAreaFilled(false);
        LoginBtn.setHideActionText(false);
        LoginBtn.setHorizontalAlignment(4);
        LoginBtn.setHorizontalTextPosition(11);
        LoginBtn.setIconTextGap(4);
        LoginBtn.setInheritsPopupMenu(false);
        LoginBtn.setLabel("Login");
        LoginBtn.setOpaque(false);
        LoginBtn.setRequestFocusEnabled(true);
        LoginBtn.setRolloverEnabled(true);
        LoginBtn.setText("Login");
        LoginBtn.putClientProperty("hideActionText", Boolean.FALSE);
        LoginBtnsPnl.add(LoginBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RegisterBtn = new JButton();
        RegisterBtn.setActionCommand("Register");
        RegisterBtn.setBorderPainted(false);
        RegisterBtn.setContentAreaFilled(false);
        RegisterBtn.setHorizontalAlignment(4);
        RegisterBtn.setLabel("Register");
        RegisterBtn.setText("Register");
        LoginBtnsPnl.add(RegisterBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LogoutBtnPnl = new JPanel();
        LogoutBtnPnl.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        LogoutBtnPnl.setVisible(true);
        ButtonsPnl.add(LogoutBtnPnl, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LogoutBtn = new JButton();
        LogoutBtn.setBorderPainted(false);
        LogoutBtn.setContentAreaFilled(false);
        LogoutBtn.setHorizontalAlignment(4);
        LogoutBtn.setText("Logout");
        LogoutBtnPnl.add(LogoutBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AddNewBtn = new JButton();
        AddNewBtn.setBorderPainted(false);
        AddNewBtn.setContentAreaFilled(false);
        AddNewBtn.setHorizontalAlignment(4);
        AddNewBtn.setHorizontalTextPosition(4);
        AddNewBtn.setText("Add Area");
        LogoutBtnPnl.add(AddNewBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RegistrationPnl = new JPanel();
        RegistrationPnl.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        UserGeneralPnl.add(RegistrationPnl, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        UserInfoPnl = new JPanel();
        UserInfoPnl.setLayout(new GridLayoutManager(6, 2, new Insets(2, 0, 2, 1), -1, -1));
        UserInfoPnl.setVisible(true);
        RegistrationPnl.add(UserInfoPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        UserInfoPnl.setBorder(BorderFactory.createTitledBorder(null, "User Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        NameLbl = new JLabel();
        Font NameLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, NameLbl.getFont());
        if (NameLblFont != null) NameLbl.setFont(NameLblFont);
        NameLbl.setText("Name");
        UserInfoPnl.add(NameLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NameTextField = new JTextField();
        NameTextField.setCaretColor(new Color(-16777216));
        NameTextField.setForeground(new Color(-16777216));
        NameTextField.setText("");
        UserInfoPnl.add(NameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        SurnameTextFIeld = new JTextField();
        SurnameTextFIeld.setCaretColor(new Color(-16777216));
        SurnameTextFIeld.setForeground(new Color(-16777216));
        SurnameTextFIeld.setText("");
        UserInfoPnl.add(SurnameTextFIeld, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        SurnameLbl = new JLabel();
        Font SurnameLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, SurnameLbl.getFont());
        if (SurnameLblFont != null) SurnameLbl.setFont(SurnameLblFont);
        SurnameLbl.setText("Surname");
        UserInfoPnl.add(SurnameLbl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        TaxCodeLbl = new JLabel();
        Font TaxCodeLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, TaxCodeLbl.getFont());
        if (TaxCodeLblFont != null) TaxCodeLbl.setFont(TaxCodeLblFont);
        TaxCodeLbl.setText("Tax Code");
        UserInfoPnl.add(TaxCodeLbl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        TaxCodeTextField = new JTextField();
        TaxCodeTextField.setCaretColor(new Color(-16777216));
        TaxCodeTextField.setForeground(new Color(-16777216));
        TaxCodeTextField.setText("");
        UserInfoPnl.add(TaxCodeTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        EmailLbl = new JLabel();
        Font EmailLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, EmailLbl.getFont());
        if (EmailLblFont != null) EmailLbl.setFont(EmailLblFont);
        EmailLbl.setText("Email");
        UserInfoPnl.add(EmailLbl, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        EmailTextField = new JTextField();
        EmailTextField.setCaretColor(new Color(-16777216));
        EmailTextField.setForeground(new Color(-16777216));
        EmailTextField.setText("");
        UserInfoPnl.add(EmailTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        UsernameLbl = new JLabel();
        Font UsernameLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, UsernameLbl.getFont());
        if (UsernameLblFont != null) UsernameLbl.setFont(UsernameLblFont);
        UsernameLbl.setText("Username");
        UserInfoPnl.add(UsernameLbl, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UsernameTextField = new JTextField();
        UsernameTextField.setCaretColor(new Color(-16777216));
        UsernameTextField.setForeground(new Color(-16777216));
        UsernameTextField.setText("");
        UserInfoPnl.add(UsernameTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        PwdLbl = new JLabel();
        Font PwdLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, PwdLbl.getFont());
        if (PwdLblFont != null) PwdLbl.setFont(PwdLblFont);
        PwdLbl.setText("Password");
        UserInfoPnl.add(PwdLbl, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        PwdTextField = new JTextField();
        PwdTextField.setCaretColor(new Color(-16777216));
        PwdTextField.setForeground(new Color(-16777216));
        UserInfoPnl.add(PwdTextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        MonitoringCenterPnl = new JPanel();
        MonitoringCenterPnl.setLayout(new GridLayoutManager(5, 2, new Insets(2, 0, 2, 0), -1, -1));
        MonitoringCenterPnl.setVisible(true);
        RegistrationPnl.add(MonitoringCenterPnl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MonitoringCenterPnl.setBorder(BorderFactory.createTitledBorder(null, "Monitoring Center", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        IsNewPnl = new JPanel();
        IsNewPnl.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        MonitoringCenterPnl.add(IsNewPnl, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        NewMonCenterLbl = new JLabel();
        Font NewMonCenterLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, NewMonCenterLbl.getFont());
        if (NewMonCenterLblFont != null) NewMonCenterLbl.setFont(NewMonCenterLblFont);
        NewMonCenterLbl.setText("New monitoring center?");
        IsNewPnl.add(NewMonCenterLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        IsNewCheckBox = new JCheckBox();
        IsNewCheckBox.setText("");
        IsNewPnl.add(IsNewCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CenterCreationOptionPnl = new JPanel();
        CenterCreationOptionPnl.setLayout(new GridLayoutManager(2, 2, new Insets(1, 0, 0, 0), -1, -1));
        MonitoringCenterPnl.add(CenterCreationOptionPnl, new GridConstraints(2, 0, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        AddressLbl = new JLabel();
        Font AddressLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, AddressLbl.getFont());
        if (AddressLblFont != null) AddressLbl.setFont(AddressLblFont);
        AddressLbl.setText("Address");
        CenterCreationOptionPnl.add(AddressLbl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AddressTextField = new JTextField();
        AddressTextField.setCaretColor(new Color(-16777216));
        AddressTextField.setForeground(new Color(-16777216));
        AddressTextField.setText("");
        AddressTextField.setVisible(true);
        CenterCreationOptionPnl.add(AddressTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        NewNameLbl = new JLabel();
        Font NewNameLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, NewNameLbl.getFont());
        if (NewNameLblFont != null) NewNameLbl.setFont(NewNameLblFont);
        NewNameLbl.setText("New Name");
        CenterCreationOptionPnl.add(NewNameLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NewNameTextField = new JTextField();
        NewNameTextField.setCaretColor(new Color(-16777216));
        NewNameTextField.setForeground(new Color(-16777216));
        CenterCreationOptionPnl.add(NewNameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ExistingMonCenterPnl = new JPanel();
        ExistingMonCenterPnl.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        MonitoringCenterPnl.add(ExistingMonCenterPnl, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MonitoringCenterNameLbl = new JLabel();
        Font MonitoringCenterNameLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, MonitoringCenterNameLbl.getFont());
        if (MonitoringCenterNameLblFont != null) MonitoringCenterNameLbl.setFont(MonitoringCenterNameLblFont);
        MonitoringCenterNameLbl.setText("Name");
        ExistingMonCenterPnl.add(MonitoringCenterNameLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MonitoringCenterComboBox = new JComboBox();
        MonitoringCenterComboBox.setEditable(false);
        ExistingMonCenterPnl.add(MonitoringCenterComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NewMonitoredAreasPnl = new JPanel();
        NewMonitoredAreasPnl.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        MonitoringCenterPnl.add(NewMonitoredAreasPnl, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        NewMonitoredAreasPnl.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        NewMonitoredAreasList = new JList();
        NewMonitoredAreasList.setValueIsAdjusting(true);
        NewMonitoredAreasList.setVisibleRowCount(4);
        scrollPane1.setViewportView(NewMonitoredAreasList);
        SelectedAreasLbl = new JLabel();
        Font SelectedAreasLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, SelectedAreasLbl.getFont());
        if (SelectedAreasLblFont != null) SelectedAreasLbl.setFont(SelectedAreasLblFont);
        SelectedAreasLbl.setText("Selected areas:");
        NewMonitoredAreasPnl.add(SelectedAreasLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RegistrationPnlButtonsPnl = new JPanel();
        RegistrationPnlButtonsPnl.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        RegistrationPnl.add(RegistrationPnlButtonsPnl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ConfirmRegisterBtn = new JButton();
        ConfirmRegisterBtn.setBorderPainted(true);
        ConfirmRegisterBtn.setContentAreaFilled(false);
        ConfirmRegisterBtn.setText("Register");
        RegistrationPnlButtonsPnl.add(ConfirmRegisterBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ExitFromRegistrationBtn = new JButton();
        ExitFromRegistrationBtn.setBorderPainted(true);
        ExitFromRegistrationBtn.setContentAreaFilled(false);
        ExitFromRegistrationBtn.setText("Exit");
        RegistrationPnlButtonsPnl.add(ExitFromRegistrationBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AvailableLocationsPnl = new JPanel();
        AvailableLocationsPnl.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        AvailableLocationsPnl.setVisible(false);
        UserGeneralPnl.add(AvailableLocationsPnl, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        AvailableListPnl = new JScrollPane();
        AvailableListPnl.setVisible(true);
        AvailableLocationsPnl.add(AvailableListPnl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        AvailableLocationsList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        AvailableLocationsList.setModel(defaultListModel1);
        AvailableListPnl.setViewportView(AvailableLocationsList);
        AvailableLocationsLabel = new JLabel();
        AvailableLocationsLabel.setFocusable(true);
        Font AvailableLocationsLabelFont = this.$$$getFont$$$(null, Font.ITALIC, -1, AvailableLocationsLabel.getFont());
        if (AvailableLocationsLabelFont != null) AvailableLocationsLabel.setFont(AvailableLocationsLabelFont);
        AvailableLocationsLabel.setText("Available locations:");
        AvailableLocationsLabel.setVisible(true);
        AvailableLocationsPnl.add(AvailableLocationsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AboutPnl = new JPanel();
        AboutPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        MainPnl.add(AboutPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(88, 185), null, 0, false));
        aboutButton = new JButton();
        aboutButton.setBorderPainted(false);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setHorizontalAlignment(2);
        aboutButton.setHorizontalTextPosition(0);
        aboutButton.setText("About");
        AboutPnl.add(aboutButton);
        SearchPnl = new JPanel();
        SearchPnl.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        MainPnl.add(SearchPnl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(592, 185), null, 0, false));
        typeAPlaceTextField = new JTextField();
        typeAPlaceTextField.setAutoscrolls(true);
        typeAPlaceTextField.setForeground(new Color(-2960686));
        typeAPlaceTextField.setOpaque(true);
        typeAPlaceTextField.setSelectedTextColor(new Color(-5460810));
        typeAPlaceTextField.setText("Type a place...");
        SearchPnl.add(typeAPlaceTextField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(480, 30), null, 0, false));
        final Spacer spacer2 = new Spacer();
        SearchPnl.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 70), null, 0, false));
        Logo = new JLabel();
        Logo.setBackground(new Color(-4473925));
        Logo.setText("");
        SearchPnl.add(Logo, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SearchListPnl = new JPanel();
        SearchListPnl.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        SearchPnl.add(SearchListPnl, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SearchListScrollPnl = new JScrollPane();
        SearchListScrollPnl.setOpaque(false);
        SearchListScrollPnl.setVerticalScrollBarPolicy(20);
        SearchListPnl.add(SearchListScrollPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        SearchList = new JList();
        SearchList.setFocusCycleRoot(true);
        Font SearchListFont = this.$$$getFont$$$(null, Font.ITALIC, -1, SearchList.getFont());
        if (SearchListFont != null) SearchList.setFont(SearchListFont);
        SearchList.setLayoutOrientation(0);
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        SearchList.setModel(defaultListModel2);
        SearchList.setOpaque(true);
        SearchList.setSelectionMode(0);
        SearchList.setVisible(true);
        SearchListScrollPnl.setViewportView(SearchList);
        final Spacer spacer3 = new Spacer();
        MainPnl.add(spacer3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 100), null, 0, false));
        UserMessagesPnl = new JPanel();
        UserMessagesPnl.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 15), -1, -1));
        UserMessagesPnl.setVisible(false);
        MainPnl.add(UserMessagesPnl, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        WelcomeBackLbl = new JLabel();
        Font WelcomeBackLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, WelcomeBackLbl.getFont());
        if (WelcomeBackLblFont != null) WelcomeBackLbl.setFont(WelcomeBackLblFont);
        WelcomeBackLbl.setText("Welcome back nome");
        UserMessagesPnl.add(WelcomeBackLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        MainPnl.add(spacer4, new GridConstraints(2, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(200, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return ParentPnl;
    }

}
