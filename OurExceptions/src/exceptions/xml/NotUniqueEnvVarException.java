package exceptions.xml;

public class NotUniqueEnvVarException extends RuntimeException{
//    private String envVarName;

    public NotUniqueEnvVarException(String envVarName)
    {
        super("Invalid xml file! En environment variable with the name: " + envVarName + " is already exists. \n each environment variable must have a unique name!");
    }
}
