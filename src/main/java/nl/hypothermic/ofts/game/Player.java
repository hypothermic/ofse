package nl.hypothermic.ofts.game;

import java.util.Iterator;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.massdata.MassPlayerData;
import nl.hypothermic.ofts.nbt.NBTTagCompound;
import nl.hypothermic.ofts.nbt.NBTTagList;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;
import nl.hypothermic.ofts.util.LoggingManager;

public class Player extends Entity {
	
	private final AcceptedConnection conn;
	public String username;
	public int entityId;
	public final boolean isVerified; // if player verification to Mojang servers is completed.
	
	public short gamemode; // from int to short
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
		if (MassPlayerData.getInstance().isPlayerRegistered(username)) {
			// existing player!!
			LoggingManager.info("Reg player");
			Player tmp = MassPlayerData.getInstance().loadAttributes(this);
			this.gamemode = tmp.gamemode;
			this.health = tmp.gamemode;
			this.locX = tmp.locX;
			this.locY = tmp.locY;
			this.locZ = tmp.locZ;
			this.yaw = tmp.yaw;
			this.pitch = tmp.pitch;
		} else {
			// new player!!
			LoggingManager.info("New player");
			this.locX = Server.world.getWorldData().getSpawnX();
			this.locY = Server.world.getWorldData().getSpawnY();
			this.locZ = Server.world.getWorldData().getSpawnZ();
		}
	}

	@Override protected void fromNbt(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		;
	}

	@Override protected void toNbt(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		;
	}
	
	public void sendPacket(Packet packet) {
		conn.queuePacket(packet);
	}
}
