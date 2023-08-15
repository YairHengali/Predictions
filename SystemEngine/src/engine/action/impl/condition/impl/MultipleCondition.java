package engine.action.impl.condition.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.impl.condition.LogicalOperator;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;
import engine.context.ContextImpl;
import engine.entity.EntityInstance;
import engine.entity.manager.EntityInstanceManager;

import java.util.ArrayList;
import java.util.List;

public class MultipleCondition extends ConditionImpl implements Condition {
    List<Condition> conditionList;
    LogicalOperator logicalOperator;

    public MultipleCondition(String mainEntityName, LogicalOperator logicalOperator){
        super(ActionType.CONDITION,mainEntityName);
        this.conditionList = new ArrayList<>();
        this.logicalOperator = logicalOperator;
    }

    @Override
    public void Run(Context context) throws Exception {
        if (evaluateCondition(context)) {
            invokeThenActions(context);
        } else {
            invokeElseActions(context);
        }
    }

    @Override
    public boolean evaluateCondition(Context context) {
        boolean res = logicalOperator == LogicalOperator.AND;

        for(Condition condition : this.conditionList){
            switch (logicalOperator){
                case OR:
                    res = res || condition.evaluateCondition(context);
                    break;
                case AND:
                    res = res && condition.evaluateCondition(context);
                    break;
            }
        }
        return res;
    }

    public void addCondition(Condition conditionToAdd){
        this.conditionList.add(conditionToAdd);
    }

}
