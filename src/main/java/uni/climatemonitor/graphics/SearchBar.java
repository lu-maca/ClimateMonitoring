package uni.climatemonitor.graphics;
import javax.swing.*;
import java.awt.*;

public class SearchBar extends JPanel{
    private JTextField searchField;
    private JButton searchButton;

    public SearchBar() {
        super();
        setLayout(new FlowLayout());

        /* Initialize button and text field */
        searchField = new JTextField("Search area...");
        searchField.setPreferredSize(new Dimension(200, 30));
        searchButton = new JButton("Search");


        /* add button and text field to the panel */
        add(searchField);
        add(searchButton);

    }

    public static void main(String[] args) {
        SearchBar sb = new SearchBar();
    }
}
