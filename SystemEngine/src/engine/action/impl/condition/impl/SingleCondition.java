package engine.action.impl.condition.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.api.Condition;
import engine.entity.EntityInstance;
import engine.entity.manager.EntityInstanceManager;
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

    PropertyInstance currPropertyUsedInEvaluate;

    public SingleCondition(String mainEntityName, String propertyName, String valueToCompare) { //TODO: EXCEPTION IF property from non-Number type
        super(ActionType.CONDITION, mainEntityName);
        this.propertyName = propertyName;
        this.valueToCompare = valueToCompare;
        this.thenActions = new ArrayList<>();
        this.elseActions = new ArrayList<>();

    }
    @Override
    public void Run(EntityInstanceManager manager) throws Exception {
        boolean resCondition = false;

        for (EntityInstance entityInstance : manager.getInstancesLists().get(this.mainEntityName)) {
            currPropertyUsedInEvaluate = entityInstance.getPropertyByName(this.propertyName);
            resCondition = evaluateCondition();

            if (resCondition) {
                invokeThenActions(manager);
            }
            else {
                invokeElseActions(manager);
            }
        }
    }

    @Override
    public boolean evaluateCondition() {
        boolean result = false;
        //TODO: figuring value out of expression

        switch (currPropertyUsedInEvaluate.getType()) {
            case BOOLEAN:
                result = operator.eval( ((BooleanProperty) currPropertyUsedInEvaluate).getValue(), valueToCompare, PropertyType.STRING);
                break;
            case DECIMAL:
                result = operator.eval( ((DecimalProperty) currPropertyUsedInEvaluate).getValue(), valueToCompare, PropertyType.DECIMAL);
                break;
            case FLOAT:
                result = operator.eval( ((FloatProperty) currPropertyUsedInEvaluate).getValue(), valueToCompare, PropertyType.FLOAT);
                break;
            case STRING:
                result = operator.eval( ((StringProperty) currPropertyUsedInEvaluate).getValue(), valueToCompare, PropertyType.STRING);
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

    void invokeThenActions(EntityInstanceManager manager){
        thenActions.forEach(action -> {
            try {
                action.Run(manager);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    void invokeElseActions(EntityInstanceManager manager){
        elseActions.forEach(action -> {
            try {
                action.Run(manager);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getPropertyName() {
        return propertyName;
    }
}
