package uni.climatemonitor.graphics;

import uni.climatemonitor.data.ClimateParams;
import uni.climatemonitor.data.Location;
import uni.climatemonitor.data.Operator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

public class DetailsPage {
    private JLabel PlaceNameLbl;
    private JButton CloseBtn;
    private JPanel ParentPnl;
    private JPanel LocationDetailPnl;
    private JPanel DetailsPnl;
    private JPanel ClosePnl;
    private JPanel PlaceNamePnl;
    private JLabel WindMostRecentValueLbl;
    private JPanel WindPnl;
    private JLabel WindAverageValueLbl;
    private JPanel ParametersContainer;
    private JPanel Humidity;
    private JLabel HumidityMostRecentValueLbl;
    private JLabel HumidityAverageValueLbl;
    private JPanel Pressure;
    private JLabel PressureMostRecentValueLbl;
    private JLabel PressureAverageValueLbl;
    private JPanel ColumnsPnl;
    private JLabel AverageTitleLbl;
    private JLabel MostRecentTitleLbl;
    private JPanel Temperature;
    private JLabel TemperatureMostRecentValueLbl;
    private JLabel TemperatureAverageValueLbl;
    private JPanel Rainfall;
    private JLabel RainfallMostRecentValueLbl;
    private JLabel RainfallAverageValueLbl;
    private JPanel GlaciersAlt;
    private JLabel GAltMostRecentValueLbl;
    private JLabel GAltAverageValueLbl;
    private JPanel GlaciersMass;
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
    private JPanel GMassAveragePnl;
    private JComboBox GMassComboBox;
    private JTextArea NotesTextArea;
    private JPanel NotesPnl;
    private JButton SaveBtn;
    private JPanel NotesSurrounderPnl;
    private JPanel ButtonsPnl;

    /* location info */
    private Location location;
    private ClimateParams params;

    public DetailsPage(){
        /*
            Callbacks for the detailed location page
         */

        /* close the detailed location page */
        closeBtn_at_selection();
        /* reset action when the page is closed */
        DetailsPnl_at_visibility_change();
        /* at save button click */
        saveBtn_at_selection();
    }


    /*********************************************************

        UTILS

     */
    private String averageBetween(String previousAverage, int currentDetection, int totDetections){
        float average;
        float prevAve;
        float N = (float) totDetections;
        prevAve = Float.parseFloat(previousAverage);
        average = ( N - 1.0f ) * prevAve + (float) currentDetection;
        average = average / N;

        String averageStr = String.format("%.2f", average);
        return averageStr;
    }

    private boolean isOperatorEnabledForThisPlace(){
        /* local variables */
        boolean out = false;
        UtilsSingleton utils = UtilsSingleton.getInstance();

        Operator operator = utils.getWhoisLoggedIn();

        if (operator != null){
            String[] monitoredAreas = utils.getCentersData().getEnabledLocationsForOperator(operator);
            /* check if the chosen location is in the areas monitored by the operator logged in */
            for (String s : monitoredAreas){
                if (s.equals(location.getGeonameID())) {
                    out = true;
                }
            }
        }

        return out;
    }

    public void setUIPnl(Location loc, ClimateParams par){
        location = loc;
        params = par;
        PlaceNameLbl.setText(location.toString());

        /* if an operator is logged in, set the combo box for detections and remove current values */
        if (isOperatorEnabledForThisPlace()) {
            setOperatorsView();
            MostRecentTitleLbl.setText("Set new detection");
        }

        /* set notes area settings */
        NotesTextArea.setBackground(new Color(238, 238, 238));
        NotesTextArea.setSize(new Dimension(200, 150));
        NotesTextArea.setMaximumSize(new Dimension(200, 150));
        NotesTextArea.setMinimumSize(new Dimension(200, 150));
        NotesPnl.setSize(new Dimension(200,150));
        NotesPnl.setMaximumSize(new Dimension(200,150));
        NotesPnl.setMinimumSize(new Dimension(200,150));

        /* if climate params is null (i.e. when no detections are found, maintain the "unknown" state */
        if (par == null){ return; }

        /* if history on climate params exists, set it */
        setParamsFromHistory();

    }

    private void setLblValues(JLabel current, String currentValue, JLabel average, String averageValue){
        if (UtilsSingleton.getInstance().getWhoisLoggedIn() == null) {
            current.setText(currentValue + " / 5");
        }
        average.setText(averageValue + " / 5");
    }

