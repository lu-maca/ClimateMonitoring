package uni.climatemonitor.graphics;

import uni.climatemonitor.generics.Constants;
import uni.climatemonitor.graphics.UtilsSingleton;
import uni.climatemonitor.graphics.DetailsPage;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    /* GUI elements */
    private JPanel MainWindow;
    private JPanel MainMExC;
    private DetailsPage DetailsPnl;
    private JPanel DetailsParentPnl;
    private JPanel MainParentPnl;
    private JPanel MainPnl;

    /* utils */
    private UtilsSingleton u = UtilsSingleton.getInstance(MainMExC, DetailsPnl);

    /**
     * Constructor for the MainWindow object.
     */
    public MainWindow() {
        setTitle(Constants.APP_NAME_S);
        setSize(1100,650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /* set the logo */
        ImageIcon earthIcon = new ImageIcon(Constants.EARTH_LOGO_PATH_S);
        setIconImage(earthIcon.getImage());

        /* set mutually exclusive panel */
        MainMExC.add(MainParentPnl, "Main Page");
        MainMExC.add(DetailsParentPnl, "Location Details Page");

        /* set visibility */
        setContentPane(MainWindow);
        setVisible(true);
    }


    /**
     * main method
     *
     */
    public static void main(String[] args) {
        uni.climatemonitor.graphics.MainWindow mainWindow = new MainWindow();
    }

}
