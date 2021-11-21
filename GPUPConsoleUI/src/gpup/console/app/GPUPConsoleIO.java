package gpup.console.app;

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
        path = getStringInput("path"); // return a valid path, yet no promise of existing file
        return path;
    }

    static String getStringInput(String inputType) {
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        String input = null;
        do {
            try {
                input = scanner.nextLine();
                valid = true;
            } catch (Exception ex) {
                System.out.println("Wrong input - this is not a valid "+ inputType +", try again.");
                valid = false;
                scanner.nextLine();
            }
        } while (!valid);

        return input;
    }

    public static void printExceptionMessege(String s) {
        System.out.println("Loading the file end with a failure:");
        System.out.println(s);
    }

    public static void successLoading() {
        System.out.println("The File loaded successfully!\n");
    }

    public static void targetInfoMenu() {
        System.out.println("Please enter the target's name");
        System.out.println("   (in order to return to the main menu, press 'QP')");
    }
}
