package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet4 extends Packet {
	
	// 4: UPDATE TIME
	
	public long time;
	
	public Packet4() {
		super(4);
	}
	
	public Packet4(long time) {
		super(4);
		this.time = time;
	}
	
	public Packet4(DataInputStream dis) throws IOException {
		super(4);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		time = dis.readLong();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeLong(time);
	}

	@Override public int getSize() {
		return 8;
	}

	@Override public String toString() {
		return "Packet4 [time=" + this.time + "]";
	}
}
