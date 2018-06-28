package nl.hypothermic.ofts.game;

import java.util.ArrayList;
import java.util.Random;

public class Block {

	public static final Block[] byId = new Block[4096];

	public boolean needsRandomTick = false;

	public int x, y ,z;

	public final int id;
	public float hardness;
	public float resistance;

	public Block(int id, int x, int y, int z) {
		if (byId[id] == null) {
			byId[id] = this;
			this.id = id;
			this.x = x; this.y = y; this.z = z;
		} else {
			throw new IllegalArgumentException("Slot " + id + " is already occupied by " + byId[id]);
		}
	}
}