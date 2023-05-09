/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import uni.climatemonitor.data.ClimateParams;
import uni.climatemonitor.data.Location;
import uni.climatemonitor.data.MonitoringCenter;
import uni.climatemonitor.data.Operator;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


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
    private ClimateParams params;

    /* operator info */
    boolean isOperatorEnabled = false;

    /* weather criticality levels */
    HashMap<String, String> criticality = new HashMap<>();

    /* info about last record (to be formatted) */
    private String aboutLastRecord =
            """
This detection has been recorded by operator %s, from Monitoring Center "%s", on %s.

%s is a monitoring center based in %s and is currently monitoring the following areas:
%s.
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
    }


    /*********************************************************

        UTILS

     */
    private String computeAverage(ArrayList<String> measures){
        float average = 0f;
        int numOfMeasures = measures.size();
        for (String measure : measures){
            average += Float.parseFloat(measure);
        }
        average = average / ((float) numOfMeasures);

        String averageStr = String.format(Locale.US, "%.2f", average);
        return averageStr;
    }

    private boolean isOperatorEnabledForThisPlace(){
        /* local variables */
        boolean out = false;
        UtilsSingleton utils = UtilsSingleton.getInstance();

        Operator operator = utils.getWhoisLoggedIn();

        if (operator != null){
            ArrayList<String> monitoredAreas = utils.getCentersData().getEnabledLocationsForOperator(operator);
            /* check if the chosen location is in the areas monitored by the operator logged in */
            for (String s : monitoredAreas){
                if (s.equals(location.getGeonameID())) {
                    out = true;
                }
            }
        }

        return out;
    }

    private void setAboutLastRecordTestArea(int idx){
        UtilsSingleton utils = UtilsSingleton.getInstance();
        AboutLastRecordTextArea.setBackground(new Color(238,238,238));
        if (params != null) {
            String monCenter = params.getCenter().get(idx).replaceAll("\"", "");
            String who =  params.getWho().get(idx);
            String date = params.getDate().get(idx).replaceAll("\"", "");
            MonitoringCenter mc = utils.getCentersData().getMonitoringCenterFromName(monCenter);
            String monCenterInfo = mc.getAddress();
            ArrayList<String> monCenterArea = mc.getMonitoredAreas();
            ArrayList<String> monitoredAreas = new ArrayList<>();
            for (String s : monCenterArea) {
                monitoredAreas.add(utils.getGeoData().getLocationFromGeoID(s).getAsciiName());
            }
            String areas = monitoredAreas.get(0);
            for (int i = 1; i < monitoredAreas.size(); i++){
                areas += ", " + monitoredAreas.get(i);
            }
            String info = String.format(aboutLastRecord, who, monCenter, date, monCenter, monCenterInfo, areas);
            AboutLastRecordTextArea.setText(info);
        } else {
            AboutLastRecordTextArea.setText("No record found.");
        }
    }

    public void setUIPnl(Location loc){
        location = loc;
        UtilsSingleton utils = UtilsSingleton.getInstance();
        Operator operator = utils.getWhoisLoggedIn();
        params = utils.getGeoData().getClimateParamsFor(location.getGeonameID());
        isOperatorEnabled = isOperatorEnabledForThisPlace();

        NotesErrorPnl.setVisible(false);

        /* set info about the last detection and the monitoring center */
        AboutLastRecordPnl.setVisible(!isOperatorEnabled);
        if (!isOperatorEnabled) {
            setAboutLastRecordTestArea(0);
        }

        PlaceNameLbl.setText(location.toStringNoCoordinates());

        DateComboBox.setPreferredSize(new Dimension(185, 24));
        if (!isOperatorEnabled && params != null) {
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            for (String s : params.getDate()) {
                /* remove quotes if any */
                comboBoxModel.addElement(s.replaceAll("\"", ""));
            }
            DateComboBox.setModel(comboBoxModel);
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("---");
            DateComboBox.setModel(model);
            DateComboBox.setEnabled(false);
        }

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

        /* if climate params is null (i.e. when no detections are found, maintain the "unknown" state */
        if (params == null && !isOperatorEnabled){
            NotesTextArea.setText("None.");
            return;
        } else if (params == null) {
            NotesTextArea.setText("");
            return;
        }

        /* if history on climate params exists, set it */
        setParamsFromHistory(0);
    }

    private void setLblValues(JLabel current, String currentValue, JLabel average, String averageValue){
        if (!isOperatorEnabled) {
            current.setText(currentValue);
        }
        average.setText(averageValue);
    }

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

    private String quoteString(String s){
        String out = "\"" + s + "\"";
        return out;
    }

    private void setParamsFromHistory(int idx){
        AverageTitleLbl.setText("Average (on a total of " + params.getTot_measure() + " records)");

        /* set wind */
        setLblValues(WindMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getWind().get(idx)),
                WindAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getWind())));
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getHumidity().get(idx)),
                HumidityAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getHumidity())));
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getPressure().get(idx)),
                PressureAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getPressure())));
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getTemperature().get(idx)),
                TemperatureAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getTemperature())));
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getRainfall().get(idx)),
                RainfallAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getRainfall())));
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getGlacier_alt().get(idx)),
                GAltAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getGlacier_alt())));
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, assignCriticalityLevelFromNumber(params.getGlacier_mass().get(idx)),
                GMassAverageValueLbl, assignCriticalityLevelFromNumber(computeAverage(params.getGlacier_mass())));
        /* set notes */
        if (!isOperatorEnabled) {
            NotesTextArea.setText(params.getNotes().get(idx).replaceAll("\"", ""));
        }
    }

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
     * center off the operator that is logged in
     */
    private void addBtn_at_selection(){
        AddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* get the operator */
                UtilsSingleton utils = UtilsSingleton.getInstance();
                Operator operator = utils.getWhoisLoggedIn();

                /* get monitoring center of the operator */
                String mc = operator.getMonitoringCenter();
                MonitoringCenter monitoringCenter = utils.getCentersData().getMonitoringCenterFromName(mc);

                /* add the location to the monitoring center */
                monitoringCenter.getMonitoredAreas().add(location.getGeonameID());

                /* rewrite the file */
                utils.getCentersData().updateMonitoringCentersFile();

                /* close the page */
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

                /* compute new averages:
                * note that tot_measure is increased by 1,
                * because when you call the save button
                * you are actually adding a new measurement */
                if (params != null) {
                    params.setTot_measure(params.getTot_measure() + 1);
                } else {
                    /* this is the case in which the climate params object for the selected location does not exist. In this case,
                    * create it */
                    params = new ClimateParams();
                    params.setTot_measure(1);
                    params.setGeonameID(location.getGeonameID());
                    params.setState(location.getState());
                    params.setAscii_name(location.getAsciiName());
                    utils.getGeoData().addClimateParams(params);
                }

                /* overwrite the object */
                params.getWind().add(0, String.format("%d", windItem));
                params.getHumidity().add(0, String.format("%d", humidityItem));
                params.getPressure().add(0, String.format("%d", pressureItem));
                params.getTemperature().add(0, String.format("%d", temperatureItem));
                params.getRainfall().add(0, String.format("%d", rainfallItem));
                params.getGlacier_alt().add(0, String.format("%d", galtItem));
                params.getGlacier_mass().add(0, String.format("%d", gmassItem));
                params.getWho().add(0,  quoteString(utils.getWhoisLoggedIn().getName()));
                params.getCenter().add(0, quoteString(utils.getWhoisLoggedIn().getMonitoringCenter() ));

                /* set today */
                LocalDateTime ld = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
                String dateString = quoteString(ld.format(formatter));

                params.getDate().add(0, dateString);
                /* set also new notes */
                String notes = NotesTextArea.getText();
                String filteredNotes = "";
                for (int i = 0; i < notes.length(); i++){
                    if (i >= Constants.NOTES_MAX_CHAR_NUM) {break;}
                    filteredNotes += notes.charAt(i);
                }
                params.getNotes().add(0, quoteString(filteredNotes));

                /* update file */
                utils.getGeoData().updateClimateParamsFile();

                utils.switchPage("Main Page");
                PlaceNameLbl.setText("");
            }
        });
    }


    /**
     * Callback called when the selected date changes
     */
    private void dateComboBox_at_item_change() {
        DateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = DateComboBox.getSelectedIndex();
                setParamsFromHistory(selectedIdx);
                setAboutLastRecordTestArea(selectedIdx);
            }
        });
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
