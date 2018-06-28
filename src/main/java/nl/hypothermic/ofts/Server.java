package nl.hypothermic.ofts;

import static nl.hypothermic.ofts.util.LoggingManager.*;

import java.util.ArrayList;
import java.util.SplittableRandom;

import nl.hypothermic.ofts.game.Player;

public class Server {
	
	public static int netLocalPort = 25565;
	private NetListenThread nl = new NetListenThread();
	
	public static boolean authOnlineMode = true; // whether to verify usernames or not
	public static String authLoginKey = ""; // can be left empty
	
	public static String motd = "Powered by OpenFire";
	public static int currentPlayers = 69;
	public static int maxPlayers = 420;
	public static int gamemode = 0; // Default gamemode
	public static int dimension = 0; // -1=nether, 0=normal, 1=end
	public static int difficulty = 0; // 0=peaceful, 1=easy, 2=normal, 3=hard
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	public static SplittableRandom random = new SplittableRandom();
	
	public Server() {
		this.init();
	}
	
	private void init() {
		nl.start();
		info("Initiation sequence complete");
	}
}
