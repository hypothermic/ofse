package nl.hypothermic._cemuV2d;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;

import nl.hypothermic.ofts.pkt.Packet;

public class ClientDebugger {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 25565);
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		OutputStream os = socket.getOutputStream();
		os.write(254);
		if (dis.read() != 255) {
			socket.close();
			throw new RuntimeException("Server did not respond with motd message");
		}
		System.out.println("Server is responding with MOTD: " + Packet.readString(dis, 64));
		socket.close();
	}
}
