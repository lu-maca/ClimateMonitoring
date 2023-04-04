package uni.climatemonitor.graphics;
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
        GridBagConstraints grid = new GridBagConstraints();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* add a search bar to the main window */
        searchBut = new SearchBar();
        searchBut.setVisible(true);
        add(searchBut);

        /* add a login/register button to the main window */
        loginBut = new JButton("Login/register");
        loginBut.setVisible(true);
        grid.anchor = GridBagConstraints.NORTHEAST;
        add(loginBut, grid);


        /* set main window visibility */
        setVisible(true);
    }

}
