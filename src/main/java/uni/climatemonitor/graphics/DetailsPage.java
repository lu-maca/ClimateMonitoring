/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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
    private JComboBox DateComboBox;
    private JPanel ChooseDatePnl;


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
        /* at date selection change */
        dateComboBox_at_item_change();

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

        String averageStr = String.format("%.2f", average);
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

    public void setUIPnl(Location loc){
        location = loc;
        UtilsSingleton utils = UtilsSingleton.getInstance();
        params = utils.getGeoData().getClimateParamsFor(location.getGeonameID());
        PlaceNameLbl.setText(location.toString());

        DateComboBox.setPreferredSize(new Dimension(185, 24));
        if (!isOperatorEnabledForThisPlace() && params != null) {
            DateComboBox.setModel(new DefaultComboBoxModel<String>(params.getBeautifulDate().toArray(new String[0])));
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("---");
            DateComboBox.setModel(model);
            DateComboBox.setEnabled(false);
        }

        /* if an operator is logged in, set the combo box for detections and remove current values */
        if (isOperatorEnabledForThisPlace()) {
            setOperatorsView();
            MostRecentTitleLbl.setText("Set new record");
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
        if (params == null){ return; }

        /* if history on climate params exists, set it */
        setParamsFromHistory(0);

    }

    private void setLblValues(JLabel current, String currentValue, JLabel average, String averageValue){
//        if (UtilsSingleton.getInstance().getWhoisLoggedIn() == null) {
        current.setText(currentValue + " / 5");
//        }
        average.setText(averageValue + " / 5");
    }

    private void setParamsFromHistory(int idx){
        AverageTitleLbl.setText("Average (on a total of " + params.getTot_measure() + " records)");

        /* set wind */
        setLblValues(WindMostRecentValueLbl, params.getWind().get(idx), WindAverageValueLbl, computeAverage(params.getWind()));
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, params.getHumidity().get(idx), HumidityAverageValueLbl, computeAverage(params.getHumidity()));
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, params.getPressure().get(idx), PressureAverageValueLbl, computeAverage(params.getPressure()));
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, params.getTemperature().get(idx), TemperatureAverageValueLbl, computeAverage(params.getTemperature()));
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, params.getRainfall().get(idx), RainfallAverageValueLbl, computeAverage(params.getRainfall()));
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, params.getGlacier_alt().get(idx), GAltAverageValueLbl, computeAverage(params.getGlacier_alt()));
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, params.getGlacier_mass().get(idx), GMassAverageValueLbl, computeAverage(params.getGlacier_mass()));
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

                /* set today */
                LocalDateTime ld = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
                String dateString = "\"" + ld.format(formatter) + "\"";

                params.getDate().add(0, dateString);
                /* set also new notes */
                params.setNotes(NotesTextArea.getText());

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
