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
    public boolean evaluateCondition(Context context) { //TODO: VALIDATE THAT IS WORKING NOW WHEN ADDED CONTEXT AND MAYBE CAN BE DONE BETTER
        boolean res = true;

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

//    @Override
//    public void addActionToThen(Action actionToAdd) {
//        this.thenActions.add(actionToAdd);
//    }
//
//    @Override
//    public void addActionToElse(Action actionToAdd) {
//        this.elseActions.add(actionToAdd);
//    }

//    private void invokeThenActions(EntityInstanceManager manager){
//        thenActions.forEach(action -> {
//            try {
//                action.Run(manager);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//    private void invokeElseActions(EntityInstanceManager manager){
//        elseActions.forEach(action -> {
//            try {
//                action.Run(manager);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

//    void invokeThenActions(Context context){
//        for (Action action: thenActions) {
//            try {
//                action.Run(context);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }
//    void invokeElseActions(Context context){
//        for (Action action: elseActions) {
//            try {
//                action.Run(context);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public void addCondition(Condition conditionToAdd){
        this.conditionList.add(conditionToAdd);
    }

}
