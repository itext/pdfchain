package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * BQL Relational == operator (specifically for ID)
 * by keeping the == operator for ID separate, we are able to detect it in the abstract syntax tree.
 * Doing so enables us to sometimes optimize queries.
 */
public class EqualID extends AbstractBQLOperator {

    private String fieldName;
    private Object fieldValue;

    public EqualID(Object fieldValue) {
        this.fieldName = "id1";
        this.fieldValue = fieldValue;
    }

    public Object getSelectedValue() {
        return fieldValue;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new ArrayList<>();
        for (Record r : in) {
            if (r.containsKey(fieldName) && r.get(fieldName).equals(fieldValue))
                out.add(r);
        }
        return out;
    }
}
