package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.IBQLOperator;

import java.util.Collection;
import java.util.HashSet;

/**
 * BQL Relational == operator
 */
public class Equal extends AbstractBQLOperator {

    private String fieldName;
    private Object fieldValue;

    public Equal(String fieldName, Object fieldValue)
    {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new HashSet<>();
        for(Record r : in)
        {
            if(r.containsKey(fieldName) && r.get(fieldName).equals(fieldValue))
                out.add(r);
        }
        return out;
    }
}
