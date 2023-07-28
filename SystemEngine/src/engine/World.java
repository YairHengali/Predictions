package engine;

import org.omg.CORBA.Environment;

import java.util.*;

public class World {

    int currentNumberOfTicks = 0;
    // TODO: current Seconds of the simulation

    int maxNumberOfTicks;
    // TODO: max Seconds of the simulation

    private Map<String, Entity> name2entity = new HashMap<>();
    private Collection<Rule> rules = new ArrayList<>();
    private Collection<Property<?>> EnvironmentVariables = new ArrayList<>();


}
