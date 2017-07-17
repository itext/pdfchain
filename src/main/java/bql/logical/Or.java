package bql.logical;

import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.IBQLOperator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * BQL Logical OR operator
 */
public class Or extends AbstractBQLOperator{

    public Or(AbstractBQLOperator left, AbstractBQLOperator right)
    {
        addChild(left);
        addChild(right);
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        IBQLOperator left = getChild(0);
        IBQLOperator right = getChild(1);
        Set<Record> out = new HashSet<>(left.apply(in));
        out.addAll(right.apply(in));
        return out;
    }

}
