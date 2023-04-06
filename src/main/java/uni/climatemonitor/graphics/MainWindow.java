package uni.climatemonitor.graphics;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel MainWindow;
    private JPanel MainPnl;
    private JButton LoginBtn;
    private JButton RegisterBtn;
    private JPanel UserPnl;
    private JPanel AboutPnl;
    private JButton aboutButton;
    private JTextField typeAPlaceTextField;
    private JButton searchBtn;
    private JPanel SearchPnl;

    public MainWindow() {
        setTitle("Climate Monitor");
        setSize(1000,600);
        setMinimumSize(new Dimension(1000,600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(MainWindow);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

}
