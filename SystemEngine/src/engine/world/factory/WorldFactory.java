package engine.world.factory;

import engine.world.WorldDefinition;
import jaxb.generated.PRDWorld;

public interface WorldFactory {

    void insertDataToWorldDefinition(WorldDefinition simulationDef, PRDWorld generatedWorld);


}
