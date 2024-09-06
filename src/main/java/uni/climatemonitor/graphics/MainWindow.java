/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import uni.climatemonitor.Client;
import uni.climatemonitor.common.IDatabaseService;
import uni.climatemonitor.generics.Constants;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainWindow extends JFrame {

    /* GUI elements */
    private JPanel MainWindow;
    private JPanel MainMExC;
    private DetailsPage DetailsPnl;
    private JPanel DetailsParentPnl;
    private JPanel MainParentPnl;
    private MainPage MainPnl;

    /* utils */
    private UtilsSingleton u;


    /**
     * Constructor for the MainWindow object.
     */
    public MainWindow() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost");
        IDatabaseService stub = (IDatabaseService) registry.lookup("dbService");

        if (stub == null) {
            System.out.println("Stub not found!");
            return;
        }
        UtilsSingleton.getInstance().setDbService(stub);

        setTitle(Constants.APP_NAME_S);
        setPreferredSize(new Dimension(1200, 650));
        setMinimumSize(new Dimension(1200, 650));
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /* set the logo */
        URL imgURL = getClass().getResource(Constants.EARTH_LOGO_PATH_S);
        ImageIcon image = new ImageIcon(imgURL);
        setIconImage(image.getImage());

        /* set mutually exclusive panel */
        MainMExC.add(MainParentPnl, "Main Page");
        MainMExC.add(DetailsParentPnl, "Location Details Page");

        /* utils */
        u = UtilsSingleton.getInstance();
        u.setMExCInfo(MainMExC, DetailsPnl, MainPnl);

        /* set visibility */
        setContentPane(MainWindow);
        setVisible(true);
    }


    /**
     * main method
     */
    public static void main(String[] args) throws IOException, NotBoundException {
        new MainWindow();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                UtilsSingleton utils = UtilsSingleton.getInstance();
                try {
                    Client client = utils.getClient();
                    if (client != null) {
                        utils.getDbService().unregisterClientForLocation(client);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
        MainWindow = new JPanel();
        MainWindow.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(2, 2, 2, 2), -1, -1));
        MainWindow.setMaximumSize(new Dimension(1000, 600));
        MainWindow.setMinimumSize(new Dimension(800, 400));
        MainWindow.setPreferredSize(new Dimension(1000, 600));
        MainWindow.setRequestFocusEnabled(true);
        MainMExC = new JPanel();
        MainMExC.setLayout(new CardLayout(0, 0));
        MainWindow.add(MainMExC, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DetailsParentPnl = new JPanel();
        DetailsParentPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        MainMExC.add(DetailsParentPnl, "Location Details Page");
        DetailsPnl = new DetailsPage();
        DetailsParentPnl.add(DetailsPnl.$$$getRootComponent$$$(), new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        MainParentPnl = new JPanel();
        MainParentPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        MainMExC.add(MainParentPnl, "Main Page");
        MainPnl = new MainPage();
        MainParentPnl.add(MainPnl.$$$getRootComponent$$$(), new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainWindow;
    }
}
