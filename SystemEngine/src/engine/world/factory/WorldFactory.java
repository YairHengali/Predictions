package engine.world.factory;

import engine.World;
import jaxb.generated.PRDWorld;

public interface WorldFactory {
    World createWorld();
    void insertDataToWorld(World simulation);
    void setGeneratedWorld(PRDWorld worldToSet);




}
