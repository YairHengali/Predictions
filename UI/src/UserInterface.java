import engine.property.PropertyType;
import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import engineAnswers.*;
import ofir.menu.api.Menu;
import ofir.menu.api.MenuManager;
import ofir.menu.impl.MenuManagerImpl;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private SystemEngine systemEngine = new SystemEngineImpl();
    private SimulationDetailsDTO simulationDetailsDTO;
    private boolean exit = false;

    private MenuManager menuManager = new MenuManagerImpl();

    void buildMainMenu(){
        Menu mainMenu = menuManager.createMenu("mainMenu");
        mainMenu.addItem("Load simulation");
        mainMenu.addItem("Show loaded simulation details");
        mainMenu.addItem("Run simulation");
        mainMenu.addItem("Show full details of a previous simulation");
        mainMenu.addItem("Save system state to file");
        mainMenu.addItem("Load system state from file");
        mainMenu.addItem("Exit");
        menuManager.addMenu(mainMenu);
    }
    private void buildEnvironmentVariablesInitiationMenu(){
        Menu EnvVarMenu = menuManager.createMenu("EnvVarMenu");
        EnvVarMenu.addItem("Insert value");
        EnvVarMenu.addItem("Skip (let system choose a random value)");
        menuManager.addMenu(EnvVarMenu);
    }

    private void buildBooleanChoiceForUserMenu(){
        Menu booleanChoice = menuManager.createMenu("booleanChoice");
        booleanChoice.addItem("True");
        booleanChoice.addItem("False");
        menuManager.addMenu(booleanChoice);
    }

    public void printMenu(String menuName){
        menuManager.showMenuByName(menuName);
    }

    public MenuManager getMenuManager(){return this.menuManager;}
    public boolean isExit(){return this.exit;}

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
                    systemEngine.loadSimulation("./SystemEngine/src/resources/master-ex1.xml");
                    System.out.println("xml file loaded successfully!");
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
                List<PropertyDTO> envVarDtos = systemEngine.getEnvVarsDefinitionDto();
                letUserChooseEnvVarsValues(envVarDtos);
                List<ActiveEnvVarDto> activeEnvVarDtos = systemEngine.getActiveEnvVarsDto();
                printActiveEnvVars(activeEnvVarDtos);
                systemEngine.runSimulation();
                break;
            case 4:
                List<pastSimulationDTO> pastSimulationsDetails = systemEngine.getPastSimulationsDetails();
                //TODO: implement menu of this option
                break;
            case 5:
                serializeEngine();
                break;
            case 6:
                deserializeEngine();
                break;
            case 7:
                this.exit = true;
                break;
        }
    }

    private void printActiveEnvVars(List<ActiveEnvVarDto> activeEnvVarDtos) {
        System.out.println("The values that determined for environment variables are:");
        for (ActiveEnvVarDto activeEnvVarDto: activeEnvVarDtos) {
            System.out.println("Name: " + activeEnvVarDto.getName() + " Value: " + activeEnvVarDto.getValue());
        }
        System.out.println(System.lineSeparator() + "Running the simulation!" + System.lineSeparator());
    }

    private void decodeEnvVarMenu(int choice){
        if(choice == 1){

        }
    }

//MINE:
    private void letUserChooseEnvVarsValues(List<PropertyDTO> envVarsDto) {
        buildEnvironmentVariablesInitiationMenu();
        buildBooleanChoiceForUserMenu();
        System.out.println("Environment variables initialization:");
        System.out.println("====================================");
        for (PropertyDTO envVarDto : envVarsDto) {
            System.out.println("        Name: " + envVarDto.getName());
            System.out.println("        Type: " + envVarDto.getType());
            if (envVarDto.getFrom() != null) {
                if (envVarDto.getType().equals(PropertyType.DECIMAL.toString()))
                {
                    System.out.println("        Range: " + envVarDto.getFrom().intValue() + " to " + envVarDto.getTo().intValue());
                }
                else
                {
                    System.out.println("        Range: " + envVarDto.getFrom() + " to " + envVarDto.getTo());
                }
            }

            letUserChooseEnvVarValue(envVarDto);
        }
    }

    ///MINE:
    private PropertyDTO letUserChooseBooleanEnvVarValue(PropertyDTO envVarDto) {
        //boolean isValidInput = false;
        String value = "true";
        int userChoice = 0;

        menuManager.showMenuByName("booleanChoice");
        //while(!isValidInput) {
        userChoice = menuManager.getMenuByName("booleanChoice").getValidInput();
        if (userChoice == 2)
            value = "false";
        return new PropertyDTO(envVarDto.getName(), envVarDto.getType(), envVarDto.getFrom(), envVarDto.getTo(), false, value);
    }

    //MINE:
    private void letUserChooseEnvVarValue(PropertyDTO envVarDto) {
        PropertyDTO resPropertyDto;
        String userInputValue;
        boolean isValidInput = false;
        int userChoice;
        Scanner scanner;

        while(!isValidInput) {
            menuManager.showMenuByName("EnvVarMenu");
            //System.out.println("Please choose the number of the preferred methode (skip = random value in range)");
            userChoice = menuManager.getMenuByName("EnvVarMenu").getValidInput();
            if (userChoice == 1) { // User initiation
                if(envVarDto.getType().equals(PropertyType.BOOLEAN.toString()))
                {
                    resPropertyDto = letUserChooseBooleanEnvVarValue(envVarDto);
                }
                else{
                    System.out.println("Please insert a valid value:");
                    scanner = new Scanner(System.in);
                    userInputValue = scanner.next(); // TODO: need to validate here or at the set property???
                    resPropertyDto = new PropertyDTO(envVarDto.getName(), envVarDto.getType(), envVarDto.getFrom(), envVarDto.getTo(), false, userInputValue);
                }
            }
            else { // (userChoice == 2) means Random initiation
                resPropertyDto = new PropertyDTO(envVarDto.getName(), envVarDto.getType(), envVarDto.getFrom(), envVarDto.getTo(), true, "");
            }

            try {
                systemEngine.setEnvVarFromDto(resPropertyDto);
                isValidInput = true;
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + " try again!");
            }
        }
    }

