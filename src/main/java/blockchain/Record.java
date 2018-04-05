package blockchain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Record extends HashMap<String, Object> {

    public Record() {
    }

    public Record(Map<String, Object> m) {
        this.putAll(m);
    }
}
