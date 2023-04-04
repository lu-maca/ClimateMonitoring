
package uni.climatemonitor.graphics;

import uni.climatemonitor.graphics.SearchBar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow {
    private JFrame mainFrame;
    private static MainWindow mainWindow;
    private SearchBar searchB;

    public MainWindow(){
        prepareGUI();
    }

    public static void main(String[] args){
        mainWindow = new MainWindow();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Climate Monitor");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.setVisible(true);

        /* In order to close the program when the window is closed */
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }
}

