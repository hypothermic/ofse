package nl.hypothermic.ofts;

import static nl.hypothermic.ofts.util.LoggingManager.*;

public class Launcher {
	
	public static void main(String[] args) {
		empty("");
		empty("<< --- OPENFIRE SERVER EMULATOR --- >>");
		empty("");
		new Server();
	}
}
