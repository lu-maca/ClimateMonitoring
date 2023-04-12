package uni.climatemonitor.graphics;

import uni.climatemonitor.data.Location;

import javax.swing.*;

public class DetailPage {
    private Location location;
    JLabel placeName;

    public DetailPage(JLabel detailNameLbl, Location location){
        this.location = location;
        placeName = detailNameLbl;
        setPlaceName(location.getAsciiName());


    }

    public void setPlaceName(String newName){
        placeName.setText(newName);
    }

}
