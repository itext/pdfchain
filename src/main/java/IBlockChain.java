import java.util.List;
import java.util.Map;

/**
 * interface implementing basic blockchain functionality
 */
public interface IBlockChain {

    /**
     * Put data on the blockchain
     *
     * @param key  the key being used to put the data on the blockchain
     * @param data the data being put on the blockchain
     */
    public boolean put(String key, Map<String, Object> data);

    /**
     * Get data from the blockchain
     *
     * @param key the key being queried
     * @return
     */
    public List<Map<String, Object>> get(String key);

}
