package uni.climatemonitor.graphics;

import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel MainPnl;
    private JTextField typeAPlaceTextField;
    private JPanel MainPanel;
    private JButton LoginBtn;
    private JButton RegisterBtn;
    private JPanel UserPnl;
    private JPanel AboutPnl;
    private JButton aboutButton;

    public MainWindow() {
        setTitle("Climate Monitor");
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(MainPnl);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

}
