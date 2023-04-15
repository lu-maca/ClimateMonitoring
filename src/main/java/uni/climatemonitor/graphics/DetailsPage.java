package uni.climatemonitor.graphics;

import uni.climatemonitor.data.ClimateParams;
import uni.climatemonitor.data.Location;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    /* location infos */
    private Location location;
    private ClimateParams params;

    public DetailsPage(){
        /*
            Callbacks for the detailed location page
         */
        /* close the detailed location page */

        closeBtn_at_selection();
    }

    /*

        UTILS

     */
    public void setUIPnl(Location loc, ClimateParams par){
        location = loc;
        params = par;
        PlaceNameLbl.setText(location.toString());

        if (par == null){ return; }
        AverageTitleLbl.setText("Average (on a total of " + par.getTot_measure() + " detections)");

        /* set wind */
        setLblValues(WindMostRecentValueLbl, par.getWind()[0], WindAverageValueLbl, par.getWind()[1]);
        /* set humidity */
        setLblValues(HumidityMostRecentValueLbl, par.getHumidity()[0], HumidityAverageValueLbl, par.getHumidity()[1]);
        /* set pressure */
        setLblValues(PressureMostRecentValueLbl, par.getPressure()[0], PressureAverageValueLbl, par.getPressure()[1]);
        /* set temperature */
        setLblValues(TemperatureMostRecentValueLbl, par.getTemperature()[0], TemperatureAverageValueLbl, par.getTemperature()[1]);
        /* set rainfall */
        setLblValues(RainfallMostRecentValueLbl, par.getRainfall()[0], RainfallAverageValueLbl, par.getRainfall()[1]);
        /* set glaciers alt */
        setLblValues(GAltMostRecentValueLbl, par.getGlacier_alt()[0], GAltAverageValueLbl, par.getGlacier_alt()[1]);
        /* set glaciers mass */
        setLblValues(GMassMostRecentValueLbl, par.getGlacier_mass()[0], GMassAverageValueLbl, par.getGlacier_mass()[1]);
    }

    private void setLblValues(JLabel current, String currentValue, JLabel average, String averageValue){
        current.setText(currentValue + " / 5");
        average.setText(averageValue + " / 5");
    }


    /*

    CALLBACKS

     */

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
     * main method
     *
     */
    public static void main(String[] args) {
        DetailsPage detailsPage = new DetailsPage();
    }
}
