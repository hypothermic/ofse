package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import nl.hypothermic.ofts.game.Explosion;
import nl.hypothermic.ofts.game.world.ChunkPosition;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet60 extends Packet {
	
	// 60: EXPLOSION
	
	public double xPos;
    public double yPos;
    public double zPos;
    public float radius;
    public HashSet<ChunkPosition> blocks;
	
	public Packet60() {
		super(60);
	}
	
	public Packet60(Explosion epl) {
		super(60);
		this.xPos = epl.xPos;
		this.yPos = epl.yPos;
		this.zPos = epl.zPos;
		this.radius = epl.radius;
		this.blocks = epl.blocks;
	}
	
	public Packet60(double xPos, double yPos, double zPos, float radius, HashSet<ChunkPosition> set) {
		super(60);
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.radius = radius;
        this.blocks = new HashSet(set);
    }
	
	public Packet60(DataInputStream dis) throws IOException {
		super(60);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.xPos = dis.readDouble();
        this.yPos = dis.readDouble();
        this.zPos = dis.readDouble();
        this.radius = dis.readFloat();
        int i = dis.readInt();

        this.blocks = new HashSet();
        int j = (int) this.xPos;
        int k = (int) this.yPos;
        int l = (int) this.zPos;

        for (int i1 = 0; i1 < i; ++i1) {
            int j1 = dis.readByte() + j;
            int k1 = dis.readByte() + k;
            int l1 = dis.readByte() + l;

            this.blocks.add(new ChunkPosition(j1, k1, l1));
        }
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(60);
		dos.writeDouble(this.xPos);
        dos.writeDouble(this.yPos);
        dos.writeDouble(this.zPos);
        dos.writeFloat(this.radius);
        dos.writeInt(this.blocks.size());
        int i = (int) this.xPos;
        int j = (int) this.yPos;
        int k = (int) this.zPos;
        
        Iterator iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            ChunkPosition chunkposition = (ChunkPosition) iterator.next();
            dos.writeByte(chunkposition.x - i);
            dos.writeByte( chunkposition.y - j);
            dos.writeByte(chunkposition.z - k);
        }
	}

	@Override public int getSize() {
		return 32 + this.blocks.size() * 3;
	}
}
