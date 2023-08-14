package engineAnswers;

public class ActiveEnvVarDto {
    String name;
    String value;

    public ActiveEnvVarDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
