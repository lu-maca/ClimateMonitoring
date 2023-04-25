/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;
import uni.climatemonitor.generics.Constants;
import javax.swing.*;
import java.net.URL;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JPanel AboutPnl;
    private JLabel logoEarthLbl;

    public AboutDialog() {
        setContentPane(contentPane);
        setSize(610,200);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("About");

        URL imgURL = getClass().getResource(Constants.EARTH_LOGO_PATH_S);
        ImageIcon image = new ImageIcon(imgURL);
        logoEarthLbl.setIcon(image);
    }

}
