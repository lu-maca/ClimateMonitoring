package uni.climatemonitor;

import uni.climatemonitor.graphics.MainWindow;

public class Main {
    private static Main app;

    public Main(){
        MainWindow app = new MainWindow();
    }
    public static void main(String[] args) {
        app = new Main();
    }
}
