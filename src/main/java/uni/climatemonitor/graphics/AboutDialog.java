/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */

package uni.climatemonitor.graphics;
import uni.climatemonitor.generics.Constants;
import javax.swing.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JPanel AboutPnl;
    private JLabel logoEarthLbl;

    public AboutDialog() {
        setContentPane(contentPane);
        setSize(610,170);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("About");

        ImageIcon iconLogo = new ImageIcon(Constants.EARTH_LOGO_PATH_S);
        logoEarthLbl.setIcon(iconLogo);
    }

}
