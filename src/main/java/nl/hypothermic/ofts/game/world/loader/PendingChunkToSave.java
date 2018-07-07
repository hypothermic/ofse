package nl.hypothermic.ofts.game.world.loader;

import nl.hypothermic.ofts.game.world.ChunkCoordIntPair;
import nl.hypothermic.ofts.nbt.NBTTagCompound;

class PendingChunkToSave {

    public final ChunkCoordIntPair a;
    public final NBTTagCompound b;

    public PendingChunkToSave(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
        this.a = chunkcoordintpair;
        this.b = nbttagcompound;
    }
}
