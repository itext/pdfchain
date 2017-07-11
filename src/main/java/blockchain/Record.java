package blockchain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class Record extends HashMap<String, Object> {

    public Record(){}

    public Record(Map<String, Object> m)
    {
        this.putAll(m);
    }
}
