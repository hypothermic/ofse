package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet8 extends Packet {

	// 8: UPDATE HEALTH

	public int healthMP;
	public int food;

	public float foodSaturation;

	public Packet8() {
		super(8);
	}

	public Packet8(int health, int food, float foodSaturation) {
		super(8);
		this.healthMP = health;
		this.food = food;
		this.foodSaturation = foodSaturation;
	}
	
	public Packet8(DataInputStream dis) throws IOException {
		super(8);
		this.read(dis);
	}

	@Override public Packet react(AcceptedConnection ac) {
		// TODO
		return null;
	}

	@Override public void read(DataInputStream dis) throws IOException {
		healthMP = dis.readShort();
		food = dis.readShort();
		foodSaturation = dis.readFloat();
	}

	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeShort(healthMP);
		dos.writeShort(food);
		dos.writeFloat(foodSaturation);
	}

	@Override public int getSize() {
		return 8;
	}
}
