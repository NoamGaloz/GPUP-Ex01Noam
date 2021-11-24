package gpup.console.app;

import gpup.components.target.TargetType;
import gpup.components.task.simulation.ProcessingTimeType;
import gpup.console.validation.ConsoleIOValidations;
import gpup.dto.TargetDTO;
import sun.security.pkcs11.wrapper.CK_SSL3_KEY_MAT_OUT;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class GPUPConsoleIO  {

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


































































































    public static int getProccesingTime() {
        Scanner scanner = new Scanner(System.in);
        int processingTime=0;
        boolean validInput=false;

        while(!validInput) {
            try{
            System.out.println("Please Enter The Task Processing Time (for single target) in ms : ");
            processingTime = scanner.nextInt();
                validInput=true;
        }
            catch(Exception e){
                System.out.println("Wrong Input - you must enter an integer. ");
                validInput=false;
            }
        }

        return  processingTime;
    }

    public static int getOptionsInfo(String msg,String firstOpt, String secondOpt) {
        Scanner scanner = new Scanner(System.in);
        int processingTime=0;
        boolean validInput=false;

        while(!validInput) {
            try{
                System.out.println(msg);
                System.out.println(firstOpt);
                System.out.println(secondOpt);
                System.out.println("Please Enter your Choise: ( 1 or 2 )");
                processingTime = scanner.nextInt();
                validInput=true;
            }
            catch(Exception e){
                System.out.println("Wrong Input - you must enter an integer. ");
            }
            if(validInput){
                validInput = processingTime==2 | processingTime==1 ? true : false;
                
                if(!validInput)
                     System.out.println("Wrong Input - you must enter 1 or 2. ");
            }
        }

        return  processingTime;
    }

    public static float getTargetCompleteProb(String msg) {
        Scanner scanner = new Scanner(System.in);
        float succesProb=0;
        boolean validInput=false;

        while(!validInput) {
            try{
                System.out.println(msg);
                System.out.println("A floating point number between 0 to 1");
                succesProb = scanner.nextFloat();
                validInput=true;
            }
            catch(Exception e){
                System.out.println("Wrong Input - you must enter a floating point number. ");
            }
            if(validInput){
                validInput = succesProb>=0&&succesProb<=1 ? true : false;

                if(!validInput)
                    System.out.println("Wrong Input - you must enter a number between 0 to 1. ");
            }
        }

        return  succesProb;
    }
}
