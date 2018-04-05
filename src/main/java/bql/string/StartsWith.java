package bql.string;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 */
public class StartsWith extends AbstractBQLOperator {

    private String fieldName;
    private String suffix;

    public StartsWith(String fieldName, String suffix) {
        this.fieldName = fieldName;
        this.suffix = suffix;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        Collection<Record> out = new ArrayList<>();
        for (Record r : in) {
            if (r.containsKey(fieldName) && r.get(fieldName).toString().startsWith(suffix))
                out.add(r);
        }
        return out;
    }
}
