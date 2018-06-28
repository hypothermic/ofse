package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet254 extends Packet {
	
	// 254: SERVER INFO RQ

	public Packet254() {
		super(254);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		//return new Packet255(Server.motd + "\u00A769\u00A7420");
		return new Packet255(Server.motd, Server.currentPlayers, Server.maxPlayers);
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		// no-op: empty id-only packet
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
	}

	@Override public int getSize() {
		return 0;
	}
}
