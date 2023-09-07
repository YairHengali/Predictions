package engine.action.impl.replace;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.entity.EntityInstance;
import engine.world.factory.SecondaryEntityDetails;

public class Replace extends AbstractAction {
    CreationMode creationMode;
    String entityToCreateName;
    public Replace(String mainEntityName,String entityToCreateName, SecondaryEntityDetails secondaryEntityDetails, CreationMode creationMode){
        super(ActionType.REPLACE, mainEntityName, secondaryEntityDetails);
        this.creationMode = creationMode;
        this.entityToCreateName = entityToCreateName;
    }

    @Override
    public void Run(Context context) {
        EntityInstance entityToKill = getMainEntityInstance(context);

        switch (creationMode) {
            case SCRATCH:
                context.getEntityInstanceManager().createScratchEntity(entityToCreateName);
//                context.getEntityInstanceManager().killEntity(context.getPrimaryEntityInstance());
                context.getEntityInstanceManager().killEntity(entityToKill);
                break;
            case DERIVED:
//                context.getEntityInstanceManager().createDerivedEntity(context.getPrimaryEntityInstance(), entityToCreateName);
                context.getEntityInstanceManager().createDerivedEntity(entityToKill, entityToCreateName);
                break;
        }
    }
}
