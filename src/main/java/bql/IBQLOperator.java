package bql;

import blockchain.Record;

import java.util.Collection;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public interface IBQLOperator {

    Collection<Record> apply(Collection<Record> in);

}
