package gpup.console.app;

import gpup.components.target.TargetType;
import gpup.console.validation.ConsoleIOValidations;
import gpup.dto.TargetDTO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GPUPConsoleIO {

    public static UserInput mainMenu() {
        int input;
        System.out.println("\n=========== G.P.U.P System ===========");
        System.out.println(" 1. Load GPUP system file\n" +
                " 2. Display Target-Graph information\n" +
                " 3. Display target information\n" +
                " 4. Find dependency between 2 targets\n" +
                " 5. Run a task\n" +
                " 6. Exit");
        System.out.println("(-- At each stage of the program, pressing 'QP' will return you to the main menu --)\n");
        input = getMainMenuInput();
        return UserInput.values()[input];
    }

    private static int getMainMenuInput() {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        boolean validInput;

        do {
            System.out.print("Please choose your action: ");
            try {
                value = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException ex) {
                System.out.println("Wrong input - this is not a number, try again.");
                validInput = false;
                scanner.nextLine();
            }
            if (validInput) {
                validInput = ConsoleIOValidations.integerRangeCheck(value);
                if (!validInput) {
                    System.out.println("Wrong input - please enter a valid number.");
                }
            }
        } while (!validInput);

        return value;
    }

    public static String getXmlPath() {
        String path;
        System.out.println("Enter the full path of the XML file you want to load:");
        path = getStringInput("path"); // return a valid path, yet no promise of existing file
        return path;
    }

    static String getStringInput(String msg) {
        Scanner scanner = new Scanner(System.in);
        boolean valid;
        String input = null;
        do {
            try {
                input = scanner.nextLine();
                valid = true;
            } catch (Exception ex) {
                System.out.println("Wrong input - this is not a valid "+ msg +", try again.");
                valid = false;
                scanner.nextLine();
            }
        } while (!valid);

        return input;
    }

    public static void printExceptionMessage(String s) {
        System.out.println("Loading the file end with a failure:");
        System.out.println(s);
    }

    public static void successLoading() {
        System.out.println("The File loaded successfully!\n");
    }

    public static void targetNameRequest() {
        System.out.println("Please enter a target's name");
        System.out.println("   (in order to return to the main menu, press 'QP')");
    }

    public static void ShowTargetsNum(int totalTargetsNumber) {
        System.out.println("There Are " + totalTargetsNumber + " Targets In The System.\n");
    }

    public static void ShowSpecificTargetsNum(TargetType targetType, int specificTypeOfTargetsNum) {
        System.out.println("There Are " + specificTypeOfTargetsNum + " " + targetType.name() +" Targets In The System.");
    }

    public static void printMsg(String s) {
        System.out.println(s);
    }

    public static void unloadedSystem() {
        System.out.println("The GPUP System is not loaded yet, please load a Xml file first.");
    }

    public static void showTargetInfo(TargetDTO targetDTO) {
        System.out.println(targetDTO.toString());
    }
}
