package uni.climatemonitor.data;

        import org.json.simple.parser.JSONParser;
        import org.json.simple.parser.ParseException;

        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Path;
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
        Path path = Path.of(fileName);
        String text = Files.readString(path);

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(text);

        /* for all elements of the list */
        List ll = (List) obj;
        for (int i = 0; i< ll.size(); i++){
            operators.add(new Operator((HashMap) ll.get(i)));
        }
    }
}
