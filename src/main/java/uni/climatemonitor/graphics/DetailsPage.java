/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import uni.climatemonitor.common.ClimateParameter;
import uni.climatemonitor.common.Location;
import uni.climatemonitor.common.MonitoringCenter;
import uni.climatemonitor.common.Operator;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Details page graphics class
 */
public class DetailsPage {
    private JLabel PlaceNameLbl;
    private JButton CloseBtn;
    private JPanel ParentPnl;
    private JPanel LocationDetailPnl;
    private JPanel ClosePnl;
    private JPanel PlaceNamePnl;
    private JLabel WindMostRecentValueLbl;
    private JPanel ParamsPnl;
    private JLabel WindAverageValueLbl;
    private JLabel HumidityMostRecentValueLbl;
    private JLabel HumidityAverageValueLbl;
    private JLabel PressureMostRecentValueLbl;
    private JLabel PressureAverageValueLbl;
    private JLabel AverageTitleLbl;
    private JLabel MostRecentTitleLbl;
    private JLabel TemperatureMostRecentValueLbl;
    private JLabel TemperatureAverageValueLbl;
    private JLabel RainfallMostRecentValueLbl;
    private JLabel RainfallAverageValueLbl;
    private JLabel GAltMostRecentValueLbl;
    private JLabel GAltAverageValueLbl;
    private JLabel GMassMostRecentValueLbl;
    private JLabel GMassAverageValueLbl;
    private JPanel WindAveragePnl;
    private JPanel WindMostRecentPnl;
    private JComboBox WindComboBox;
    private JPanel HumidityMostRecentPnl;
    private JPanel HumidityAveragePnl;
    private JComboBox HumidityComboBox;
    private JPanel PressureMostRecentPnl;
    private JPanel PressureAveragePnl;
    private JComboBox PressureComboBox;
    private JPanel TemperatureMostRecentPnl;
    private JPanel TemperatureAveragePnl;
    private JComboBox TemperatureComboBox;
    private JPanel RainfallMostRecentPnl;
    private JPanel RainfallAveragePnl;
    private JComboBox RainfallComboBox;
    private JPanel GAltMostRecentPnl;
    private JPanel GAltAveragePnl;
    private JComboBox GAltComboBox;
    private JPanel GMassMostRecentPnl;
    private JComboBox GMassComboBox;
    private JTextArea NotesTextArea;
    private JPanel NotesPnl;
    private JButton SaveBtn;
    private JPanel ButtonsPnl;
    private JComboBox DateComboBox;
    private JPanel ChooseDatePnl;
    private JPanel NotesErrorPnl;
    private JLabel WindLbl;
    private JLabel HumidityLbl;
    private JLabel PressureLbl;
    private JLabel TemperatureLbl;
    private JLabel RainfallLbl;
    private JLabel GAltLbl;
    private JLabel GMassLbl;
    private JPanel GMassAveragePnl;
    private JPanel DetailsPnl;
    private JLabel MaxNumOfCharErrLbl;
    private JLabel NotesLbl;
    private JPanel AboutLastRecordPnl;
    private JTextArea AboutLastRecordTextArea;
    private JButton AddBtn;

    /* location info */
    private Location location;
    private ArrayList<ClimateParameter> params;
    // ocmbo box for dates
    DefaultComboBoxModel<String> comboBoxModel;


    // singleton
    private UtilsSingleton utils;

    /* operator info */
    boolean isOperatorEnabled = false;

    /* weather criticality levels */
    HashMap<String, String> criticality = new HashMap<>();

    /* info about last record (to be formatted) */
    private String aboutLastRecord =
            """
This detection has been recorded on %s, from Monitoring Center "%s".

%s is a monitoring center based in %s.
""";


