package nl.hypothermic.ofts.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javafx.scene.paint.Material;
import nl.hypothermic.ofts.game.world.Biome;
import nl.hypothermic.ofts.game.world.Chunk;
import nl.hypothermic.ofts.game.world.ChunkCoordIntPair;
import nl.hypothermic.ofts.game.world.ChunkPosition;
import nl.hypothermic.ofts.game.world.ChunkSection;
import nl.hypothermic.ofts.game.world.WorldChunkManager;
import nl.hypothermic.ofts.game.world.WorldData;
import nl.hypothermic.ofts.game.world.loader.IChunkLoader;
import nl.hypothermic.ofts.util.MathHelper;
import nl.hypothermic.ofts.util.NextTickListEntry;
import nl.hypothermic.ofts.util.Vec3D;

public class World {

    private WorldData worldData;
    
    private IChunkLoader loader;
    private Chunk chunks; // TODO
    
    public Chunk getChunk(int x, int z) {
    	return chunks; // TODO
    }
    
    public World(IChunkLoader loader) throws IOException {
    	this.loader = loader;
    	chunks = loader.loadChunk(this, 1, 1);
    }
    
    public WorldData getWorldData() {
    	return this.worldData;
    }

    public long getSeed() {
        return this.worldData.getSeed();
    }

    public long getTime() {
        return this.worldData.getTime();
    }
}
