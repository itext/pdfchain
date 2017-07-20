package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.IBQLOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * BQL Relational != operator
 */
public class NotEqual extends AbstractBQLOperator {

    private String fieldName;
    private Object fieldValue;

    public NotEqual(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new ArrayList<>();
        for (Record r : in) {
            if (r.containsKey(fieldName) && !r.get(fieldName).equals(fieldValue))
                out.add(r);
        }
        return out;
    }

}
