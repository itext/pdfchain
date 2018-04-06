package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * BQL Relational &lt; operator
 */
public class Smaller extends AbstractBQLOperator {

    private String fieldName;
    private Object fieldValue;

    public Smaller(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new ArrayList<>();
        for (Record r : in) {
            if (r.containsKey(fieldName)) {
                Object val = r.get(fieldName);
                if (val instanceof Number) {
                    Number valN = (Number) val;
                    Number fldN = (Number) fieldValue;
                    if (cmpNumbers(valN, fldN) < 0)
                        out.add(r);
                }
            }
        }
        return out;
    }

    private int cmpNumbers(Number n0, Number n1) {
        return Double.compare(n0.doubleValue(), n1.doubleValue());
    }
}
