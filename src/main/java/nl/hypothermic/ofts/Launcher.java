package nl.hypothermic.ofts;

import static nl.hypothermic.ofts.util.LoggingManager.*;

import java.io.File;

public class Launcher {
	
	public static void main(String[] args) {
		new File("data/player/").mkdirs();
		empty("");
		empty("<< --- OPENFIRE SERVER EMULATOR --- >>");
		empty("");
		new Server();
	}
}
