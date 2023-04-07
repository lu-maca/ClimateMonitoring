package uni.climatemonitor.graphics;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JPanel AboutPnl;

    public AboutDialog() {
        setContentPane(contentPane);
        setSize(600,200);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("About the authors");
    }

}
