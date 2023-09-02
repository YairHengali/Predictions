package engine.world.grid.manager;

import com.sun.javafx.sg.prism.GrowableDataBuffer;
import engine.entity.EntityInstance;
import engine.world.grid.location.GridLocation;

import java.util.List;
import java.util.Map;

public interface GridManagerAPI {
    void setEmptyGrid(int rowSize, int colSize);
    void initEntitiesRandomly(Map<String, List<EntityInstance>> name2EntInstancesList);
    void makeRandomMoveToAllEntities(Map<String, List<EntityInstance>> name2EntInstancesList);
    void setEntityInLocation(EntityInstance newEntity, GridLocation location);
    void replaceEntitiesInLocation(EntityInstance outEntity, EntityInstance inEntity, GridLocation location);

}
