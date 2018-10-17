package nl.hypothermic._cemuV2d;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import nl.hypothermic.ofts.game.world.WorldType;
import nl.hypothermic.ofts.pkt.Packet;
import nl.hypothermic.ofts.pkt.PacketReader;
import nl.hypothermic.ofts.pkt.all.Packet1;
import nl.hypothermic.ofts.pkt.all.Packet2;
import nl.hypothermic.ofts.util.LoggingManager;

public class ClientDebugger {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 25565);
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		//os.write(254);
		//if (dis.read() != 255) {
		//	socket.close();
		//	throw new RuntimeException("Server did not respond with motd message");
		//}
		//System.out.println("Server is responding with MOTD: " + Packet.readString(dis, 64));
		
		new Packet2("_cemuV2d;localhost:25565").write(dos);
		
		boolean isConnected = true;
		while (isConnected) {
			Packet current = PacketReader.read(dis);
			// assert + ignore -1
			if (current != null && current.id >= 0) {
				LoggingManager.info("cemu rec pkt " + current.toString());
				if (current.id == 1) {
					LoggingManager.info("SUCC ESS");
				} else if (current.id == 2) {
					new Packet1(29, "_cemuV2d").write(dos);
				} else if (current.id == 255) {
					LoggingManager.warn("Kicked --> " + current.toString());
					socket.close();
					isConnected = false;
				}
			}
			System.out.println("----");
		}
	}
}
