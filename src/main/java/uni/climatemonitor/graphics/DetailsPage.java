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
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
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


    public DetailsPage() {
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
        NotesPnl.setSize(new Dimension(150, 50));
        NotesPnl.setMaximumSize(new Dimension(150, 50));
        NotesPnl.setMinimumSize(new Dimension(150, 50));

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
     *
     * @param measures
     * @return String
     */
    private String computeAverage(ArrayList<Integer> measures) {
        float average = 0f;
        int numOfMeasures = measures.size();
        for (Integer measure : measures) {
            average += (float) measure;
        }
        average = average / ((float) numOfMeasures);

        String averageStr = String.format(Locale.US, "%.2f", average);
        return averageStr;
    }

    /**
     * Set the about field for the idx-th record
     *
     * @param idx
     */
    private void setAboutLastRecordTextArea(int idx) {
        AboutLastRecordTextArea.setBackground(new Color(238, 238, 238));
        if (params.size() != 0) {
            String who = params.get(idx).getWho();
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
     *
     * @param loc
     */
    public void setUIPnl(Location loc) {
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
        if (params.size() == 0 && !isOperatorEnabled) {
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
     *
     * @param current
     * @param currentValue
     * @param average
     * @param averageValue
     */
    private void setLblValues(JLabel current, String currentValue, JLabel average, String averageValue) {
        if (!isOperatorEnabled) {
            current.setText(currentValue);
        }
        average.setText(averageValue);
    }

    /**
     * Compute the criticality level from the given number. If the decimal part is
     * different from 0, it computes sublevels too.
     *
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
        } catch (StringIndexOutOfBoundsException e) {
            integerPart = number;
        }

        String currentCriticality = criticality.get(integerPart);
        /* set sublevels according to the decimal part */
        if (isDecimalPartValid) {
            String subLevel;
            if (decimalPart == 0) {
                subLevel = "";
            } else if (decimalPart < 10) {
                subLevel = "RARELY";
            } else if (decimalPart < 40) {
                subLevel = "OCCASIONALLY";
            } else if (decimalPart < 70) {
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
     *
     * @param s
     * @return String
     */
    private String quoteString(String s) {
        String out = "\"" + s + "\"";
        return out;
    }

    /**
     * Set parameters from historical values for idx-th record
     *
     * @param idx
     */
    private void setParamsFromHistory(int idx) {
        AverageTitleLbl.setText("Average (on a total of " + params.size() + " records)");

        // get all values for statistics
        ArrayList<Integer> winds = new ArrayList<>();
        ArrayList<Integer> humidities = new ArrayList<>();
        ArrayList<Integer> pressures = new ArrayList<>();
        ArrayList<Integer> temperatures = new ArrayList<>();
        ArrayList<Integer> rainfalls = new ArrayList<>();
        ArrayList<Integer> alts = new ArrayList<>();
        ArrayList<Integer> masses = new ArrayList<>();

        for (int j = 0; j < params.size(); j++) {
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
    private void setOperatorsView() {
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
    private void resetAllFields() {
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
     * - set climate params info to "NONE"
     * - reset the combo boxes values when in operator mode
     */
    private void DetailsPnl_at_visibility_change() {
        ParentPnl.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                JComponent component = (JComponent) e.getSource();
                if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                        && !component.isShowing()) {
                    resetAllFields();
                }
            }
        });
    }

    ;


    /**
     * Callback for the add button: add location to the places monitored by the monitoring
     * center of the operator that is logged in
     */
    private void addBtn_at_selection() {
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

                if (rc) {
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
    private void closeBtn_at_selection() {
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
     * - get all the values
     * - overwrite the ClimateParams object
     * - overwrite the file
     * - close the page
     */
    private void saveBtn_at_selection() {
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
    private void notesTextArea_at_change() {
        NotesTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkMaxNumberReached();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkMaxNumberReached();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void checkMaxNumberReached() {
                if (NotesTextArea.getText().length() > Constants.NOTES_MAX_CHAR_NUM) {
                    NotesErrorPnl.setVisible(true);
                } else {
                    NotesErrorPnl.setVisible(false);
                }
            }
        });
    }


    /**
     * main method
     */
    public static void main(String[] args) {
        DetailsPage detailsPage = new DetailsPage();
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
        ParentPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ParentPnl.setMinimumSize(new Dimension(1200, 800));
        LocationDetailPnl = new JPanel();
        LocationDetailPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        ParentPnl.add(LocationDetailPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ClosePnl = new JPanel();
        ClosePnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        LocationDetailPnl.add(ClosePnl, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ButtonsPnl = new JPanel();
        ButtonsPnl.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        ClosePnl.add(ButtonsPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        SaveBtn = new JButton();
        SaveBtn.setBorderPainted(false);
        SaveBtn.setContentAreaFilled(false);
        SaveBtn.setHorizontalAlignment(4);
        SaveBtn.setText("Save");
        SaveBtn.setVisible(false);
        ButtonsPnl.add(SaveBtn);
        AddBtn = new JButton();
        AddBtn.setBorderPainted(false);
        AddBtn.setContentAreaFilled(false);
        AddBtn.setText("Add");
        AddBtn.setVisible(false);
        ButtonsPnl.add(AddBtn);
        CloseBtn = new JButton();
        CloseBtn.setBorderPainted(false);
        CloseBtn.setContentAreaFilled(false);
        CloseBtn.setHorizontalAlignment(4);
        CloseBtn.setHorizontalTextPosition(4);
        CloseBtn.setOpaque(false);
        CloseBtn.setText("Close");
        ButtonsPnl.add(CloseBtn);
        DetailsPnl = new JPanel();
        DetailsPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 20, 0, 0), -1, -1));
        LocationDetailPnl.add(DetailsPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(1000, -1), new Dimension(1000, -1), new Dimension(1000, -1), 0, false));
        PlaceNamePnl = new JPanel();
        PlaceNamePnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(20, 0, 0, 0), -1, -1));
        DetailsPnl.add(PlaceNamePnl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        PlaceNameLbl = new JLabel();
        PlaceNameLbl.setAutoscrolls(false);
        Font PlaceNameLblFont = this.$$$getFont$$$(null, Font.BOLD, 24, PlaceNameLbl.getFont());
        if (PlaceNameLblFont != null) PlaceNameLbl.setFont(PlaceNameLblFont);
        PlaceNameLbl.setHorizontalAlignment(0);
        PlaceNameLbl.setText("AAAAA");
        PlaceNamePnl.add(PlaceNameLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 5, false));
        ChooseDatePnl = new JPanel();
        ChooseDatePnl.setLayout(new BorderLayout(0, 0));
        PlaceNamePnl.add(ChooseDatePnl, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(133, 21), null, 0, false));
        DateComboBox = new JComboBox();
        ChooseDatePnl.add(DateComboBox, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Go to previous record:");
        ChooseDatePnl.add(label1, BorderLayout.NORTH);
        ParamsPnl = new JPanel();
        ParamsPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 3, new Insets(10, 20, 10, 20), -1, 10));
        DetailsPnl.add(ParamsPnl, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ParamsPnl.setBorder(BorderFactory.createTitledBorder(null, "Weather Parameters", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, null, null));
        WindMostRecentPnl = new JPanel();
        WindMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(WindMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        WindMostRecentValueLbl = new JLabel();
        Font WindMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, WindMostRecentValueLbl.getFont());
        if (WindMostRecentValueLblFont != null) WindMostRecentValueLbl.setFont(WindMostRecentValueLblFont);
        WindMostRecentValueLbl.setText("NONE");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        WindMostRecentPnl.add(WindMostRecentValueLbl, gbc);
        WindComboBox = new JComboBox();
        WindComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("EXCELLENT");
        defaultComboBoxModel1.addElement("FAVORABLE");
        defaultComboBoxModel1.addElement("MODERATE");
        defaultComboBoxModel1.addElement("SEVERE");
        defaultComboBoxModel1.addElement("CRITICAL");
        WindComboBox.setModel(defaultComboBoxModel1);
        WindComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        WindMostRecentPnl.add(WindComboBox, gbc);
        WindAveragePnl = new JPanel();
        WindAveragePnl.setLayout(new GridBagLayout());
        ParamsPnl.add(WindAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        WindAverageValueLbl = new JLabel();
        Font WindAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, WindAverageValueLbl.getFont());
        if (WindAverageValueLblFont != null) WindAverageValueLbl.setFont(WindAverageValueLblFont);
        WindAverageValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        WindAveragePnl.add(WindAverageValueLbl, gbc);
        AverageTitleLbl = new JLabel();
        AverageTitleLbl.setText("Average (on a total of ... records)");
        ParamsPnl.add(AverageTitleLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MostRecentTitleLbl = new JLabel();
        MostRecentTitleLbl.setText("Most recent record");
        ParamsPnl.add(MostRecentTitleLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        WindLbl = new JLabel();
        WindLbl.setText("Wind");
        ParamsPnl.add(WindLbl, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        HumidityMostRecentPnl = new JPanel();
        HumidityMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(HumidityMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        HumidityMostRecentValueLbl = new JLabel();
        Font HumidityMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, HumidityMostRecentValueLbl.getFont());
        if (HumidityMostRecentValueLblFont != null) HumidityMostRecentValueLbl.setFont(HumidityMostRecentValueLblFont);
        HumidityMostRecentValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        HumidityMostRecentPnl.add(HumidityMostRecentValueLbl, gbc);
        HumidityComboBox = new JComboBox();
        HumidityComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("EXCELLENT");
        defaultComboBoxModel2.addElement("FAVORABLE");
        defaultComboBoxModel2.addElement("MODERATE");
        defaultComboBoxModel2.addElement("SEVERE");
        defaultComboBoxModel2.addElement("CRITICAL");
        HumidityComboBox.setModel(defaultComboBoxModel2);
        HumidityComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        HumidityMostRecentPnl.add(HumidityComboBox, gbc);
        HumidityLbl = new JLabel();
        HumidityLbl.setText("Humidity");
        ParamsPnl.add(HumidityLbl, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        HumidityAveragePnl = new JPanel();
        HumidityAveragePnl.setLayout(new GridBagLayout());
        ParamsPnl.add(HumidityAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        HumidityAverageValueLbl = new JLabel();
        Font HumidityAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, HumidityAverageValueLbl.getFont());
        if (HumidityAverageValueLblFont != null) HumidityAverageValueLbl.setFont(HumidityAverageValueLblFont);
        HumidityAverageValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        HumidityAveragePnl.add(HumidityAverageValueLbl, gbc);
        PressureMostRecentPnl = new JPanel();
        PressureMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(PressureMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        PressureMostRecentValueLbl = new JLabel();
        Font PressureMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, PressureMostRecentValueLbl.getFont());
        if (PressureMostRecentValueLblFont != null) PressureMostRecentValueLbl.setFont(PressureMostRecentValueLblFont);
        PressureMostRecentValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        PressureMostRecentPnl.add(PressureMostRecentValueLbl, gbc);
        PressureComboBox = new JComboBox();
        PressureComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("EXCELLENT");
        defaultComboBoxModel3.addElement("FAVORABLE");
        defaultComboBoxModel3.addElement("MODERATE");
        defaultComboBoxModel3.addElement("SEVERE");
        defaultComboBoxModel3.addElement("CRITICAL");
        PressureComboBox.setModel(defaultComboBoxModel3);
        PressureComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        PressureMostRecentPnl.add(PressureComboBox, gbc);
        PressureAveragePnl = new JPanel();
        PressureAveragePnl.setLayout(new GridBagLayout());
        ParamsPnl.add(PressureAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        PressureAverageValueLbl = new JLabel();
        Font PressureAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, PressureAverageValueLbl.getFont());
        if (PressureAverageValueLblFont != null) PressureAverageValueLbl.setFont(PressureAverageValueLblFont);
        PressureAverageValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        PressureAveragePnl.add(PressureAverageValueLbl, gbc);
        PressureLbl = new JLabel();
        PressureLbl.setText("Pressure");
        ParamsPnl.add(PressureLbl, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        TemperatureMostRecentPnl = new JPanel();
        TemperatureMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(TemperatureMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        TemperatureMostRecentValueLbl = new JLabel();
        Font TemperatureMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, TemperatureMostRecentValueLbl.getFont());
        if (TemperatureMostRecentValueLblFont != null)
            TemperatureMostRecentValueLbl.setFont(TemperatureMostRecentValueLblFont);
        TemperatureMostRecentValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        TemperatureMostRecentPnl.add(TemperatureMostRecentValueLbl, gbc);
        TemperatureComboBox = new JComboBox();
        TemperatureComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("EXCELLENT");
        defaultComboBoxModel4.addElement("FAVORABLE");
        defaultComboBoxModel4.addElement("MODERATE");
        defaultComboBoxModel4.addElement("SEVERE");
        defaultComboBoxModel4.addElement("CRITICAL");
        TemperatureComboBox.setModel(defaultComboBoxModel4);
        TemperatureComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        TemperatureMostRecentPnl.add(TemperatureComboBox, gbc);
        TemperatureAveragePnl = new JPanel();
        TemperatureAveragePnl.setLayout(new GridBagLayout());
        ParamsPnl.add(TemperatureAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        TemperatureAverageValueLbl = new JLabel();
        Font TemperatureAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, TemperatureAverageValueLbl.getFont());
        if (TemperatureAverageValueLblFont != null) TemperatureAverageValueLbl.setFont(TemperatureAverageValueLblFont);
        TemperatureAverageValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        TemperatureAveragePnl.add(TemperatureAverageValueLbl, gbc);
        TemperatureLbl = new JLabel();
        TemperatureLbl.setText("Temperature");
        ParamsPnl.add(TemperatureLbl, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RainfallMostRecentPnl = new JPanel();
        RainfallMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(RainfallMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RainfallMostRecentValueLbl = new JLabel();
        Font RainfallMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, RainfallMostRecentValueLbl.getFont());
        if (RainfallMostRecentValueLblFont != null) RainfallMostRecentValueLbl.setFont(RainfallMostRecentValueLblFont);
        RainfallMostRecentValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        RainfallMostRecentPnl.add(RainfallMostRecentValueLbl, gbc);
        RainfallComboBox = new JComboBox();
        RainfallComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("EXCELLENT");
        defaultComboBoxModel5.addElement("FAVORABLE");
        defaultComboBoxModel5.addElement("MODERATE");
        defaultComboBoxModel5.addElement("SEVERE");
        defaultComboBoxModel5.addElement("CRITICAL");
        RainfallComboBox.setModel(defaultComboBoxModel5);
        RainfallComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        RainfallMostRecentPnl.add(RainfallComboBox, gbc);
        RainfallAveragePnl = new JPanel();
        RainfallAveragePnl.setLayout(new GridBagLayout());
        ParamsPnl.add(RainfallAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RainfallAverageValueLbl = new JLabel();
        Font RainfallAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, RainfallAverageValueLbl.getFont());
        if (RainfallAverageValueLblFont != null) RainfallAverageValueLbl.setFont(RainfallAverageValueLblFont);
        RainfallAverageValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        RainfallAveragePnl.add(RainfallAverageValueLbl, gbc);
        RainfallLbl = new JLabel();
        RainfallLbl.setText("Rainfall");
        ParamsPnl.add(RainfallLbl, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GAltMostRecentPnl = new JPanel();
        GAltMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(GAltMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        GAltMostRecentValueLbl = new JLabel();
        Font GAltMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, GAltMostRecentValueLbl.getFont());
        if (GAltMostRecentValueLblFont != null) GAltMostRecentValueLbl.setFont(GAltMostRecentValueLblFont);
        GAltMostRecentValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        GAltMostRecentPnl.add(GAltMostRecentValueLbl, gbc);
        GAltComboBox = new JComboBox();
        GAltComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel6 = new DefaultComboBoxModel();
        defaultComboBoxModel6.addElement("EXCELLENT");
        defaultComboBoxModel6.addElement("FAVORABLE");
        defaultComboBoxModel6.addElement("MODERATE");
        defaultComboBoxModel6.addElement("SEVERE");
        defaultComboBoxModel6.addElement("CRITICAL");
        GAltComboBox.setModel(defaultComboBoxModel6);
        GAltComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        GAltMostRecentPnl.add(GAltComboBox, gbc);
        GAltAveragePnl = new JPanel();
        GAltAveragePnl.setLayout(new GridBagLayout());
        ParamsPnl.add(GAltAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        GAltAverageValueLbl = new JLabel();
        Font GAltAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, GAltAverageValueLbl.getFont());
        if (GAltAverageValueLblFont != null) GAltAverageValueLbl.setFont(GAltAverageValueLblFont);
        GAltAverageValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        GAltAveragePnl.add(GAltAverageValueLbl, gbc);
        GAltLbl = new JLabel();
        GAltLbl.setText("Glaciers Altitude");
        ParamsPnl.add(GAltLbl, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GMassMostRecentPnl = new JPanel();
        GMassMostRecentPnl.setLayout(new GridBagLayout());
        ParamsPnl.add(GMassMostRecentPnl, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        GMassMostRecentValueLbl = new JLabel();
        Font GMassMostRecentValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, GMassMostRecentValueLbl.getFont());
        if (GMassMostRecentValueLblFont != null) GMassMostRecentValueLbl.setFont(GMassMostRecentValueLblFont);
        GMassMostRecentValueLbl.setText("NONE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        GMassMostRecentPnl.add(GMassMostRecentValueLbl, gbc);
        GMassComboBox = new JComboBox();
        GMassComboBox.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel7 = new DefaultComboBoxModel();
        defaultComboBoxModel7.addElement("EXCELLENT");
        defaultComboBoxModel7.addElement("FAVORABLE");
        defaultComboBoxModel7.addElement("MODERATE");
        defaultComboBoxModel7.addElement("SEVERE");
        defaultComboBoxModel7.addElement("CRITICAL");
        GMassComboBox.setModel(defaultComboBoxModel7);
        GMassComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        GMassMostRecentPnl.add(GMassComboBox, gbc);
        GMassLbl = new JLabel();
        GMassLbl.setText("Glaciers Mass");
        ParamsPnl.add(GMassLbl, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GMassAveragePnl = new JPanel();
        GMassAveragePnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ParamsPnl.add(GMassAveragePnl, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        GMassAverageValueLbl = new JLabel();
        Font GMassAverageValueLblFont = this.$$$getFont$$$(null, Font.PLAIN, -1, GMassAverageValueLbl.getFont());
        if (GMassAverageValueLblFont != null) GMassAverageValueLbl.setFont(GMassAverageValueLblFont);
        GMassAverageValueLbl.setText("NONE");
        GMassAveragePnl.add(GMassAverageValueLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NotesPnl = new JPanel();
        NotesPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(20, 60, 0, 40), -1, -1));
        ParamsPnl.add(NotesPnl, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        NotesTextArea = new JTextArea();
        NotesTextArea.setBackground(new Color(-1));
        NotesTextArea.setEditable(false);
        NotesTextArea.setEnabled(true);
        Font NotesTextAreaFont = this.$$$getFont$$$(null, Font.PLAIN, -1, NotesTextArea.getFont());
        if (NotesTextAreaFont != null) NotesTextArea.setFont(NotesTextAreaFont);
        NotesTextArea.setForeground(new Color(-16777216));
        NotesTextArea.setLineWrap(true);
        NotesTextArea.setMargin(new Insets(2, 2, 2, 2));
        NotesTextArea.setRows(2);
        NotesTextArea.setText("");
        NotesTextArea.setWrapStyleWord(false);
        NotesPnl.add(NotesTextArea, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 80), null, 0, false));
        NotesErrorPnl = new JPanel();
        NotesErrorPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        NotesPnl.add(NotesErrorPnl, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MaxNumOfCharErrLbl = new JLabel();
        Font MaxNumOfCharErrLblFont = this.$$$getFont$$$(null, Font.ITALIC, -1, MaxNumOfCharErrLbl.getFont());
        if (MaxNumOfCharErrLblFont != null) MaxNumOfCharErrLbl.setFont(MaxNumOfCharErrLblFont);
        MaxNumOfCharErrLbl.setText("Max number of characters reached! Note will be cut!");
        MaxNumOfCharErrLbl.setVisible(true);
        NotesErrorPnl.add(MaxNumOfCharErrLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NotesLbl = new JLabel();
        NotesLbl.setText("Generic Notes");
        ParamsPnl.add(NotesLbl, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        ParamsPnl.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        DetailsPnl.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        AboutLastRecordPnl = new JPanel();
        AboutLastRecordPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        DetailsPnl.add(AboutLastRecordPnl, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        AboutLastRecordPnl.setBorder(BorderFactory.createTitledBorder(null, "About the Monitoring Center", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        AboutLastRecordTextArea = new JTextArea();
        AboutLastRecordTextArea.setEditable(false);
        Font AboutLastRecordTextAreaFont = this.$$$getFont$$$(null, Font.PLAIN, -1, AboutLastRecordTextArea.getFont());
        if (AboutLastRecordTextAreaFont != null) AboutLastRecordTextArea.setFont(AboutLastRecordTextAreaFont);
        AboutLastRecordTextArea.setForeground(new Color(-16777216));
        AboutLastRecordTextArea.setLineWrap(true);
        AboutLastRecordTextArea.setMargin(new Insets(15, 40, 15, 40));
        AboutLastRecordTextArea.setText("");
        AboutLastRecordPnl.add(AboutLastRecordTextArea, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        LocationDetailPnl.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
