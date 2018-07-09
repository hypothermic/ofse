package nl.hypothermic.ofts.massdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import nl.hypothermic.ofts.game.Player;
import nl.hypothermic.ofts.util.MathHelper;

public class MassPlayerData {

	private static final MassPlayerData instance = new MassPlayerData();

	public static final MassPlayerData getInstance() {
		return instance;
	}

	public static final File folder = new File("data/player/");

	private MassPlayerData() {
		/*if (!folder.exists()) {
			folder.mkdirs();
		}*/
	}

	// ====================================
	// OpenFire Player Data Format v1.0.0
	// don't worry: we'll have SQL in the future, but this is easier for now.
	//
	// ./data/player/<username>.opd
	//
	// [str] username § [double] locX § [double] locY § [double] locZ § [int] yaw § [int] pitch § [short] health § [int] gamemode
	// ====================================

	public boolean isPlayerRegistered(String username) {
		return new File(folder, username + ".opd").exists();
	}

	public boolean isPlayerRegistered(Player player) {
		return this.isPlayerRegistered(player.username);
	}

	public void registerPlayer(Player player) throws IOException {
		File f = new File(folder, player.username + ".opd");
		f.createNewFile();
		FileWriter fw = new FileWriter(f.getAbsoluteFile(), false);
		fw.write(player.username + "§" + player.locX + "§" + player.locY + "§" + player.locZ + "§" + MathHelper.floor(player.yaw) + "§" + MathHelper.floor(player.pitch) + "§" + player.health + "§" + player.gamemode);
		fw.close();
	}

	public Player loadAttributes(Player player) {
		File f = new File(folder, player.username + ".opd");
		try {
			BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
			String read = br.readLine();
			if (read != null) {
				String[] data = read.split("§");
				if (data.length < 8) {
					System.out.println("MassPlayerData: not enough arguments in file for " + player.username);
					br.close();
					return player;
				}
				player.username = data[0];
				player.locX = Integer.parseInt(data[1]);
				player.locY = Integer.parseInt(data[2]);
				player.locZ = Integer.parseInt(data[3]);
				player.yaw = Integer.parseInt(data[4]);
				player.pitch = Integer.parseInt(data[5]);
				player.health = Short.parseShort(data[6]);
				player.gamemode = Short.parseShort(data[7]);
			}
			br.close();
		} catch (IOException x) {
			x.printStackTrace();
			return player;
		}

		return player;
	}
}
