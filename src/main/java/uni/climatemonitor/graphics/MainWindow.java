package uni.climatemonitor.graphics;

import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel MainPnl;
    private JTextField typeAPlaceTextField;
    private JLabel titleLbl;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel MainPanel;

    public MainWindow() {
        setTitle("Climate Monitor");
        setSize(400,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(MainPnl);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

}
