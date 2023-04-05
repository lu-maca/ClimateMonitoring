
package uni.climatemonitor.graphics;

import uni.climatemonitor.graphics.SearchBar;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static SearchBar searchBut;
    private static JButton loginBut;

    public MainWindow() {
        /* Set main window properties */
        super("Climate Monitor");
        setSize(600,600);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* add a panel to the main window to organize it */
        JPanel mainPnl = new JPanel(new java.awt.GridBagLayout());
        GridBagConstraints gridConstr = new GridBagConstraints();

        gridConstr.fill = GridBagConstraints.NONE;
        gridConstr.insets = new Insets(3, 3, 3, 3);


        /* add a search bar to the main window */
        gridConstr.gridx = 2;
        gridConstr.gridy = 2;
        searchBut = new SearchBar();
        searchBut.setVisible(true);
        mainPnl.add(searchBut, gridConstr);

        /* add a login/register button to the main window */
        loginBut = new JButton("Login/register");
        loginBut.setVisible(true);
        gridConstr.gridx = 50;
        gridConstr.gridy = 0;
        mainPnl.add(loginBut, gridConstr);

        /* add main panel to the window */
        add(mainPnl);
        /* set main window visibility */
        setVisible(true);
    }

}

