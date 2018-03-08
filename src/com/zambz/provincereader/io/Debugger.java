package com.zambz.provincereader.io;

/**
 * This class serves as a static output logging tool.
 * The class takes a string to log, then prints it to the console and stores the log into a text file,
 * attaching the debug statement to a timestamp.
 */
public class Debugger {

    private static boolean enabled = true;

    public static void setEnabled(boolean active) {
        Debugger.enabled = active;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void log(String str){
        if(isEnabled()) {
            System.out.println(str);
        }
    }

}
