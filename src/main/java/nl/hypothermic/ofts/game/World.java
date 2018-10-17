package nl.hypothermic.ofts.game;

import java.io.IOException;

import nl.hypothermic.fireloader.FireLoader;
import nl.hypothermic.ofts.game.world.WorldData;
import nl.hypothermic.ofts.game.world.generator.WorldGenerator;
import nl.hypothermic.ofts.game.world.loader.Chunk;

public class World {

    private WorldData worldData;
    
    private FireLoader loader;
    
    private WorldGenerator worldGen;
    
    public Chunk getChunk(int x, int z) {
    	Chunk chunk = loader.getChunkAt(x, z);
    	if (chunk != null) {
    		chunk = worldGen.generate(x, z);
    	}
    	return chunk;
    }
    
    public World(FireLoader loader, WorldGenerator worldGen) throws IOException {
    	this.loader = loader;
    	this.worldData = loader.getWorldData();
    	this.worldGen = worldGen;
    }
    
    public WorldData getWorldData() {
    	return this.worldData;
    }

    public long getSeed() {
        return this.worldData.getSeed();
    }

    public long getTime() {
        //return this.worldData.getTime();
    	return this.worldData.getTime();
    }
}
