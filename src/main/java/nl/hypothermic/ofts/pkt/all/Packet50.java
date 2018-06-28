package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet50 extends Packet {
	
	// 50: CHUNK ALLOCATION (load/unload chunk - send 51 directly after this!)
	
	public int x; // chunk x-coord
	public int y; // chunk y-coord
	
	/**
     * If mode is true (1) the client will initialize the chunk. If it is false (0) the client will unload the chunk.
     */
	public boolean mode;

	public Packet50() {
		super(50);
	}
	
	public Packet50(DataInputStream dis) throws IOException {
		this();
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.x = dis.readInt();
        this.y = dis.readInt();
        this.mode = dis.read() != 0;
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeInt(this.x);
        dos.writeInt(this.y);
        dos.write(this.mode ? 1 : 0);
	}

	@Override public int getSize() {
		return 9;
	}
}
