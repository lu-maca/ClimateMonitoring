package uni.climatemonitor;
import org.json.simple.parser.ParseException;
import uni.climatemonitor.graphics.MainWindow;
import uni.climatemonitor.graphics.UtilsSingleton;

import java.io.IOException;

public class Main {
    public Main() throws ParseException, IOException {
        MainWindow frame = new MainWindow();
    }

    public static void main(String[] args) throws ParseException, IOException {
        Main program = new Main();


    }
}
