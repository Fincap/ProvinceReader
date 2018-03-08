package com.zambz.provincereader.io;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class serves as a static output logging tool.
 * The class takes a string to log, then prints it to the console and stores the log into a text file,
 * attaching the debug statement to a timestamp.
 */
public final class Debugger {

	private static boolean enabled = true;

	public static void setEnabled(boolean active) {
		Debugger.enabled = active;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void log(String str){
		if(isEnabled()) {
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd|HH:mm:ss").format(new Date());
			System.out.printf("[%s] %s\n", timeStamp, str);
		}
	}

}
