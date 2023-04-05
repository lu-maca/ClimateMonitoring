package uni.climatemonitor;

import uni.climatemonitor.graphics.MainWindow;

public class Main {
    private static MainWindow frame;
    private static Main program;

    public Main() {
        frame = new MainWindow();
    }

    public static void main(String[] args) {
        program = new Main();
    }
}
