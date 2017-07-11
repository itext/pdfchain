package bql.relational;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class EqualID extends AbstractBQLOperator{

    private String fieldName;
    private Object fieldValue;

    public EqualID(Object fieldValue)
    {
        this.fieldName = "id1";
        this.fieldValue = fieldValue;
    }

    public Object getSelectedValue()
    {
        return fieldValue;
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
