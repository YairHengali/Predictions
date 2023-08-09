package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;

public class Decrease extends AbstractAction {
    String propertyName;
    String byExpression; //TODO: Expression????

    public Decrease(String mainEntityName, String propertyName, String byExpression) {
        super(ActionType.INCREASE, mainEntityName);
        this.propertyName = propertyName;
        this.byExpression = byExpression; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }


    @Override
    public void Run(Context context) throws Exception {
            //TODO: copy from increase
    }
}
