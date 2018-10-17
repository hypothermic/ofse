package nl.hypothermic.ofts.game.world.generator;

import nl.hypothermic.ofts.game.world.loader.Chunk;

public abstract class WorldGenerator {

	public abstract Chunk generate(int chunkPosX, int chunkPosZ);
	
}
