package uni.climatemonitor.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.Dialog.ModalityType;
import uni.climatemonitor.graphics.AboutDialog;

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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(MainWindow);
        setVisible(true);

        /* perform a search action when clicking the search button */
        searchBtn_at_click();
        /* remove the text when selecting the search button */
        placeTextField_at_selection();
        /* open the about popup clicking on the About button */
        aboutBtn_at_click();
    }

    /**************************************************************
    *
    *                       CALLBACKS
    *
     **************************************************************/

    /**
     * Callback for About button push. Open the dialog with some info
     * about the authors and the project.
     *
     */
    private void aboutBtn_at_click(){
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog about = new AboutDialog();
                about.setModalityType(ModalityType.APPLICATION_MODAL);
                about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                about.setVisible(true);
            }
        });
    }

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

    /**
     * This is the callback for a selection of the text edit field. It
     * removes the "Type a place..." text.
     *
     */
    private void placeTextField_at_selection(){
        typeAPlaceTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                typeAPlaceTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) { }
        });
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

}
