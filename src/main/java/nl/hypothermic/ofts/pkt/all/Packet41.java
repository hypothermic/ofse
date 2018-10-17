package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet41 extends Packet {
	
	// 41: ENTITY EFFECT
	
	public int entityId;
    public byte effectId;

    /** amplifier */
    public byte effectAmp;
    public short duration;
	
	public Packet41() {
		super(41);
	}
	
	/** Note: use for subclasses ONLY. */
	public Packet41(int subpacketId) {
		super(subpacketId);
	}
	
	public Packet41(DataInputStream dis) throws IOException {
		super(41);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		entityId = dis.readInt();
        effectId = dis.readByte();
        effectAmp = dis.readByte();
        duration = dis.readShort();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeInt(entityId);
        dos.writeByte(effectId);
        dos.writeByte(effectAmp);
        dos.writeShort(duration);
	}

	@Override public int getSize() {
		return 8;
	}
}