    public DetailsPage(){
        /* set criticality levels */
        criticality.put("5", "CRITICAL");
        criticality.put("4", "SEVERE");
        criticality.put("3", "MODERATE");
        criticality.put("2", "FAVORABLE");
        criticality.put("1", "EXCELLENT");

        /* some graphics settings */
        NotesTextArea.setSize(new Dimension(150, 50));
        NotesTextArea.setMaximumSize(new Dimension(150, 50));
        NotesTextArea.setMinimumSize(new Dimension(150, 50));
        NotesTextArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        NotesPnl.setSize(new Dimension(150,50));
        NotesPnl.setMaximumSize(new Dimension(150,50));
        NotesPnl.setMinimumSize(new Dimension(150,50));

        comboBoxModel = new DefaultComboBoxModel<>();

        /*
            Callbacks for the detailed location page
         */
        /* add button */
        addBtn_at_selection();
        /* close the detailed location page */
        closeBtn_at_selection();
        /* reset action when the page is closed */
        DetailsPnl_at_visibility_change();
        /* at save button click */
        saveBtn_at_selection();
        /* at date selection change */
        dateComboBox_at_item_change();
        /* notes text area max number of characters check */
        notesTextArea_at_change();

        utils = UtilsSingleton.getInstance();

    }
    /********************************************************
     * PUBLIC METHODS
     ********************************************************/
    public void updateDate(ClimateParameter cp) {
        params.add(0, cp);
        setUIGenericUser(false);
    }

    /*********************************************************
     * UTILS
     */

    /**
     * Compute the average on a given list of measures and return it as
     * a string
     * @param measures
     * @return String
     */
    private String computeAverage(ArrayList<Integer> measures){
        float average = 0f;
        int numOfMeasures = measures.size();
        for (Integer measure : measures){
            average += (float) measure;
        }
        average = average / ((float) numOfMeasures);

        String averageStr = String.format(Locale.US, "%.2f", average);
        return averageStr;
    }

    /**
     * Set the about field for the idx-th record
     * @param idx
     */
    private void setAboutLastRecordTextArea(int idx){
        AboutLastRecordTextArea.setBackground(new Color(238,238,238));
        if (params.size() != 0) {
            String who =  params.get(idx).getWho();
            MonitoringCenter monCenter = null;
            try {
                monCenter = utils.getDbService().getMonitoringCenterForOperator(who);
            } catch (RemoteException e) {
                // don't know what to do
                throw new RuntimeException(e);
            }
            LocalDate date = params.get(idx).getDate();
            String monCenterInfo = monCenter.getAddress();
            String info = String.format(aboutLastRecord, date, monCenter, monCenter, monCenterInfo);
            AboutLastRecordTextArea.setText(info);
        } else {
            AboutLastRecordTextArea.setText("No record found.");
        }
    }

