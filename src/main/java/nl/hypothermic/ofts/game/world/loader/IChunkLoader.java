package nl.hypothermic.ofts.game.world.loader;

import java.io.IOException;

import nl.hypothermic.ofts.game.World;
import nl.hypothermic.ofts.game.world.WorldData;

public interface IChunkLoader {

    Chunk loadChunk(World world, int i, int j) throws IOException;
    
    WorldData loadWorldData() throws IOException;

    void a(World world, Chunk chunk);

    void b(World world, Chunk chunk);

    void a();

    void b();
}
