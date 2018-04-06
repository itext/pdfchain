package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.Collection;

public class Star extends AbstractBQLOperator {

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        return in;
    }

}
