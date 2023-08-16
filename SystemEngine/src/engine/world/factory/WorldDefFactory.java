package engine.world.factory;

import engine.world.WorldDefinition;
import jaxb.generated.PRDWorld;

public interface WorldDefFactory {

    void insertDataToWorldDefinition(WorldDefinition simulationDef, PRDWorld generatedWorld);


}
