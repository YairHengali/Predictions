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
