package gpup.console.app;

import gpup.components.target.TargetType;
import gpup.console.validation.ConsoleIOValidations;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GPUPConsoleIO {

    public static UserInput mainMenu() {
        int input;
        UserInput userInput = UserInput.INIT;
        System.out.println("\n=========== G.P.U.P System ===========");
        System.out.println(" 1. Load GPUP's system file\n" +
                " 2. Display Target-Graph information\n" +
                " 3. Display target information\n" +
                " 4. Find dependency between 2 targets\n" +
                " 5. Run a task\n" +
                " 6. Exit\n");
        input = getMainMenuInput();
        return UserInput.values()[input];
    }


    private static int getMainMenuInput() {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        boolean validInput = false;

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
        path = getPathInput(); // return a valid path, yet no promise of existing file
        return path;
    }

    private static String getPathInput() {
        Scanner scanner = new Scanner(System.in);
        boolean validPath = false;
        String path = null;
        do {
            try {
                path = scanner.nextLine();
                validPath = true;
            } catch (Exception ex) {
                System.out.println("Wrong input - this is not a valid path, try again.");
                validPath = false;
                scanner.nextLine();
            }
        } while (!validPath);

        return path;
    }

    public static void ShowTargetsNum(int totalTargetsNumber) {
        System.out.println("There Are " + totalTargetsNumber + " Targets In The System.");
    }


    public static void ShowSpecificTargetsNum(TargetType targetType, int specificTypeOfTargetsNum) {
        System.out.println("There Are " + specificTypeOfTargetsNum + " " + targetType.name() +" Targets In The System.");
    }
}
