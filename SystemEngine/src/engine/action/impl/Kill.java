package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;

public class Kill extends AbstractAction {

    public Kill(String mainEntityName){
        super(ActionType.KILL, mainEntityName);
    }

    @Override
    public void Run(Context context) throws Exception {
        context.getEntityInstanceManager().killEntity(context.getPrimaryEntityInstance());
    }
}
