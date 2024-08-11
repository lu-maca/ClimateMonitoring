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
        setPreferredSize(new Dimension(1200,650));
        setMinimumSize(new Dimension(1200,650));
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
        u.setMExCInfo( MainMExC, DetailsPnl, MainPnl);

        /* set visibility */
        setContentPane(MainWindow);
        setVisible(true);
    }


    /**
     * main method
     *
     */
    public static void main(String[] args) throws IOException, NotBoundException {
        new MainWindow();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
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

}
