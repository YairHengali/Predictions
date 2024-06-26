package engineAnswers;

public class AveragePropertyValueDTO {
    final String entityName;
    final String propertyName;
    final Double value;

    public AveragePropertyValueDTO(String entityName, String propertyName, Double value) {
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Double getValue() {
        return value;
    }
}
