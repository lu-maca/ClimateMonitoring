package uni.climatemonitor.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        /* perform a search action when clicking the search button */
        searchBtn_at_click();

    }

    /**************************************************************
    *
    *                       CALLBACKS
    *
     **************************************************************/

    /**
     * This is the callback of a push event on the Search button
     * @todo: add the callback to the search function
     */
    private void searchBtn_at_click(){
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* perform here some action */
            }
        });
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

}
