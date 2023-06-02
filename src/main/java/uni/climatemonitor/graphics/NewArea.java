package uni.climatemonitor.graphics;

import uni.climatemonitor.data.Coordinates;
import uni.climatemonitor.data.GeoData;
import uni.climatemonitor.data.Location;
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
                /* check the place existance */
                double latSign = LatComboBox.getSelectedIndex() == 0 ? 1.0 : -1.0;
                double longSign = LongComboBox.getSelectedIndex() == 0 ? 1.0 : -1.0;

                double lat = Double.parseDouble(LatTextField.getText()) * latSign;
                double lon = Double.parseDouble(LongTextField.getText()) * longSign;
                Coordinates c = new Coordinates(lat, lon);
                boolean isExisting = isAlreadyExisting(NameTextField.getText(), StateTextField.getText(), c.toString());

                if (isExisting){
                    JOptionPane.showMessageDialog(new JFrame(), "This area is already existing!", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                /* save on csv and on place array */

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
