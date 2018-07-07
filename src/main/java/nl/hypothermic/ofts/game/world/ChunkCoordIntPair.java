package nl.hypothermic.ofts.game.world;

import nl.hypothermic.ofts.game.Entity;

public class ChunkCoordIntPair {

    public final int x;
    public final int z;

    public ChunkCoordIntPair(int i, int j) {
        this.x = i;
        this.z = j;
    }

    public static long a(int i, int j) {
        long k = (long) i;
        long l = (long) j;

        return k & 4294967295L | (l & 4294967295L) << 32;
    }

    public int hashCode() {
        long i = a(this.x, this.z);
        int j = (int) i;
        int k = (int) (i >> 32);

        return j ^ k;
    }

    public boolean equals(Object object) {
        ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) object;

        return chunkcoordintpair.x == this.x && chunkcoordintpair.z == this.z;
    }

    public double a(Entity entity) {
        double d0 = (double) (this.x * 16 + 8);
        double d1 = (double) (this.z * 16 + 8);
        double d2 = d0 - entity.locX;
        double d3 = d1 - entity.locZ;

        return d2 * d2 + d3 * d3;
    }

    public int a() {
        return (this.x << 4) + 8;
    }

    public int b() {
        return (this.z << 4) + 8;
    }

    public ChunkPosition a(int i) {
        return new ChunkPosition(this.a(), i, this.b());
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }
}
