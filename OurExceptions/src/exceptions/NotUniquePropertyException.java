package exceptions;

public class NotUniquePropertyException extends RuntimeException{
//    private String propertyName;
//    private String entityName;

    public NotUniquePropertyException(String propertyName, String entityName)
    {
        super("Invalid xml file! A property named " + propertyName + "is already exists in the entity: " + entityName + ". each property in an entity must have a unique name!");
    }
}
