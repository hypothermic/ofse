package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet10 extends Packet {
	
	// 10: FLYING
	
	/** The player's X position. */
    public double xPosition;
    /** The player's Y position. */
    public double yPosition;
    /** The player's Z position. */
    public double zPosition;
    /** The player's stance. (boundingBox.minY) */
    public double stance;
    /** The player's yaw rotation. */
    public float yaw;
    /** The player's pitch rotation. */
    public float pitch;
    /** True if the client is on the ground. */
    public boolean onGround;
    /** Boolean set to true if the player is moving. */
    public boolean moving;
    /** Boolean set to true if the player is rotating. */
    public boolean rotating;
	
	public Packet10() {
		super(10);
	}
	
	/** Note: use for subclasses ONLY. */
	public Packet10(int subpacketId) {
		super(subpacketId);
	}
	
	public Packet10(boolean onGround) {
		super(10);
		this.onGround = onGround;
	}
	
	public Packet10(DataInputStream dis) throws IOException {
		super(10);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		onGround = dis.read() != 0;
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.write(onGround ? 1 : 0);
	}

	@Override public int getSize() {
		return 1;
	}
}
