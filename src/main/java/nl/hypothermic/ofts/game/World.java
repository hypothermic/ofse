package nl.hypothermic.ofts.game;

import java.io.IOException;

import nl.hypothermic.fireloader.FireLoader;
import nl.hypothermic.ofts.game.world.Chunk;
import nl.hypothermic.ofts.game.world.WorldData;

public class World {

    private WorldData worldData;
    
    private FireLoader loader;
    
    public Chunk getChunk(int x, int z) {
    	return loader.getChunkAt(x, z);
    }
    
    public World(FireLoader loader) throws IOException {
    	this.loader = loader;
    	this.worldData = loader.getWorldData();
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
