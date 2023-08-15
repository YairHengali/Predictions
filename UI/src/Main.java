import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import engineAnswers.SimulationDetailsDTO;
import ofir.menu.api.Menu;
import ofir.menu.api.MenuItem;
import ofir.menu.api.MenuManager;
import ofir.menu.impl.MenuItemImpl;
import ofir.menu.impl.MenuManagerImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserInterface UI = new UserInterface();
        UI.buildMainMenu();

        do {
            UI.printMenu("mainMenu");
            UI.decodeUserChoice("mainMenu", UI.getMenuManager().getMenuByName("mainMenu").getValidInput());
        }
        while (!UI.isExit());

    }
}
