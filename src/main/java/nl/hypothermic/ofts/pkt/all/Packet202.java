package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet202 extends Packet {
	
	// 202: PLAYER ABILITIES
	
	public boolean isInvulnerable = false;
    public boolean isFlying = false;
    public boolean canFly = false;
    public boolean canInstantlyBuild = false;

	public Packet202() {
		super(202);
	}
	
	public Packet202(boolean isInvulnerable, boolean isFlying, boolean canFly, boolean canInstantlyBuild) {
		super(202);
		this.isInvulnerable = isInvulnerable;
		this.isFlying = isFlying;
		this.canFly = canFly;
		this.canInstantlyBuild = canInstantlyBuild;
	}
	
	public Packet202(DataInputStream dis) throws IOException {
		super(202);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.isInvulnerable = dis.readBoolean();
        this.isFlying = dis.readBoolean();
        this.canFly = dis.readBoolean();
        this.canInstantlyBuild = dis.readBoolean();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeBoolean(this.isInvulnerable);
        dos.writeBoolean(this.isFlying);
        dos.writeBoolean(this.canFly);
        dos.writeBoolean(this.canInstantlyBuild);
	}

	@Override public int getSize() {
		return 1;
	}

	@Override public String toString() {
		return "Packet202 [isInvulnerable=" + this.isInvulnerable + ", isFlying=" + this.isFlying + ", canFly=" + this.canFly + ", canInstantlyBuild=" + this.canInstantlyBuild + "]";
	}
}
