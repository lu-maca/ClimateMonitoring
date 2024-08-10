package uni.climatemonitor.graphics;

import uni.climatemonitor.common.Coordinates;
import uni.climatemonitor.common.Location;
import uni.climatemonitor.generics.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.rmi.RemoteException;
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

                /* save on csv and on place array */
                UtilsSingleton utils = UtilsSingleton.getInstance();

                /* don't need to compute the id, it will be computed by the server itself */
                String geoID = "0";

                Location newLoc = new Location(geoID, NameTextField.getText(), NameTextField.getText(), StateTextField.getText(), lat, lon);

                try {
                    boolean rc = utils.getDbService().pushLocation(newLoc);
                    if (!rc){
                        JOptionPane.showMessageDialog(new JFrame(), "This area is already existing!", "",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Area correctly created.", "",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    /* close the dialog */
                    dispose();
                    throw new RuntimeException(ex);
                }

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
