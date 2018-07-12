package nl.hypothermic.ofts.game.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.hypothermic.ofts.game.World;
import nl.hypothermic.ofts.game.world.biome.BiomeCache;
import nl.hypothermic.ofts.util.IntCache;

public class WorldChunkManager {

	private BiomeCache c;
	private List d;

	protected WorldChunkManager() {
		this.c = new BiomeCache(this);
		this.d = new ArrayList();
		this.d.add(Biome.FOREST);
		this.d.add(Biome.PLAINS);
		this.d.add(Biome.TAIGA);
		this.d.add(Biome.TAIGA_HILLS);
		this.d.add(Biome.FOREST_HILLS);
		this.d.add(Biome.JUNGLE);
		this.d.add(Biome.JUNGLE_HILLS);
	}

	public WorldChunkManager(long i, WorldType worldtype) {
		this();
	}

	public WorldChunkManager(World world) {
		this(world.getWorldData().getSeed(), world.getWorldData().getType());
	}

	public List a() {
		return this.d;
	}

	public Biome getBiome(int i, int j) {
		return this.c.b(i, j);
	}

	public void b() {
		this.c.a();
	}
}