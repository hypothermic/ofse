package nl.hypothermic.ofts.game;

import java.util.Iterator;

import nl.hypothermic.ofts.nbt.NBTTagCompound;
import nl.hypothermic.ofts.nbt.NBTTagList;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Player extends Entity {
	
	private final AcceptedConnection conn;
	public final String username;
	public final int entityId;
	public final boolean isVerified; // if player verification to Mojang servers is completed.
	
	public int gamemode;
	public short health;
	
	/**
	 * Notice: Constructing players is for internal use only.
	 */
	public Player(AcceptedConnection conn, String username, boolean isVerified) {
		super();
		this.conn = conn;
		this.username = username;
		this.entityId = EntityTracker.instance.registerEntity(this);
		this.isVerified = isVerified;
	}

	@Override protected void fromNbt(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		
	}

	@Override protected void toNbt(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		
	}
	
	public void sendPacket(Packet packet) {
		conn.queuePacket(packet);
	}
}