    private void setParamsFromHistory(){
        AverageTitleLbl.setText("Average (on a total of " + params.getTot_measure() + " detections)");

        /* set wind */
        setLblValues(WindMostRecentValueLbl, params.getWind()[0], WindAverageValueLbl, params.getWind()[1]);
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, params.getHumidity()[0], HumidityAverageValueLbl, params.getHumidity()[1]);
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, params.getPressure()[0], PressureAverageValueLbl, params.getPressure()[1]);
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, params.getTemperature()[0], TemperatureAverageValueLbl, params.getTemperature()[1]);
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, params.getRainfall()[0], RainfallAverageValueLbl, params.getRainfall()[1]);
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, params.getGlacier_alt()[0], GAltAverageValueLbl, params.getGlacier_alt()[1]);
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, params.getGlacier_mass()[0], GMassAverageValueLbl, params.getGlacier_mass()[1]);
        /* set notes */
        NotesTextArea.setText(params.getNotes());
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
        setLblValues(WindMostRecentValueLbl, "??", WindAverageValueLbl, "??");
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, "??", HumidityAverageValueLbl, "??");
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, "??", PressureAverageValueLbl, "??");
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, "??", TemperatureAverageValueLbl, "??");
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, "??", RainfallAverageValueLbl, "??");
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, "??", GAltAverageValueLbl, "??");
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, "??", GMassAverageValueLbl, "??");
        /* notes field */
        NotesTextArea.setText("");
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
        }
    }

    /***************************************************************

    CALLBACKS

     */

    /**
     * When the details page is closed, perform some reset actions,
     * for example:
     *  - set climate params info to "?? / 5"
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
     *  - compute new averages
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
                String windAverage;
                String humidityAverage;
                String pressureAverage;
                String temperatureAverage;
                String rainfallAverage;
                String galtAverage;
                String gmassAverage;

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
                    windAverage = averageBetween(params.getWind()[1], windItem, params.getTot_measure());
                    humidityAverage = averageBetween(params.getHumidity()[1], humidityItem, params.getTot_measure());
                    pressureAverage = averageBetween(params.getPressure()[1], pressureItem, params.getTot_measure());
                    temperatureAverage = averageBetween(params.getTemperature()[1], temperatureItem, params.getTot_measure());
                    rainfallAverage = averageBetween(params.getRainfall()[1], rainfallItem, params.getTot_measure());
                    galtAverage = averageBetween(params.getGlacier_alt()[1], galtItem, params.getTot_measure());
                    gmassAverage = averageBetween(params.getGlacier_mass()[1], gmassItem, params.getTot_measure());
                } else {
                    /* this is the case in which the climate params object for the selected location does not exist. In this case,
                    * create it */
                    params = new ClimateParams();
                    params.setTot_measure(1);
                    params.setGeonameID(location.getGeonameID());
                    params.setState(location.getState());
                    params.setAscii_name(location.getAsciiName());
                    windAverage = String.format("%d", windItem);
                    humidityAverage = String.format("%d", humidityItem);
                    pressureAverage = String.format("%d", pressureItem);
                    temperatureAverage = String.format("%d", temperatureItem);
                    rainfallAverage = String.format("%d", rainfallItem);
                    galtAverage = String.format("%d", galtItem);
                    gmassAverage = String.format("%d", gmassItem);
                    utils.getGeoData().addClimateParams(params);
                }

                /* overwrite the object */
                String[] newWind = {String.format("%d", windItem), windAverage };
                params.setWind(newWind);
                String[] newHumidity = {String.format("%d", humidityItem), humidityAverage };
                params.setHumidity(newHumidity);
                String[] newPress = {String.format("%d", pressureItem), pressureAverage };
                params.setPressure(newPress);
                String[] newTemp = {String.format("%d", temperatureItem), temperatureAverage };
                params.setTemperature(newTemp);
                String[] newRain = {String.format("%d", rainfallItem), rainfallAverage };
                params.setRainfall(newRain);
                String[] newGalt = {String.format("%d", galtItem), galtAverage };
                params.setGlacier_alt(newGalt);
                String[] newGmass = {String.format("%d", gmassItem), gmassAverage };
                params.setGlacier_mass(newGmass);
                /* set also new notes */
                params.setNotes(NotesTextArea.getText());

                utils.getGeoData().updateClimateParamsFile();

                utils.switchPage("Main Page");
                PlaceNameLbl.setText("");
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
