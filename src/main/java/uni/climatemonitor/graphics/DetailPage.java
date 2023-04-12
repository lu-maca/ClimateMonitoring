package uni.climatemonitor.graphics;

import javax.swing.*;

public class DetailPage {
    private JLabel placeName;
    public DetailPage(JLabel detailNameLbl, String locationName){
        placeName = detailNameLbl;
        placeName.setText(locationName);
    }

    public void setPLaceName(String newName){
        placeName.setText(newName);
    }
}
