package gpup.console.validation;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

public class ConsoleIOValidations {

    public static boolean integerRangeCheck(int value) {
        final int START_RANGE = 1;
        final int END_RANGE = 6;

        return (value >= START_RANGE && value <= END_RANGE);
    }


    public static boolean isQuit(String name) {
        final String QUIT = "QP";
        return name.equals(QUIT);
    }
}
