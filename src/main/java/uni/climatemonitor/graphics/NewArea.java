package uni.climatemonitor.graphics;

import uni.climatemonitor.common.Coordinates;
import uni.climatemonitor.common.Location;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

public class NewArea extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField NameTextField;
    private JTextField StateTextField;
    private JLabel NameLbl;
    private JLabel StateLbl;
    private JLabel LatLbl;
    private JTextField LatTextField;
    private JLabel LongLbl;
    private JTextField LongTextField;
    private JComboBox LatComboBox;
    private JComboBox LongComboBox;


    public NewArea() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Add new area");
        setSize(610,200);
        setResizable(false);
        setLocationRelativeTo(null);
        URL imgURL = getClass().getResource(Constants.EARTH_LOGO_PATH_S);
        ImageIcon image = new ImageIcon(imgURL);
        setIconImage(image.getImage());

        /* click ok */
        ok_at_click();

        /* cancel click */
        cancel_at_click();

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setVisible(true);
    }

    /**
     * Check if a location with given properties already exists
     * @param name
     * @param state
     * @param coordinates
     * @return true if exists with same name, same state, same coordinates; false otherwise
     */
    private boolean isAlreadyExisting(String name, String state, String coordinates){
        UtilsSingleton utils = UtilsSingleton.getInstance();
        GeoData geoData = utils.getGeoData();
        ArrayList<Location> locations = geoData.getGeoLocationsList();
        String locName;
        String locState;
        String locCoords;
        for (Location l : locations){
            locName = l.getAsciiName();
            locCoords = l.getCoordinates().toString();
            locState = l.getState();

            if (locName.equals(name) && locState.equals(state) && locCoords.equals(coordinates)){
                return true;
            }
        }
        return false;
    }

    private boolean areCoordsValid(){
        boolean out;
        try {
            out = ! (
                LatTextField.getText().isEmpty() ||
                LongTextField.getText().isEmpty() ||
                Double.parseDouble(LatTextField.getText()) > 90.0 ||
                Double.parseDouble(LongTextField.getText()) > 180.0
            );
        }
        catch (Exception err){
            return false;
        }
        return out;
    }
    /**
     * Callback for ok button:
     * - check if the new place already exists
     * - if so, raise an error
     * - save it otherwise
     * - close the dialog
     */
    private void ok_at_click() {
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (NameTextField.getText().isEmpty() || StateTextField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(new JFrame(), "Invalid name or country code.", "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (! areCoordsValid() ){
                    JOptionPane.showMessageDialog(new JFrame(), "Latitude or longitude are not valid.", "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                /* check the place existance */
                double latSign = LatComboBox.getSelectedIndex() == 0 ? 1.0 : -1.0;
                double longSign = LongComboBox.getSelectedIndex() == 0 ? 1.0 : -1.0;
                double lat, lon;
                lat = Double.parseDouble(LatTextField.getText()) * latSign;
                lon = Double.parseDouble(LongTextField.getText()) * longSign;

                Coordinates c = new Coordinates(lat, lon);
                boolean isExisting = isAlreadyExisting(NameTextField.getText(), StateTextField.getText(), c.toString());

                if (isExisting){
                    JOptionPane.showMessageDialog(new JFrame(), "This area is already existing!", "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                /* save on csv and on place array */
                UtilsSingleton utils = UtilsSingleton.getInstance();

                /* for the geoname id, a unique representation shall be chosen: it can be directly a string formed by appending
                * the coordinates of the place, since coordinates are unique; in order to be able to parse the string
                * in the json files, replace all the dashes with zeros (to avoid equal coordinates from - removal), and dots */
                String geoID = c.toRawString().
                                replaceAll("-","0").
                                replaceAll("\\.", "").
                                replaceAll(",", "").
                                replaceAll(" ", "");

                String[] arr = {geoID, NameTextField.getText(), NameTextField.getText(), StateTextField.getText(), StateTextField.getText(), c.toRawString()};
                Location newLoc = new Location(arr);
                utils.getGeoData().addLocation(newLoc);

                utils.getGeoData().updateLocationsFile();
                /* close the dialog */
                dispose();
            }
        });

    }

    /**
     * Callback for cancel button
     */
    private void cancel_at_click() {
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    public static void main(String[] args) {
        NewArea dialog = new NewArea();
    }

}
