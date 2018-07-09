package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet33 extends Packet30 {
	
	// 30: RELATIVE ENTITY MOVE LOOK
	
	public Packet33() {
		super(33);
		rotating = true;
	}
	
	/** Note: use for subclasses ONLY. */
	public Packet33(int subpacketId) {
		super(subpacketId);
	}
	
	public Packet33(boolean onGround) {
		super(30);
	}
	
	public Packet33(DataInputStream dis) throws IOException {
		super(30);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		super.read(dis);
        xPosition = dis.readByte();
        yPosition = dis.readByte();
        zPosition = dis.readByte();
        yaw = dis.readByte();
        pitch = dis.readByte();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		super.write(dos);
        dos.writeByte(xPosition);
        dos.writeByte(yPosition);
        dos.writeByte(zPosition);
        dos.writeByte(yaw);
        dos.writeByte(pitch);
	}

	@Override public int getSize() {
		return 9;
	}
}
