package engineAnswers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HistogramDTO {
    private int simulationID;
    private String entityName;
    private String propertyName;
    private Map<String, Long> propertyHistogram;

    public HistogramDTO(int simulationID, String entityName, String propertyName, Map<String, Long> propertyHistogram) {
        this.simulationID = simulationID;
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.propertyHistogram = propertyHistogram;
    }
    public int getSimulationID() {
        return simulationID;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Map<String, Long> getPropertyHistogram() {
        return propertyHistogram;
    }

    @Override
    public String toString() {
        StringBuilder res =
                new StringBuilder("simulation ID = " + simulationID + '\n' +
                        "entity name = " + entityName + '\n' +
                        "property name = " + propertyName + '\n' +
                        "property Histogram: " + '\n' + "==================" + '\n');
        for(Map.Entry<String, Long> entry : propertyHistogram.entrySet()) {
            res.append("Value: ").append(entry.getKey()).append("   Num of entities: ").append(entry.getValue()).append('\n');
        }

        return res.toString();
    }
}