//    private void letUserChooseEnvVarsValues(List<PropertyDTO> envVarsDto) {
//        buildEnvironmentVariablesInitiationMenu();
//        buildBooleanChoiceForUserMenu();
//        System.out.println("Environment variables initialization:");
//        System.out.println("====================================");
//        for (PropertyDTO envVarDto : envVarsDto) {
//            System.out.println("        Name: " + envVarDto.getName());
//            System.out.println("        Type: " + envVarDto.getType());
//            if (envVarDto.getFrom() != null) {
//                System.out.println("        Range: " + envVarDto.getFrom() + " to " + envVarDto.getTo());
//            }
//
//            if(envVarDto.getType().equals(PropertyType.BOOLEAN.toString()))
//                letUserChooseBooleanEnvVarValue(envVarDto);
//            else
//                letUserChooseEnvVarValue(envVarDto);
//        }
//    }

//    private void letUserChooseBooleanEnvVarValue(PropertyDTO envVarDto) {
//        boolean isValidInput = false;
//        String value = "true";
//        int userChoice = 0;
//
//        menuManager.showMenuByName("booleanChoice");
//        while(!isValidInput) {
//            userChoice = menuManager.getMenuByName("EnvVarMenu").getValidInput();
//            if (userChoice == 2)
//                value = "false";
//            PropertyDTO resPropertyDto = new PropertyDTO(envVarDto.getName(), envVarDto.getType(), envVarDto.getFrom(), envVarDto.getTo(), false, value);
//            try {
//                systemEngine.setEnvVarFromDto(resPropertyDto);
//                isValidInput = true;
//            }
//            catch (Exception e) {
//                System.out.println("Error: " + e.getMessage() + " try again!");
//            }
//        }
//    }


//    private void letUserChooseEnvVarValue(PropertyDTO envVarDto) {
//        PropertyDTO resPropertyDto;
//        String userInputValue;
//        boolean isValidInput = false;
//        int userChoice;
//        Scanner scanner;
//
//        while(!isValidInput) {
//            menuManager.showMenuByName("EnvVarMenu");
//            //System.out.println("Please choose the number of the preferred methode (skip = random value in range)");
//            userChoice = menuManager.getMenuByName("EnvVarMenu").getValidInput();
//            if (userChoice == 1) { // User initiation
//                System.out.println("Please insert a valid value:");
//                scanner = new Scanner(System.in);
//                userInputValue = scanner.next(); // TODO: need to validate here or at the set property???
//                resPropertyDto = new PropertyDTO(envVarDto.getName(), envVarDto.getType(), envVarDto.getFrom(), envVarDto.getTo(), false, userInputValue);
//            }
//            else { // (userChoice == 2) means Random initiation
//                resPropertyDto = new PropertyDTO(envVarDto.getName(), envVarDto.getType(), envVarDto.getFrom(), envVarDto.getTo(), true, "");
//            }
//
//            try {
//                systemEngine.setEnvVarFromDto(resPropertyDto);
//                isValidInput = true;
//            }
//            catch (Exception e) {
//                System.out.println("Error: " + e.getMessage() + " try again!");
//            }
//        }
//    }
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
                    if (propertyDTO.getType().equals("DECIMAL"))
                    {
                        System.out.println("        Range: " + propertyDTO.getFrom().intValue() + " to " + propertyDTO.getTo().intValue());
                    }
                    else
                    {
                        System.out.println("        Range: " + propertyDTO.getFrom() + " to " + propertyDTO.getTo());
                    }
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


    private void serializeEngine() //TODO, make all Serielizable at the end and check
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the file path (including the file name) you would like to save to:");
        String filePath = scanner.nextLine();
        filePath = filePath + ".ser"; // \obj\bin

        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(systemEngine);
            System.out.println("System saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserializeEngine()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the file path (including the file name) you would like load the system from:");
        String filePath = scanner.nextLine();
        filePath = filePath + ".ser"; // \obj\bin

        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

//            return (SystemEngineImpl) in.readObject();
            this.systemEngine = (SystemEngineImpl) in.readObject();
            System.out.println("System loaded successfully!");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

