package uni.climatemonitor.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public abstract class FileHandler {
    private final String fileName;
    private String fileContent = null;

    public FileHandler(String fileName){
        this.fileName = fileName;
    }
}
