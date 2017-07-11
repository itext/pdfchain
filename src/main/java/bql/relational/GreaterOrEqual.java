package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.IBQLOperator;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class GreaterOrEqual extends AbstractBQLOperator {

    private String fieldName;
    private Object fieldValue;

    public GreaterOrEqual(String fieldName, Object fieldValue)
    {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new HashSet<>();
        for(Record r : in)
        {
            if(r.containsKey(fieldName))
            {
                Object val = r.get(fieldName);
                if(val instanceof Number)
                {
                    Number valN = (Number) val;
                    Number fldN = (Number) fieldValue;
                    if(cmpNumbers(valN, fldN) >= 0)
                        out.add(r);
                }
            }
        }
        return out;
    }

    private int cmpNumbers(Number n0, Number n1)
    {
        return ((Double) n0.doubleValue()).compareTo(n1.doubleValue());
    }
}
