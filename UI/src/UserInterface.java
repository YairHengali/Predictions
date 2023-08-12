import engine.rule.Rule;
import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import engineAnswers.*;
import ofir.menu.api.Menu;
import ofir.menu.api.MenuManager;
import ofir.menu.impl.MenuManagerImpl;

public class UserInterface {
    SystemEngine systemEngine = new SystemEngineImpl();
    SimulationDetailsDTO simulationDetailsDTO;

    private MenuManager menuManager = new MenuManagerImpl();

    void buildMainMenu(){
        Menu mainMenu = menuManager.createMenu("mainMenu");
        mainMenu.addItem("Load simulation");
        mainMenu.addItem("Run simulation");
        mainMenu.addItem("Show past simulation");
        menuManager.addMenu(mainMenu);
    }
    void printMenu(String menuName){
        menuManager.showMenuByName(menuName);
    }

    MenuManager getMenuManager(){return this.menuManager;}

    public void decodeUserChoice(String menuName, int choice){
        switch(menuName){
            case "mainMenu":
                decodeMainMenu(choice);
                break;
        }
    }

    private void decodeMainMenu(int choice)
    {
        switch (choice){
            case 1:
                try {
                    systemEngine.loadSimulation("./SystemEngine/src/resources/ex1-cigarets.xml");
                } catch (Exception e) {
                    //TODO: DEAL WITH EXCEPTION
                }
                break;
            case 2:
                if(systemEngine.isThereLoadedSimulation()) {//TODO: TEMPORARY TESTING, PROBABLY NEEDED MORE COMPLEX ONE
                    simulationDetailsDTO = systemEngine.showSimulationDetails();
                    printSimulationDetails();
                }
                else{
                    throw new RuntimeException("there is no simulation to show, please load one first");
                }

                break;
            case 3:
                systemEngine.runSimulation();
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void printSimulationDetails()
    {
        System.out.println("Currently loaded simulation details:");
        System.out.println("Entities:");
        for (EntityDTO entityDTO : simulationDetailsDTO.getEntities()) {
            System.out.println("    Name: " + entityDTO.getName());
            System.out.println("    Population: " + entityDTO.getPopulation());
            System.out.println("    Properties:");
            for (PropertyDTO propertyDTO : entityDTO.getProperties()){
                System.out.println("        Name: " + propertyDTO.getName());
                System.out.println("        Type: " + propertyDTO.getType());
                if (propertyDTO.getFrom() != null)
                    System.out.println("        Range: " + propertyDTO.getFrom() + " to " + propertyDTO.getTo());
                System.out.println("        Is initialized randomly: " + propertyDTO.isInitialisedRandomly());
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("Rules:");
        for (RuleDTO ruleDTO : simulationDetailsDTO.getRules()) {
            System.out.println("    Name: " + ruleDTO.getName());
            System.out.println("    Ticks for activation: " + ruleDTO.getTicksForActivation());
            System.out.println("    Probability for activation: " + ruleDTO.getProbabilityForActivation());
            System.out.println("    Number of actions: " + ruleDTO.getActions().size());
            System.out.println("    Actions name's: ");
            for (ActionDTO actionDTO : ruleDTO.getActions()){
                System.out.println("        Name: " + actionDTO.getName());
            }
            System.out.println();
        }
        System.out.println("Termination conditions: "); //TODO: NOT ALL CONDITIONS MUST BE SET! NEED TO DEAL WITH NULLS
        System.out.println("Number of ticks: " + simulationDetailsDTO.getMaxNumberOfTicks());
        System.out.println("Number of seconds: " + simulationDetailsDTO.getSecondsToTerminate());
    }
}
