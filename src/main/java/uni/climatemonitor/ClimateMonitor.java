/*************************************************
 * ClimateMonitor class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor;
import uni.climatemonitor.graphics.MainWindow;

import java.io.IOException;

public class ClimateMonitor {
    public ClimateMonitor()  {
        try {
            MainWindow frame = new MainWindow();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ClimateMonitor program = new ClimateMonitor();
    }
}
