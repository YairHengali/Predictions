package engine.action;

public class Increase implements Action{
    String entityName;
    String propertyName;
    int amountToTncrease;

    public Increase(){
        amountToTncrease = //MAYBE ENVIRONMENT
    }

    @Override
    public void Run() {
        //FOREACH ENTITY : GET ENTITIES()
            //IF ENTITY.NAME == entityName
                //FOREACH PROPERTY : GET PROPERTIES()
                    //IF PROPERTY.NAME == propertyName


    }
}
