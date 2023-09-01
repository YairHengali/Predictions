package engine.action.impl.replace;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.world.factory.SecondaryEntityDetails;

public class Replace extends AbstractAction {
    CreationMode creationMode;
    String entityToCreateName; //TODO: DOES "CREATE" IS THE SECONDARY? (IF SO, WE NEED TO CHANGE THE FUNCTION IN FACTORY WHERE WE CREATE IT) FOR NOW I DID LIKE THAT FOR TESTING
    public Replace(String mainEntityName,String entityToCreateName, SecondaryEntityDetails secondaryEntityDetails, CreationMode creationMode){
        super(ActionType.REPLACE, mainEntityName, secondaryEntityDetails);
        this.creationMode = creationMode;
        this.entityToCreateName = entityToCreateName;
    }

    @Override
    public void Run(Context context) {
        switch (creationMode) {
            case SCRATCH:
//                context.getEntityInstanceManager().createScratchEntity(secondaryEntityDetails.getName());
                context.getEntityInstanceManager().createScratchEntity(entityToCreateName);
                context.getEntityInstanceManager().killEntity(context.getPrimaryEntityInstance());
                break;
            case DERIVED:
//                context.getEntityInstanceManager().createDerivedEntity(context.getPrimaryEntityInstance(), secondaryEntityDetails.getName());
                context.getEntityInstanceManager().createDerivedEntity(context.getPrimaryEntityInstance(), entityToCreateName);
                break;
        }
    }
}
