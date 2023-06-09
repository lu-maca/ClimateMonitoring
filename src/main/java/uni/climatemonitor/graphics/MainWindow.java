/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;

import org.json.simple.parser.ParseException;
import uni.climatemonitor.generics.Constants;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame {

    /* GUI elements */
    private JPanel MainWindow;
    private JPanel MainMExC;
    private DetailsPage DetailsPnl;
    private JPanel DetailsParentPnl;
    private JPanel MainParentPnl;
    private JPanel MainPnl;

    /* utils */
    private UtilsSingleton u;


    /**
     * Constructor for the MainWindow object.
     */
    public MainWindow() throws ParseException, IOException {
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
        u.setMExCInfo( MainMExC, DetailsPnl);

        /* set visibility */
        setContentPane(MainWindow);
        setVisible(true);
    }


    /**
     * main method
     *
     */
    public static void main(String[] args) throws ParseException, IOException {
        uni.climatemonitor.graphics.MainWindow mainWindow = new MainWindow();
    }

}
