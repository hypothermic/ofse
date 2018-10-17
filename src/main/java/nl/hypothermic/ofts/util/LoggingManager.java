package nl.hypothermic.ofts.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingManager {
	
	private static final DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static Date date;
	
	// This whole class is just a "placeholder" for a future GUI, so we don't have to reroute logging anymore then.
	
	private static void write(String dat) {
		date = new Date();
		//System.out.println(sdf.format(date) + dat);
	}
	
	public static void empty(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Send an info message
	 * @param msg Message to show the user
	 */
	public static void info(String msg) {
		write(" | INFO | " + msg);
	}
	
	/**
	 * Send a warning message
	 * @param msg Message to show the user
	 */
	public static void warn(String msg) {
		write(" | WARN | " + msg);
	}
	
	/**
	 * Send an error message
	 * @param msg Message to show the user
	 */
	public static void err(String msg) {
		write(" | ERROR | " + msg);
	}
	
	/**
	 * Abruptly exit with a final message.
	 * @param msg Message to show the user
	 */
	public static void crit(String msg) {
		write(" | -------------------------------------------");
		write(" |  A critical error has occured: " + msg);
		write(" |  OpenFire Server Emulator will now exit.");
		write(" | -------------------------------------------");
		System.exit(1);
	}
}
