package bql;

import blockchain.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public abstract class AbstractBQLOperator implements IBQLOperator {

    private AbstractBQLOperator parent = null;
    private List<AbstractBQLOperator> children = new ArrayList<>();

    public AbstractBQLOperator addChild(AbstractBQLOperator op)
    {
        AbstractBQLOperator prevParent = op.parent;
        if(prevParent != null)
            prevParent.children.remove(op);
        op.parent = this;
        this.children.add(op);
        return this;
    }

    public AbstractBQLOperator getChild(int index)
    {
        return (index < children.size() && index >= 0) ? children.get(index) : null;
    }

    public List<AbstractBQLOperator> getChildren()
    {
        return children;
    }

    public AbstractBQLOperator getParent()
    {
        return parent;
    }

    @Override
    public abstract Collection<Record> apply(Collection<Record> in);

}