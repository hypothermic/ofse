package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet0 extends Packet {
	
	// 0: KEEP ALIVE

	public Packet0() {
		super(0);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		// we *should* be checking if the response matches our last random number we sent,
		// but it doesn't matter much for now. see documentation.
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		// TODO
	}

	@Override public int getSize() {
		return 4;
	}
}
