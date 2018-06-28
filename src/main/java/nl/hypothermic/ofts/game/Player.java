package nl.hypothermic.ofts.game;

import nl.hypothermic.ofts.net.AcceptedConnection;

public class Player implements Entity {
	
	private final AcceptedConnection conn;
	public final String username;
	public final int entityId;
	public final boolean isVerified; // if player verification to Mojang servers is completed.
	
	public int gamemode;
	
	/**
	 * Notice: Constructing players is for internal use only.
	 */
	public Player(AcceptedConnection conn, String username, boolean isVerified) {
		this.conn = conn;
		this.username = username;
		this.entityId = EntityTracker.instance.registerEntity(this);
		this.isVerified = isVerified;
	}
}