    /**
     * Set up the full UI for the given location
     * @param loc
     */
    public void setUIPnl(Location loc){
        location = loc;

        try {
            params = utils.getDbService().getClimateParameterHistory(location);
        } catch (RemoteException e) {
            // throw, I don't know what to do here at the moment
            throw new RuntimeException(e);
        }

        Operator operator = utils.getWhoisLoggedIn();
        // if we are not in operator mode, register the client in the server for this location
        if (operator == null) {
            try {
                utils.getDbService().registerClientForLocation(utils.getClient(), location);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            isOperatorEnabled = utils.getDbService().isOperatorEnabledForLocation(utils.getWhoisLoggedIn().getUsername(), location);
        } catch (Exception e) {
            isOperatorEnabled = false;
        }
        NotesErrorPnl.setVisible(false);

        /* if an operator is logged in, set the combo box for detections and remove current values */
        NotesTextArea.setBackground(new Color(238, 238, 238));
        if (isOperatorEnabled) {
            setOperatorsView();
            MostRecentTitleLbl.setText("Set new record");

            /* set notes area settings */
            NotesTextArea.setBackground(new Color(255, 255, 255));
        } else if (operator != null) {
            AddBtn.setVisible(true);
        }

        setUIGenericUser(isOperatorEnabled);
    }

    private void setUIGenericUser(boolean isOperatorEnabled) {
        /* set info about the last detection and the monitoring center */
        AboutLastRecordPnl.setVisible(!isOperatorEnabled);
        if (!isOperatorEnabled) {
            setAboutLastRecordTextArea(0);
        }

        PlaceNameLbl.setText(location.toStringNoCoordinates());

        DateComboBox.setPreferredSize(new Dimension(185, 24));
        if (!isOperatorEnabled && params.size() != 0) {
            ArrayList<String> dates = new ArrayList<>();
            for (ClimateParameter cp : params) {
                dates.add(cp.getDate().toString());
            }
            // before changing DateComboBox, disable the listener, the re-enable it
            DateComboBox.removeActionListener(dateComboBoxItemChangeListener);
            comboBoxModel.removeAllElements();
            for (String s : dates) {
                /* remove quotes if any */
                comboBoxModel.addElement(s.replaceAll("\"", ""));
            }
            DateComboBox.setModel(comboBoxModel);
            DateComboBox.setEnabled(true);
            DateComboBox.addActionListener(dateComboBoxItemChangeListener);
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("---");
            DateComboBox.setModel(model);
            DateComboBox.setEnabled(false);
        }

        /* if climate params is empty (i.e. when no detections are found, maintain the "unknown" state */
        if (params.size() == 0 && !isOperatorEnabled){
            NotesTextArea.setText("None.");
            return;
        } else if (params.size() == 0) {
            NotesTextArea.setText("");
            return;
        }

        /* if history on climate params exists, set it */
        setParamsFromHistory(0);
    }

    /**
     * Set labels for the parameters, both for current values and for averages
     * @param current
     * @param currentValue
     * @param average
     * @param averageValue
     */
    private void setLblValues(JLabel current, String currentValue, JLabel average, String averageValue){
        if (!isOperatorEnabled) {
            current.setText(currentValue);
        }
        average.setText(averageValue);
    }

    /**
     * Compute the criticality level from the given number. If the decimal part is
     * different from 0, it computes sublevels too.
     * @param number
     * @return String
     */
    private String assignCriticalityLevelFromNumber(String number) {
        String integerPart;
        Integer decimalPart = 0;
        boolean isDecimalPartValid = false;
        /* get the integer and decimal parts of the number */
        try {
            decimalPart = Integer.parseInt(number.substring(number.indexOf(".") + 1));
            integerPart = number.substring(0, number.indexOf("."));
            isDecimalPartValid = true;
        } catch(java.lang.StringIndexOutOfBoundsException e) {
            integerPart = number;
        }

        String currentCriticality = criticality.get(integerPart);
        /* set sublevels according to the decimal part */
        if (isDecimalPartValid){
            String subLevel;
            if (decimalPart == 0) {
                subLevel = "";
            }
            else if (decimalPart < 10){
                subLevel = "RARELY";
            }
            else if (decimalPart < 40){
                subLevel = "OCCASIONALLY";
            }
            else if (decimalPart < 70) {
                subLevel = "FREQUENTLY";
            } else {
                subLevel = "NORMALLY";
            }
            currentCriticality = subLevel + " " + currentCriticality;
        }

        return currentCriticality;
    }

    /**
     * Double quote a string s
     * @param s
     * @return String
     */
    private String quoteString(String s){
        String out = "\"" + s + "\"";
        return out;
    }

    /**
     * Set parameters from historical values for idx-th record
     * @param idx
     */
    private void setParamsFromHistory(int idx){
        AverageTitleLbl.setText("Average (on a total of " + params.size() + " records)");

        // get all values for statistics
        ArrayList<Integer> winds = new ArrayList<>();
        ArrayList<Integer> humidities = new ArrayList<>();
        ArrayList<Integer> pressures = new ArrayList<>();
        ArrayList<Integer> temperatures = new ArrayList<>();
        ArrayList<Integer> rainfalls = new ArrayList<>();
        ArrayList<Integer> alts = new ArrayList<>();
        ArrayList<Integer> masses = new ArrayList<>();

        for (int j = 0; j < params.size(); j++ ) {
            winds.add(params.get(j).getWind());
            humidities.add(params.get(j).getHumidity());
            pressures.add(params.get(j).getPressure());
            temperatures.add(params.get(j).getTemperature());
            rainfalls.add(params.get(j).getRainfall());
            alts.add(params.get(j).getGlaciersAlt());
            masses.add(params.get(j).getGlaciersMass());
        }
        /* set wind */
        setLblValues(WindMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getWind())),
                WindAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(winds)));
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getHumidity())),
                HumidityAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(humidities)));
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getPressure())),
                PressureAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(pressures)));
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getTemperature())),
                TemperatureAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(temperatures)));
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getRainfall())),
                RainfallAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(rainfalls)));
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getGlaciersAlt())),
                GAltAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(alts)));
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, assignCriticalityLevelFromNumber(String.valueOf(params.get(idx).getGlaciersMass())),
                GMassAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(masses)));
        /* set notes */
        if (!isOperatorEnabled) {
            NotesTextArea.setText(params.get(idx).getNotes().replaceAll("\"", ""));
        }
    }

    /**
     * Set the operator UI view
     */
    private void setOperatorsView(){
        /* set combo boxes visible */
        WindMostRecentValueLbl.setVisible(false);
        HumidityMostRecentValueLbl.setVisible(false);
        PressureMostRecentValueLbl.setVisible(false);
        TemperatureMostRecentValueLbl.setVisible(false);
        RainfallMostRecentValueLbl.setVisible(false);
        GAltMostRecentValueLbl.setVisible(false);
        GMassMostRecentValueLbl.setVisible(false);
        WindComboBox.setVisible(true);
        HumidityComboBox.setVisible(true);
        PressureComboBox.setVisible(true);
        TemperatureComboBox.setVisible(true);
        RainfallComboBox.setVisible(true);
        GAltComboBox.setVisible(true);
        GMassComboBox.setVisible(true);
        NotesTextArea.setEditable(true);
        /* make save button visible */
        SaveBtn.setVisible(true);
    }

    /**
     * Reset all the fields to their default value
     */
    private void resetAllFields(){
        AverageTitleLbl.setText("Average (no detection found)");
        MostRecentTitleLbl.setText("Most recent detection");
        /* set wind */
        setLblValues(WindMostRecentValueLbl, Constants.NONE, WindAverageValueLbl, Constants.NONE);
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, Constants.NONE, HumidityAverageValueLbl, Constants.NONE);
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, Constants.NONE, PressureAverageValueLbl, Constants.NONE);
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, Constants.NONE, TemperatureAverageValueLbl, Constants.NONE);
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, Constants.NONE, RainfallAverageValueLbl, Constants.NONE);
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, Constants.NONE, GAltAverageValueLbl, Constants.NONE);
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, Constants.NONE, GMassAverageValueLbl, Constants.NONE);
        /* notes field */
        NotesTextArea.setText("");
        /* date combo box */
        DateComboBox.setEnabled(true);
        /* if operator mode, reset all the combo boxes */
        if (UtilsSingleton.getInstance().getWhoisLoggedIn() != null) {
            WindComboBox.setSelectedIndex(0);
            HumidityComboBox.setSelectedIndex(0);
            PressureComboBox.setSelectedIndex(0);
            TemperatureComboBox.setSelectedIndex(0);
            RainfallComboBox.setSelectedIndex(0);
            GAltComboBox.setSelectedIndex(0);
            GMassComboBox.setSelectedIndex(0);
            WindMostRecentValueLbl.setVisible(true);
            HumidityMostRecentValueLbl.setVisible(true);
            PressureMostRecentValueLbl.setVisible(true);
            TemperatureMostRecentValueLbl.setVisible(true);
            RainfallMostRecentValueLbl.setVisible(true);
            GAltMostRecentValueLbl.setVisible(true);
            GMassMostRecentValueLbl.setVisible(true);
            WindComboBox.setVisible(false);
            HumidityComboBox.setVisible(false);
            PressureComboBox.setVisible(false);
            TemperatureComboBox.setVisible(false);
            RainfallComboBox.setVisible(false);
            GAltComboBox.setVisible(false);
            GMassComboBox.setVisible(false);
            SaveBtn.setVisible(false);
            NotesTextArea.setEditable(false);
            AddBtn.setVisible(false);
        }
    }

    /***************************************************************

    CALLBACKS

     */

    /**
     * When the details page is closed, perform some reset actions,
     * for example:
     *  - set climate params info to "NONE"
     *  - reset the combo boxes values when in operator mode
     */
    private void DetailsPnl_at_visibility_change(){
        ParentPnl.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e)
            {
                JComponent component = (JComponent)e.getSource();
                if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                        &&  ! component.isShowing()){
                    resetAllFields();
                }
            }
        });
    };


    /**
     * Callback for the add button: add location to the places monitored by the monitoring
     * center of the operator that is logged in
     */
    private void addBtn_at_selection(){
        AddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* get the operator */
                UtilsSingleton utils = UtilsSingleton.getInstance();
                Operator operator = utils.getWhoisLoggedIn();

                /* get monitoring center of the operator */
                MonitoringCenter monitoringCenter = operator.getMonitoringCenter();

                /* add the location to the monitoring center */
                boolean rc = false;
                try {
                    rc = utils.getDbService().addLocationToMonitoringCenter(location, monitoringCenter);
                } catch (RemoteException ex) {
                    // what to do
                }

                if (rc){
                    JOptionPane.showMessageDialog(DetailsPnl, "This area is now registered in your monitoring center!", "",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(DetailsPnl, "Something went wrong, try again", "",
                            JOptionPane.ERROR_MESSAGE);
                }

                /* close the page */
                utils.getMainPnl().updateAvailableLocations(location);
                utils.switchPage("Main Page");
                PlaceNameLbl.setText("");
            }
        });
    }


    /**
     * Callback for the close button of the detailed location page
     */
    private void closeBtn_at_selection(){
        CloseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UtilsSingleton utils = UtilsSingleton.getInstance();
                Operator operator = utils.getWhoisLoggedIn();

                if (operator == null) {
                    try {
                        utils.getDbService().unregisterClientForLocation(utils.getClient());
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                utils.switchPage("Main Page");
                PlaceNameLbl.setText("");
             }
        });
    }


    /**
     * Callback for the save button:
     *  - get all the values
     *  - overwrite the ClimateParams object
     *  - overwrite the file
     *  - close the page
     */
    private void saveBtn_at_selection(){
        SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* local variables */
                UtilsSingleton utils = UtilsSingleton.getInstance();

                /* get values */
                int windItem = WindComboBox.getSelectedIndex() + 1;
                int humidityItem = HumidityComboBox.getSelectedIndex() + 1;
                int pressureItem = PressureComboBox.getSelectedIndex() + 1;
                int temperatureItem = TemperatureComboBox.getSelectedIndex() + 1;
                int rainfallItem = RainfallComboBox.getSelectedIndex() + 1;
                int galtItem = GAltComboBox.getSelectedIndex() + 1;
                int gmassItem = GMassComboBox.getSelectedIndex() + 1;

                /* set today */
                LocalDate ld = LocalDate.now();

                /* set also new notes */
                String notes = NotesTextArea.getText();
                String filteredNotes = "";
                for (int i = 0; i < notes.length(); i++) {
                    if (i >= Constants.NOTES_MAX_CHAR_NUM) {
                        break;
                    }
                    filteredNotes += notes.charAt(i);
                }

                ClimateParameter cp = new ClimateParameter(location.getGeonameID(), windItem, humidityItem, pressureItem, temperatureItem,
                        rainfallItem, galtItem, gmassItem, filteredNotes, ld, utils.getWhoisLoggedIn().getTaxCode());

                boolean rc = false;
                try {
                    rc = utils.getDbService().pushClimateParameter(cp);
                } catch (RemoteException ex) {
                }

                if (!rc) {
                    JOptionPane.showMessageDialog(DetailsPnl, "Something went wrong, try again", "",
                            JOptionPane.ERROR_MESSAGE);
                }

                utils.switchPage("Main Page");
                PlaceNameLbl.setText("");
            }
        });
    }

    private ActionListener dateComboBoxItemChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIdx = DateComboBox.getSelectedIndex();
            setParamsFromHistory(selectedIdx);
            setAboutLastRecordTextArea(selectedIdx);
        }
    };


    /**
     * Callback called when the selected date changes
     */
    private void dateComboBox_at_item_change() {
        DateComboBox.addActionListener(dateComboBoxItemChangeListener);
    }


    /**
     * Callback for check on the number of characters in notes
     */
    private void notesTextArea_at_change(){
        NotesTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkMaxNumberReached();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { checkMaxNumberReached(); }

            @Override
            public void changedUpdate(DocumentEvent e) { }

            private void checkMaxNumberReached(){
                if (NotesTextArea.getText().length() > Constants.NOTES_MAX_CHAR_NUM) {
                    NotesErrorPnl.setVisible(true);
                }
                else {
                    NotesErrorPnl.setVisible(false);
                }
            }
        });
    }


    /**
     * main method
     *
     */
    public static void main(String[] args) {
        DetailsPage detailsPage = new DetailsPage();
    }

}
