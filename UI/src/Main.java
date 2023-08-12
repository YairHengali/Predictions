import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import engineAnswers.SimulationDetailsDTO;
import ofir.menu.api.Menu;
import ofir.menu.api.MenuItem;
import ofir.menu.api.MenuManager;
import ofir.menu.impl.MenuItemImpl;
import ofir.menu.impl.MenuManagerImpl;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        SystemEngine systemEngine = new SystemEngineImpl();

        //TODO: need to mova all the menu build to another func
        MenuManager menuManager = new MenuManagerImpl();
        Menu mainMenu = menuManager.createMenu("main");
        mainMenu.addItem("Load simulation");
        mainMenu.addItem("Run simulation");
        mainMenu.addItem("Show past simulation");
        menuManager.addMenu(mainMenu);
        menuManager.showMenuByName("main");

        System.out.println("enter choice num in range: ");
        int userChoice = 2;
        while(!menuManager.getMenuByName("main").isInRange(userChoice)){
            System.out.println("Bad choice, try again:");
            // TODO: read input from user - just an example
        }

        switch(userChoice){
            case 1:
                try {
                    systemEngine.loadSimulation("./SystemEngine/src/resources/ex1-cigarets.xml");
                } catch (Exception e) {
                    //TODO: DEAL WITH EXCEPTION
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }





        SimulationDetailsDTO simulationDetailsDTO = systemEngine.showSimulationDetails();

        systemEngine.runSimulation();



                /*
        //CREATE WORLD:
        World testSimulation = systemEngine.getSimulation();
        testSimulation.addEntity("person", 100);
        Property<Number> testProp = new Property<>("age", PropertyType.DECIMAL, 5, new Range(2,10), false);
        testSimulation.getEntityByName("person", 0).addProperty2(testProp);
//        testSimulation.getEntityByName("person").addProperty("age", PropertyType.DECIMAL, 5, new Range(2,10), false);
        testSimulation.getEntityByName("person", 0).addProperty("smoker",PropertyType.BOOLEAN, false, null, false);
        testSimulation.getEntityByName("person", 0).addProperty( "balance",PropertyType.FLOAT, 8000.55, null, false);

        //PRINT WORLD:
        System.out.println(systemEngine.showSimulation());

        //Trying to set increase rule:
        testSimulation.addRule("testIncreaseRule", 1, 1);

        testSimulation.getRuleByName("testIncreaseRule").addIncreaseAction(testSimulation.getEntityByName("person", 0), testProp, 1);//TODO: WITHOUT CASTING - is it a good way? get the property external.

//        testSimulation.getRuleByName("testIncreaseRule").addIncreaseAction(testSimulation.getEntityByName("person"), (Property<Number>)testSimulation.getEntityByName("person").getPropertyByName("age"), 1);//TODO: HOW TO DO WITHOUT CASTING
        testSimulation.getRuleByName("testIncreaseRule").runRule();

        //PRINT WORLD:
        System.out.println(systemEngine.showSimulation());

         */

        }

    private void printSimulationDetails(){

    }
    }
