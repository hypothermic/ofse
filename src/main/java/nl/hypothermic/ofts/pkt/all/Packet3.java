package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet3 extends Packet {

	// 3: CHAT

	public static final byte CHAT_MAX_LEN = 119;

	public String message;

	public Packet3() {
		super(3);
	}
	
	public Packet3(DataInputStream dis) throws IOException {
		super(3);
		this.read(dis);
	}

	public Packet3(String message) {
		super(3);
		if (message.length() > CHAT_MAX_LEN) {
			message = message.substring(0, CHAT_MAX_LEN);
		}

		this.message = message;
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}

	@Override public void read(DataInputStream dis) throws IOException {
		this.message = readString(dis, CHAT_MAX_LEN);
	}

	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		writeString(this.message, dos);
	}

	@Override public int getSize() {
		return 2 + this.message.length() * 2;
	}
}
