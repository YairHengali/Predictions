package exceptions.xml;

public class NotExistingEntityException extends RuntimeException{
    public NotExistingEntityException(String entityName, String actionName)
    {
        super("Invalid xml file! The entity: " + entityName + " that referenced in action: " + actionName + " does not exist!");
    }
}