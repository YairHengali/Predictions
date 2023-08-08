package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;

public class SetAction extends AbstractAction {
    String valueExpression;
    public SetAction(String mainEntityName, String valueExpression){
        super(ActionType.SET, mainEntityName);
        this.valueExpression = valueExpression;
    }

    @Override
    public void Run(Context context) throws Exception {
        //TODO WITH EXPRESSIONS
    }
}
