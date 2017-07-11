package bql.executor;

import blockchain.IBlockChain;
import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.IBQLOperator;
import bql.logical.And;
import bql.relational.EqualID;
import bql.sort.SortBy;
import bql.transform.Select;

import java.util.*;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class Executor {

    private IBlockChain blockchain;

    public Executor(IBlockChain blockchain)
    {
        this.blockchain = blockchain;
    }

    public Collection<Record> execute(AbstractBQLOperator op)
    {
        Object id = useID(op);
        Collection<Record> db = (id == null) ? blockchain.all() : blockchain.get(id.toString());
        return op.apply(db);
    }

    private List<AbstractBQLOperator> leaves(AbstractBQLOperator root)
    {
        List<AbstractBQLOperator> out = new ArrayList<>();
        Stack<AbstractBQLOperator> operatorStack = new Stack();
        operatorStack.push(root);
        while(!operatorStack.isEmpty())
        {
            AbstractBQLOperator op = operatorStack.pop();
            if(op.getChildren().isEmpty())
                out.add(op);
            else{
                for(AbstractBQLOperator c : op.getChildren())
                    operatorStack.push(c);
            }
        }
        return out;
    }

    private Object useID(AbstractBQLOperator root)
    {
        for(AbstractBQLOperator leaf : leaves(root))
        {
            AbstractBQLOperator tmp = leaf;
            if(!(tmp instanceof EqualID))
                continue;
            boolean pathUp = true;
            while(tmp.getParent() != null)
            {
                AbstractBQLOperator parent = tmp.getParent();
                if(!enforcesID(parent))
                {
                    pathUp = false;
                    break;
                }
                tmp = parent;
            }
            if(pathUp)
                return ((EqualID) leaf).getSelectedValue();
        }
        return null;
    }

    private boolean enforcesID(AbstractBQLOperator operator){
        return (operator instanceof And) ||
                (operator instanceof Select) ||
                (operator instanceof SortBy);
    }
}
