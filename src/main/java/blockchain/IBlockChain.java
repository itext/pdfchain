package blockchain;

import java.util.List;

/**
 * blockchain implementing basic blockchain functionality
 */
public interface IBlockChain {

    /**
     * Put data on the blockchain
     *
     * @param key  the key being used to put the data on the blockchain
     * @param data the data being put on the blockchain
     */
    public boolean put(String key, Record data);

    /**
     * Get data from the blockchain
     *
     * @param key the key being queried
     * @return
     */
    public List<Record> get(String key);

    /**
     * Get all data from the blockchain
     *
     * @return
     */
    public List<Record> all();
}
