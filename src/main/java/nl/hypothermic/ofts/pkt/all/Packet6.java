package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet6 extends Packet {
	
	// 6: SPAWN POSITION
	
	public int x; // block x-coord
	public int y; // block y-coord
	public int z; // block z-coord
	
	public Packet6() {
		super(6);
	}
	
	public Packet6(int x, int y, int z) {
		super(6);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Packet6(DataInputStream dis) throws IOException {
		super(6);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		// TODO
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.x = dis.readInt();
        this.y = dis.readInt();
        this.z = dis.readInt();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeInt(this.x);
        dos.writeInt(this.y);
        dos.writeInt(this.z);
	}

	@Override public int getSize() {
		return 12;
	}

	@Override public String toString() {
		return "Packet6 [x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
	}
}
