package bql.string;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.ArrayList;
import java.util.Collection;

public class EndsWith extends AbstractBQLOperator {

    private String fieldName;
    private String suffix;

    public EndsWith(String fieldName, String suffix) {
        this.fieldName = fieldName;
        this.suffix = suffix;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new ArrayList<>();
        for (Record r : in) {
            if (r.containsKey(fieldName) && r.get(fieldName).toString().endsWith(suffix))
                out.add(r);
        }
        return out;
    }

}
