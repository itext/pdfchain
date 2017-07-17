package bql;

import blockchain.Record;

import java.util.Collection;

/**
 * Interface representing a BQL operator
 */
public interface IBQLOperator {

    Collection<Record> apply(Collection<Record> in);

}
