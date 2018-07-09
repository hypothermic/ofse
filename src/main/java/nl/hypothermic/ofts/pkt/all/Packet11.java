package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet11 extends Packet10 {
	
	// 11: PLAYER POSITION
	
	public Packet11() {
		super(11);
		this.moving = true;
	}
	
	public Packet11(DataInputStream dis) throws IOException {
		super(11);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.xPosition = dis.readDouble();
		this.yPosition = dis.readDouble();
        this.stance = dis.readDouble();
        this.zPosition = dis.readDouble();
        super.read(dis);
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.writeDouble(this.xPosition);
		dos.writeDouble(this.yPosition);
        dos.writeDouble(this.stance);
        dos.writeDouble(this.zPosition);
        super.write(dos);
	}

	@Override public int getSize() {
		return 33;
	}
}
