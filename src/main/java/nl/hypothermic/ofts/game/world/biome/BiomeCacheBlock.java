package nl.hypothermic.ofts.game.world.biome;

import nl.hypothermic.ofts.game.world.Biome;

public class BiomeCacheBlock {

    public float[] a;
    public float[] b;
    public Biome[] c;
    public int d;
    public int e;
    public long f;

    final BiomeCache g;

    public BiomeCacheBlock(BiomeCache biomecache, int i, int j) {
        this.g = biomecache;
        this.a = new float[256];
        this.b = new float[256];
        this.c = new Biome[256];
        this.d = i;
        this.e = j;
    }

    public Biome a(int i, int j) {
        return this.c[i & 15 | (j & 15) << 4];
    }
}
