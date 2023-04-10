package uni.climatemonitor.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Generic class for data files handling
 */
public abstract class FileHandler {
    protected final String fileName;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    public abstract void readFile();
}
