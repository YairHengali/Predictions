import engine.Range;
import engine.SystemEngine;
import engine.World;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        SystemEngine systemEngine = new SystemEngine();
        //CREATE WORLD:
        World testSimulation = systemEngine.getSimulation();
        testSimulation.addEntity("person", 100);
        testSimulation.getEntityByName("person").addProperty("age", 5, new Range(2,10));
        testSimulation.getEntityByName("person").addProperty("smoker", false, null);
        testSimulation.getEntityByName("person").addProperty( "balance",8000.55, null);

        //PRINT WORLD:
        System.out.println(systemEngine.showSimulation());
        }
    }
