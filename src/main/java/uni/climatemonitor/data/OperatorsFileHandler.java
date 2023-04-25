/*************************************************
 * OperatorsFileHandler class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OperatorsFileHandler extends FileHandler{
    private ArrayList<Operator> operators = new ArrayList<>();

    public OperatorsFileHandler(String fileName){
        super(fileName);
    }

    public ArrayList<Operator> getOperators() {
        return operators;
    }

    @Override
    public void readFile() throws ParseException, IOException {
        InputStream input = getClass().getResourceAsStream(fileName);
        String text = readFromInputStream(input);
        input.close();
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(text);

        /* for all elements of the list */
        List ll = (List) obj;
        for (int i = 0; i< ll.size(); i++){
            operators.add(new Operator((HashMap) ll.get(i)));
        }
    }

    @Override
    public void writeFile(){
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("[\n");
            int size = operators.size();
            int i = 0;
            for (Operator op : operators){
                if (i == size){ break; }
                i++;
                myWriter.write(op.toJson());
                myWriter.write(",");
                myWriter.write("\n");
            }
            myWriter.write("\n]");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
