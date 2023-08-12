import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import ofir.menu.api.Menu;
import ofir.menu.api.MenuManager;
import ofir.menu.impl.MenuManagerImpl;

public class UserInterface {
    SystemEngine systemEngine = new SystemEngineImpl();

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
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }
}
