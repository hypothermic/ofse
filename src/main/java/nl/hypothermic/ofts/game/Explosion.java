package nl.hypothermic.ofts.game;

import java.util.HashSet;

import nl.hypothermic.ofts.game.world.ChunkPosition;

public class Explosion {
	
	public double xPos, yPos, zPos;
	public float radius;
	public HashSet<ChunkPosition> blocks = new HashSet<ChunkPosition>();
	
	public Explosion(int xPos, int yPos, int zPos, float radius) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.radius = radius;
		// TODO
		this.blocks.add(new ChunkPosition(0, 0, 0));
	}
	
	public void explode(boolean destroyLandscape) {
		// TODO
	}
}
