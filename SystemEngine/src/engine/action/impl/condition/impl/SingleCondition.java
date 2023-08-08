package engine.action.impl.condition.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;
import engine.property.impl.BooleanProperty;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.property.impl.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class SingleCondition extends AbstractAction implements Condition
{
    String propertyName;
    ConditionOp operator;
    String valueToCompare;
    List<Action> thenActions;
    List<Action> elseActions;


    public SingleCondition(String mainEntityName, String propertyName, String valueToCompare) { //TODO: EXCEPTION IF property from non-Number type
        super(ActionType.CONDITION, mainEntityName);
        this.propertyName = propertyName;
        this.valueToCompare = valueToCompare;
        this.thenActions = new ArrayList<>();
        this.elseActions = new ArrayList<>();

    }
//    @Override
//    public void Run(Context context) throws Exception {
//        boolean resCondition = false;
//
//        for (EntityInstance entityInstance : context.getInstancesLists().get(this.mainEntityName)) {
//            currPropertyUsedInEvaluate = entityInstance.getPropertyByName(this.propertyName);
//            resCondition = evaluateCondition();
//
//            if (resCondition) {
//                invokeThenActions(context);
//            }
//            else {
//                invokeElseActions(context);
//            }
//        }
//    }

    @Override
    public void Run(Context context) throws Exception {
        boolean resCondition = false;

        resCondition = evaluateCondition(context);

        if (resCondition) {
            invokeThenActions(context);
        }
        else {
            invokeElseActions(context);
        }
    }

    @Override
    public boolean evaluateCondition(Context context) {
        boolean result = false;

        PropertyInstance propertyToEvaluate = context.getPrimaryEntityInstance().getPropertyByName(this.propertyName);
        //TODO: figuring value out of expression

        switch (propertyToEvaluate.getType()) {
            case BOOLEAN:
                result = operator.eval( ((BooleanProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.STRING);
                break;
            case DECIMAL:
                result = operator.eval( ((DecimalProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.DECIMAL);
                break;
            case FLOAT:
                result = operator.eval( ((FloatProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.FLOAT);
                break;
            case STRING:
                result = operator.eval( ((StringProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.STRING);
                break;
        }

        return result;
    }

    @Override
    public void addActionToThen(Action actionToAdd) {
        this.thenActions.add(actionToAdd);
    }

    @Override
    public void addActionToElse(Action actionToAdd) {
        this.elseActions.add(actionToAdd);
    }

//    void invokeThenActions(EntityInstanceManager manager){
//        thenActions.forEach(action -> {
//            try {
//                action.Run(manager);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//    void invokeElseActions(EntityInstanceManager manager){
//        elseActions.forEach(action -> {
//            try {
//                action.Run(manager);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    void invokeThenActions(Context context){
        for (Action action: thenActions) {
            try {
                action.Run(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
    void invokeElseActions(Context context){
        for (Action action: elseActions) {
            try {
                action.Run(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getPropertyName() {
        return propertyName;
    }
}
