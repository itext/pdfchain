package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.Collection;

/**
 * Created by Joris Schellekens on 7/19/2017.
 */
public class Star extends AbstractBQLOperator {

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        return in;
    }

}
