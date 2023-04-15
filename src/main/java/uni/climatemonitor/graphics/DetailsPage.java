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
        PlaceNameLbl.setText(location.getAsciiName());


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
