package gpup.console.validation;

import gpup.console.app.GPUPConsoleIO;

public class ConsoleIOValidations {

    public static boolean integerRangeCheck(int value) {
        final int START_RANGE = 1;
        final int END_RANGE = 6;

        return (value >= START_RANGE && value <= END_RANGE);
    }


    public static boolean isQuit(String name) {
        final String QUIT = "QP";
        if(name.equals(QUIT))
        {
            GPUPConsoleIO.printMsg("Going back to main menu.");
            return true;
        }
        return false;
    }
}
