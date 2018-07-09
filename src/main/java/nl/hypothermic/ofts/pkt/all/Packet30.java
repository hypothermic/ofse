package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet30 extends Packet {
	
	// 30: ENTITY
	
	/** The ID of this entity. */
    public int entityId;
    /** The X axis relative movement. */
    public byte xPosition;
    /** The Y axis relative movement. */
    public byte yPosition;
    /** The Z axis relative movement. */
    public byte zPosition;
    /** The X axis rotation. */
    public byte yaw;
    /** The Y axis rotation. */
    public byte pitch;
    /** Boolean set to true if the entity is rotating. */
    public boolean rotating;
	
	public Packet30() {
		super(30);
		rotating = false;
	}
	
	/** Note: use for subclasses ONLY. */
	public Packet30(int subpacketId) {
		super(subpacketId);
	}
	
	public Packet30(boolean onGround) {
		super(30);
	}
	
	public Packet30(DataInputStream dis) throws IOException {
		super(30);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		entityId = dis.readInt();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeInt(entityId);
	}

	@Override public int getSize() {
		return 4;
	}
}
