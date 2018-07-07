package nl.hypothermic.ofts.game.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldChunkManagerHell extends WorldChunkManager {

    private Biome a;
    private float b;
    private float c;

    public WorldChunkManagerHell(Biome biomebase, float f, float f1) {
        this.a = biomebase;
        this.b = f;
        this.c = f1;
    }

    public Biome getBiome(int i, int j) {
        return this.a;
    }

    public Biome[] getBiomes(Biome[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        Arrays.fill(abiomebase, 0, k * l, this.a);
        return abiomebase;
    }

    public float[] getTemperatures(float[] afloat, int i, int j, int k, int l) {
        if (afloat == null || afloat.length < k * l) {
            afloat = new float[k * l];
        }

        Arrays.fill(afloat, 0, k * l, this.b);
        return afloat;
    }

    public float[] getWetness(float[] afloat, int i, int j, int k, int l) {
        if (afloat == null || afloat.length < k * l) {
            afloat = new float[k * l];
        }

        Arrays.fill(afloat, 0, k * l, this.c);
        return afloat;
    }

    public Biome[] getBiomeBlock(Biome[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        Arrays.fill(abiomebase, 0, k * l, this.a);
        return abiomebase;
    }

    public Biome[] a(Biome[] abiomebase, int i, int j, int k, int l, boolean flag) {
        return this.getBiomeBlock(abiomebase, i, j, k, l);
    }

    public ChunkPosition a(int i, int j, int k, List list, Random random) {
        return list.contains(this.a) ? new ChunkPosition(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1)) : null;
    }

    public boolean a(int i, int j, int k, List list) {
        return list.contains(this.a);
    }
}
