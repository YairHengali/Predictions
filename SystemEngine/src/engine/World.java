package engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class World {

    int currentNumberOfTicks = 0;
    // TODO: current Seconds of the simulation

    int maxNumberOfTicks;
    // TODO: max Seconds of the simulation

    private Collection<Entity> entities = new ArrayList<>();
    private Collection<Rule> rules = new ArrayList<>();
    private Collection<Property> EnvironmentVariables = new ArrayList<>();
}
