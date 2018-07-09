package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet13 extends Packet10 {
	
	// 13: PLAYER LOOK MOVE
	
	public Packet13() {
		super(13);
	}
	
	public Packet13(double xPos, double yPos, double stance, double zPos, float yaw, float pitch, boolean onGround) {
		super(13);
		xPosition = xPos;
        yPosition = yPos;
        this.stance = stance;
        zPosition = zPos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        rotating = true;
        moving = true;
	}
	
	public Packet13(DataInputStream dis) throws IOException {
		super(13);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		xPosition = dis.readDouble();
        yPosition = dis.readDouble();
        stance = dis.readDouble();
        zPosition = dis.readDouble();
        yaw = dis.readFloat();
        pitch = dis.readFloat();
        super.read(dis);
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeDouble(xPosition);
        dos.writeDouble(yPosition);
        dos.writeDouble(stance);
        dos.writeDouble(zPosition);
        dos.writeFloat(yaw);
        dos.writeFloat(pitch);
        super.write(dos);
	}

	@Override public int getSize() {
		return 41;
	}
}
