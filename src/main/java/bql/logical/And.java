package bql.logical;

import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.IBQLOperator;

import java.util.Collection;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class And extends AbstractBQLOperator {

    public And(AbstractBQLOperator left, AbstractBQLOperator right)
    {
        addChild(left);
        addChild(right);
    }

    @Override
    public Collection<Record> apply(Collection<Record> in) {
        IBQLOperator left = getChild(0);
        IBQLOperator right = getChild(1);
        return left.apply(right.apply(in));
    }

}
