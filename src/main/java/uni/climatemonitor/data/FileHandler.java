/*************************************************
 * FileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import org.json.simple.parser.ParseException;

import java.io.*;


/**
 * Generic class for data files handling
 */
public abstract class FileHandler {
    protected final String fileName;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    public abstract void readFile() throws IOException, ParseException;

    public abstract void writeFile();

    protected String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
