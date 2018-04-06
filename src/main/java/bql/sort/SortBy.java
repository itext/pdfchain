package bql.sort;

import blockchain.Record;
import bql.AbstractBQLOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Sort operator for BQL
 */
public class SortBy extends AbstractBQLOperator {

    private String fieldName;
    private boolean ascending;

    public SortBy(AbstractBQLOperator source, String fieldName, boolean ascending) {
        this.addChild(source);
        this.fieldName = fieldName;
        this.ascending = ascending;
    }

    public SortBy(AbstractBQLOperator source, String fieldName) {
        this.addChild(source);
        this.fieldName = fieldName;
        this.ascending = true;
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        List<Record> out = new ArrayList<>(getChild(0).apply(in));

        java.util.Collections.sort(out, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                Object val0 = o1.get(fieldName);
                Object val1 = o2.get(fieldName);

                if (val0 == null && val1 == null)
                    return 0;
                if (val0 == null || val1 == null)
                    return val0 == null ? -1 : 1;

                Comparable cmp0 = (Comparable) val0;
                Comparable cmp1 = (Comparable) val1;

                return (ascending ? 1 : -1) * cmp0.compareTo(cmp1);
            }
        });

        return out;
    }

}
