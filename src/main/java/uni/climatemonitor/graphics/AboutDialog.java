package uni.climatemonitor.graphics;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JPanel AboutPnl;
    private JLabel logoEarthLbl;

    public AboutDialog() {
        setContentPane(contentPane);
        setSize(610,225);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("About");

        ImageIcon iconLogo = new ImageIcon(Constants.EARTH_LOGO_PATH_S);
        logoEarthLbl.setIcon(iconLogo);
    }

}
