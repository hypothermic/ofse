package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet255 extends Packet {

	// 255: KICK DISCONNECT

	public String reason;

	public Packet255(String reason) {
		super(255);
		this.reason = reason;
	}

	public Packet255(String motd, int currentPlayers, int maxPlayers) {
		super(255);
		this.reason = motd + "\u00A7" + currentPlayers + "\u00A7" + maxPlayers;
	}

	public Packet255(DataInputStream dis) throws IOException {
		super(255);
		this.read(dis);
	}

	@Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void read(DataInputStream dis) throws IOException {
		// no-op: send-only packet.
	}

	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		writeString(this.reason, dos);
	}

	@Override public int getSize() {
		return this.reason.length();
	}

	@Override public String toString() {
		return "Packet255 [reason=" + this.reason + "]";
	}
}
