package exceptions;

public class NotUniqueEnvVarException extends RuntimeException{
//    private String envVarName;

    public NotUniqueEnvVarException(String envVarName)
    {
        super("Invalid xml file! En environment variable with the name: " + envVarName + " is already exists. each environment variable needs to have a unique name!");
    }
}
