package uni.climatemonitor.graphics;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
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
        setSize(610, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        URL imgURL = getClass().getResource(Constants.EARTH_LOGO_PATH_S);
        ImageIcon image = new ImageIcon(imgURL);
        setIconImage(image.getImage());

        /* click ok */
        ok_at_click();

        /* cancel click */
        cancel_at_click();

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setVisible(true);
    }

    private boolean areCoordsValid() {
        boolean out;
        try {
            out = !(
                    LatTextField.getText().isEmpty() ||
                            LongTextField.getText().isEmpty() ||
                            Double.parseDouble(LatTextField.getText()) > 90.0 ||
                            Double.parseDouble(LongTextField.getText()) > 180.0
            );
        } catch (Exception err) {
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
                if (NameTextField.getText().isEmpty() || StateTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Invalid name or country code.", "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!areCoordsValid()) {
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
                    if (!rc) {
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
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setVisible(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setContentAreaFilled(false);
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setContentAreaFilled(false);
        buttonOK.setText("OK");
        panel2.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        NameLbl = new JLabel();
        NameLbl.setText("Name");
        CellConstraints cc = new CellConstraints();
        panel3.add(NameLbl, cc.xy(1, 1));
        NameTextField = new JTextField();
        panel3.add(NameTextField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        StateLbl = new JLabel();
        StateLbl.setText("Country Code");
        panel3.add(StateLbl, cc.xy(1, 3));
        StateTextField = new JTextField();
        panel3.add(StateTextField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        LatLbl = new JLabel();
        LatLbl.setText("Latitude");
        panel3.add(LatLbl, cc.xy(1, 5));
        LatTextField = new JTextField();
        panel3.add(LatTextField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        LongLbl = new JLabel();
        LongLbl.setText("Longitude");
        panel3.add(LongLbl, cc.xy(1, 7));
        LongTextField = new JTextField();
        panel3.add(LongTextField, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        LatComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("N");
        defaultComboBoxModel1.addElement("S");
        LatComboBox.setModel(defaultComboBoxModel1);
        panel3.add(LatComboBox, cc.xy(5, 5));
        LongComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("E");
        defaultComboBoxModel2.addElement("W");
        LongComboBox.setModel(defaultComboBoxModel2);
        panel3.add(LongComboBox, cc.xy(5, 7));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
