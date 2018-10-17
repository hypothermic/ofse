package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.notforge.NotForge;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet53 extends Packet {
	
	// 53: SINGLE BLOCK CHANGE
	
	public int x; // block x-coord
	public byte y; // block y-coord
	public int z; // block z-coord
	public int blockId; // block id (this should be a byte according to the wiki.vg, however we're working with EID)
	public byte meta; // block metadata

	public Packet53() {
		super(53);
	}
	
	public Packet53(int x, int y, int z, int blockId) {
		this();
		this.x = x;
		this.y = (byte) y;
		this.z = z;
		this.blockId = blockId;
	}
	
	public Packet53(int x, int y, int z, int blockId, byte meta) {
		this(x, y, z, blockId);
		this.meta = meta;
	}
	
	public Packet53(DataInputStream dis) throws IOException {
		this();
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.x = dis.readInt();
        this.y = (byte) dis.read();
        this.z = dis.readInt();
        if (NotForge.enable4096) {
        	this.blockId = dis.readInt();
        } else {
        	this.blockId = dis.read();
        }
        this.meta = (byte) dis.read();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeInt(this.x);
        dos.write(this.y);
        dos.writeInt(this.z);
        if (NotForge.enable4096) {
        	dos.writeInt(this.blockId);
        } else {
        	dos.write(this.blockId);
        }
        dos.write(this.meta);
	}

	@Override public int getSize() {
		return 11;
	}

	@Override public String toString() {
		return "Packet53 [x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", blockId=" + this.blockId + ", meta=" + this.meta + "]";
	}
}
