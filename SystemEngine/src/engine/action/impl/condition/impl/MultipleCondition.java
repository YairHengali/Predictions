package engine.action.impl.condition.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.impl.condition.LogicalOperator;
import engine.action.impl.condition.api.Condition;
import engine.entity.EntityInstance;
import engine.entity.manager.EntityInstanceManager;
import engine.property.api.PropertyInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultipleCondition extends AbstractAction implements Condition {
    List<Condition> conditionList;
    LogicalOperator logicalOperator;
    List<Action> thenActions;
    List<Action> elseActions;

    public MultipleCondition(String mainEntityName, String propertyName, LogicalOperator logicalOperator){
        super(ActionType.CONDITION,mainEntityName);
        this.logicalOperator = logicalOperator;
        thenActions = new ArrayList<>();
        elseActions = new ArrayList<>();
    }

    @Override
    public void Run(EntityInstanceManager manager) throws Exception {
        if (evaluateCondition()) {
            invokeThenActions(manager);
        } else {
            invokeElseActions(manager);
        }

    }

    @Override
    public boolean evaluateCondition() {
        boolean res = true;

        for(Condition condition : this.conditionList){
            switch (logicalOperator){
                case OR:
                    res = res || condition.evaluateCondition();
                    break;
                case AND:
                    res = res && condition.evaluateCondition();
                    break;
            }
        }
        return res;
    }

    @Override
    public void addActionToThen(Action actionToAdd) {
        this.thenActions.add(actionToAdd);
    }

    @Override
    public void addActionToElse(Action actionToAdd) {
        this.elseActions.add(actionToAdd);
    }

    private void invokeThenActions(EntityInstanceManager manager){
        thenActions.forEach(action -> {
            try {
                action.Run(manager);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void invokeElseActions(EntityInstanceManager manager){
        elseActions.forEach(action -> {
            try {
                action.Run(manager);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void addCondition(Condition conditionToAdd){
        this.conditionList.add(conditionToAdd);
    }

}
