import engine.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        SystemEngine systemEngine = new SystemEngine();
        //CREATE WORLD:
        World testSimulation = systemEngine.getSimulation();
        testSimulation.addEntity("person", 100);
        testSimulation.getEntityByName("person").addProperty("age", PropertyType.DECIMAL, 5, new Range(2,10), false);
        testSimulation.getEntityByName("person").addProperty("smoker",PropertyType.BOOLEAN, false, null, false);
        testSimulation.getEntityByName("person").addProperty( "balance",PropertyType.FLOAT, 8000.55, null, false);

        //PRINT WORLD:
        System.out.println(systemEngine.showSimulation());

        //Trying to set increase rule:
        testSimulation.addRule("testIncreaseRule", 1, 1);
        testSimulation.getRuleByName("testIncreaseRule").addIncreaseAction(testSimulation.getEntityByName("person"), (Property<Number>)testSimulation.getEntityByName("person").getPropertyByName("age"), 1);//TODO: HOW TO DO WITHOUT CASTING
        testSimulation.getRuleByName("testIncreaseRule").runRule();

        //PRINT WORLD:
        System.out.println(systemEngine.showSimulation());
        }
    }
