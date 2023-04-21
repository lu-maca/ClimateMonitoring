/*************************************************
 * ClimateMonitor class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor;
import org.json.simple.parser.ParseException;
import uni.climatemonitor.graphics.MainWindow;

import java.io.IOException;

public class ClimateMonitor {
    public ClimateMonitor()  {
        try {
            MainWindow frame = new MainWindow();
        } catch (Exception e){
            //
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        ClimateMonitor program = new ClimateMonitor();


    }
}
