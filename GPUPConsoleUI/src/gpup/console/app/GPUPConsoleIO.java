package gpup.console.app;

import gpup.components.target.TargetType;
import gpup.console.validation.IOValidations;
import gpup.dto.ProcessedTargetDTO;
import gpup.dto.TargetDTO;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Consumer;

public class GPUPConsoleIO implements Consumer<ProcessedTargetDTO> {
    @Override
    public void accept(ProcessedTargetDTO processedTarget) {
        System.out.println(processedTarget.getName());
    }

    public static void welcome() {
        System.out.println("Hello and welcome to G.P.U.P - Generic system for streamlining and improving processes");
    }

    public static UserInput mainMenu() {
        int input;
        System.out.println("\n=========== G.P.U.P System ===========");
        System.out.println(" 1. Load GPUP system file\n" +
                " 2. Display Target-Graph information\n" +
                " 3. Display target information\n" +
                " 4. Find dependency between 2 targets\n" +
                " 5. Run a task\n" +
                " 6. Exit");
        System.out.println("(-- At each stage of the program, pressing '0' will return you to the main menu --)\n");
        input = getIntegerInput();
        return UserInput.values()[input];
    }

    public static int getIntegerInput() {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        boolean validInput;

        do {
            System.out.print("Please choose an option: ");
            try {
                value = scanner.nextInt();
                if (value == 0) { // quit
                    break;
                }
                validInput = true;
            } catch (InputMismatchException ex) {
                System.out.println("Wrong input - this is not a number, try again.");
                validInput = false;
                scanner.nextLine();
            }
            if (validInput) {
                validInput = IOValidations.integerRangeCheck(value);
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
        if (path.matches(".*[א-ת]+.*")) {
            return null;
        }
        return path;
    }

    public static String getStringInput(String msg) {
        Scanner scanner = new Scanner(System.in);
        boolean valid;
        String input = null;
        do {
            try {
                input = scanner.nextLine();
                valid = true;
            } catch (Exception ex) {
                System.out.println("Wrong input - this is not a valid " + msg + ", try again.");
                valid = false;
                scanner.nextLine();
            }
        } while (!valid);

        return input;
    }

    public static void failedLoadSystem(String s) {
        System.out.println("Loading the file ended with a failure:");
        System.out.println(s);
    }

    public static void successLoading() {
        System.out.println("The File loaded successfully!\n");
    }

    public static void targetNameRequest() {
        System.out.println("Please enter a target's name: (to return to the main menu, press '0')");
    }

    public static void showTargetsCount(int totalTargetsNumber) {
        System.out.println("There Are " + totalTargetsNumber + " Targets In The System.\n");
    }

    public static void showTargetCountByType(TargetType targetType, int specificTypeOfTargetsNum) {
        System.out.println("There Are " + specificTypeOfTargetsNum + " " + targetType.name() + " Targets In The System.");
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

    public static void continueApp() {
        System.out.println("Press ENTER to continue ...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static UserInput exit() {
        System.out.println("The System is about the end");
        do {
            System.out.println("Are you sure you want to exit? (Y/N)");
            String answer = getStringInput("answer");
            switch (answer) {
                case "Y":
                    return UserInput.QUIT;
                case "N":
                    return UserInput.INIT;
                default:
                    System.out.println("This is not a valid answer, try again");
            }
        } while (true);
    }
}
