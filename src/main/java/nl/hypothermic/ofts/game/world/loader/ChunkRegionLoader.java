package nl.hypothermic.ofts.game.world.loader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.hypothermic.ofts.game.Entity;
import nl.hypothermic.ofts.game.TileEntity;
import nl.hypothermic.ofts.game.World;
import nl.hypothermic.ofts.game.entity.EntityTypes;
import nl.hypothermic.ofts.game.world.ChunkCoordIntPair;
import nl.hypothermic.ofts.game.world.ChunkSection;
import nl.hypothermic.ofts.game.world.NibbleArray;
import nl.hypothermic.ofts.game.world.WorldData;
import nl.hypothermic.ofts.nbt.NBTCompressedStreamTools;
import nl.hypothermic.ofts.nbt.NBTTagCompound;
import nl.hypothermic.ofts.nbt.NBTTagList;
import nl.hypothermic.ofts.util.LoggingManager;

public class ChunkRegionLoader implements IChunkLoader {

    private List a = new ArrayList();
    private Set b = new HashSet();
    private Object c = new Object();
    private final File worldDir;

    public ChunkRegionLoader(File file1) {
        this.worldDir = file1;
    }

    public Chunk loadChunk(World world, int i, int j) throws IOException {
        NBTTagCompound nbttagcompound = null;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
        Object object = this.c;

        synchronized (this.c) {
            if (this.b.contains(chunkcoordintpair)) {
                for (int k = 0; k < this.a.size(); ++k) {
                    if (((PendingChunkToSave) this.a.get(k)).a.equals(chunkcoordintpair)) {
                        nbttagcompound = ((PendingChunkToSave) this.a.get(k)).b;
                        break;
                    }
                }
            }
        }
        
        if (nbttagcompound == null) {
            DataInputStream datainputstream = RegionFileCache.b(this.worldDir, i, j);

            if (datainputstream == null) {
                return null;
            }

            nbttagcompound = NBTCompressedStreamTools.read((DataInput) datainputstream);
        }

        return this.a(world, i, j, nbttagcompound);
    }
    
    public WorldData loadWorldData() throws IOException {
    	File file1 = new File(this.worldDir, "level.dat");
    	NBTTagCompound nbttagcompound = NBTCompressedStreamTools.readCompressed((InputStream) (new FileInputStream(file1)));
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
        return new WorldData(nbttagcompound1);
    }

    protected Chunk a(World world, int i, int j, NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("Level")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
            return null;
        } else if (!nbttagcompound.getCompoundTag("Level").hasKey("Sections")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing block data, skipping");
            return null;
        } else {
            Chunk chunk = this.a(world, nbttagcompound.getCompoundTag("Level"));

            if (!chunk.isAtLocation(i, j)) {
                System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
                nbttagcompound.setInteger("xPos", i);
                nbttagcompound.setInteger("zPos", j);
                chunk = this.a(world, nbttagcompound.getCompoundTag("Level"));
            }

            chunk.removeUnknownBlocks();
            return chunk;
        }
    }

    public void a(World world, Chunk chunk) {
        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.setTag("Level", nbttagcompound1);
            this.a(chunk, world, nbttagcompound1);
            this.a(chunk.getChunkCoordIntPair(), nbttagcompound);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void a(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
        Object object = this.c;

        synchronized (this.c) {
            if (this.b.contains(chunkcoordintpair)) {
                for (int i = 0; i < this.a.size(); ++i) {
                    if (((PendingChunkToSave) this.a.get(i)).a.equals(chunkcoordintpair)) {
                        this.a.set(i, new PendingChunkToSave(chunkcoordintpair, nbttagcompound));
                        return;
                    }
                }
            }

            this.a.add(new PendingChunkToSave(chunkcoordintpair, nbttagcompound));
            this.b.add(chunkcoordintpair);
            FileIOThread.a.a(this);
        }
    }

    public boolean c() {
        PendingChunkToSave pendingchunktosave = null;
        Object object = this.c;

        synchronized (this.c) {
            if (this.a.size() <= 0) {
                return false;
            }

            pendingchunktosave = (PendingChunkToSave) this.a.remove(0);
            this.b.remove(pendingchunktosave.a);
        }

        if (pendingchunktosave != null) {
            try {
                this.a(pendingchunktosave);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    private void a(PendingChunkToSave pendingchunktosave) throws IOException {
        DataOutputStream dataoutputstream = RegionFileCache.c(this.worldDir, pendingchunktosave.a.x, pendingchunktosave.a.z);

        NBTCompressedStreamTools.write(pendingchunktosave.b, (DataOutput) dataoutputstream);
        try {
			dataoutputstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void b(World world, Chunk chunk) {}

    public void a() {}

    public void b() {}

    private void a(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("xPos", chunk.xPosition);
        nbttagcompound.setInteger("zPos", chunk.zPosition);
        nbttagcompound.setLong("LastUpdate", world.getWorldData().getTime());
        nbttagcompound.setIntArray("HeightMap", chunk.heightMap);
        nbttagcompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated);
        ChunkSection[] achunksection = chunk.storageArrays;
        NBTTagList nbttaglist = new NBTTagList("Sections");
        ChunkSection[] achunksection1 = achunksection;
        int i = achunksection.length;

        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j) {
            ChunkSection chunksection = achunksection1[j];

            if (chunksection != null && chunksection.getBlockRefCount() != 0) { // ofts: not sure if .getBlockRefCount() is correct. Original: ".f()"
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte) (chunksection.getYLocation() >> 4 & 255));
                nbttagcompound1.setByteArray("Blocks", chunksection.getBlockLSBArray());
                if (chunksection.getBlockMSBArray() != null) {
                    nbttagcompound1.setByteArray("Add", chunksection.getBlockMSBArray().data);
                }

                nbttagcompound1.setByteArray("Data", chunksection.getBlockMetadataArray().data);
                nbttagcompound1.setByteArray("SkyLight", chunksection.getSkylightArray().data);
                nbttagcompound1.setByteArray("BlockLight", chunksection.getBlocklightArray().data);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Sections", nbttaglist);
        nbttagcompound.setByteArray("Biomes", chunk.getBiomeArray());
        chunk.hasEntities = false;
        NBTTagList nbttaglist1 = new NBTTagList();

        Iterator iterator;

        for (i = 0; i < chunk.entityLists.length; ++i) {
            iterator = chunk.entityLists[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                chunk.hasEntities = true;
                nbttagcompound1 = new NBTTagCompound();
                if (entity.matchesNbtData(nbttagcompound1)) {
                    nbttaglist1.appendTag(nbttagcompound1);
                }
            }
        }

        nbttagcompound.setTag("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        iterator = chunk.chunkTileEntityMap.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            tileentity.b(nbttagcompound1);
            nbttaglist2.appendTag(nbttagcompound1);
        }

        // OFTS: tile entities not supported yet.
        
        /*nbttagcompound.setTag("TileEntities", nbttaglist2);
        List list = world.a(chunk, false);

        if (list != null) {
            long k = world.getWorldData().getTime();
            NBTTagList nbttaglist3 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                nbttagcompound2.setInteger("i", nextticklistentry.d);
                nbttagcompound2.setInteger("x", nextticklistentry.a);
                nbttagcompound2.setInteger("y", nextticklistentry.b);
                nbttagcompound2.setInteger("z", nextticklistentry.c);
                nbttagcompound2.setInteger("t", (int) (nextticklistentry.e - k));
                nbttaglist3.appendTag(nbttagcompound2);
            }

            nbttagcompound.setTag("TileTicks", nbttaglist3);
        }*/
    }

    private Chunk a(World world, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInteger("xPos");
        int j = nbttagcompound.getInteger("zPos");
        Chunk chunk = new Chunk(i, j);

        chunk.heightMap = nbttagcompound.getIntArray("HeightMap");
        chunk.isTerrainPopulated = nbttagcompound.getBoolean("TerrainPopulated");
        NBTTagList nbttaglist = nbttagcompound.getTagList("Sections");
        byte b0 = 16;
        ChunkSection[] achunksection = new ChunkSection[b0];

        for (int k = 0; k < nbttaglist.tagCount(); ++k) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(k);
            byte b1 = nbttagcompound1.getByte("Y");
            ChunkSection chunksection = new ChunkSection(b1 << 4);

            chunksection.setBlockLSBArray(nbttagcompound1.getByteArray("Blocks"));
            if (nbttagcompound1.hasKey("Add")) {
                chunksection.setBlockMSBArray(new NibbleArray(nbttagcompound1.getByteArray("Add"), 4));
            }

            chunksection.setBlockMetadataArray(new NibbleArray(nbttagcompound1.getByteArray("Data"), 4));
            chunksection.setSkylightArray(new NibbleArray(nbttagcompound1.getByteArray("SkyLight"), 4));
            chunksection.setBlocklightArray(new NibbleArray(nbttagcompound1.getByteArray("BlockLight"), 4));
            chunksection.update();
            achunksection[b1] = chunksection;
        }

        chunk.setStorageArrays(achunksection);
        if (nbttagcompound.hasKey("Biomes")) {
            chunk.setBiomeArray(nbttagcompound.getByteArray("Biomes"));
        }

        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Entities");

        if (nbttaglist1 != null) {
            for (int l = 0; l < nbttaglist1.tagCount(); ++l) {
                NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist1.tagAt(l);
                Entity entity = EntityTypes.a(nbttagcompound2);

                chunk.hasEntities = true;
                if (entity != null) {
                    chunk.addEntity(entity);
                }
            }
        }

        // OFTS: tile entities not supported yet.
        
        /*NBTTagList nbttaglist2 = nbttagcompound.getTagList("TileEntities");

        if (nbttaglist2 != null) {
            for (int i1 = 0; i1 < nbttaglist2.tagCount(); ++i1) {
                NBTTagCompound nbttagcompound3 = (NBTTagCompound) nbttaglist2.tagAt(i1);
                TileEntity tileentity = TileEntity.c(nbttagcompound3);

                if (tileentity != null) {
                    chunk.a(tileentity);
                }
            }
        }

        if (nbttagcompound.hasKey("TileTicks")) {
            NBTTagList nbttaglist3 = nbttagcompound.getTagList("TileTicks");

            if (nbttaglist3 != null) {
                for (int j1 = 0; j1 < nbttaglist3.tagCount(); ++j1) {
                    NBTTagCompound nbttagcompound4 = (NBTTagCompound) nbttaglist3.tagAt(j1);

                    world.d(nbttagcompound4.getInteger("x"), nbttagcompound4.getInteger("y"), nbttagcompound4.getInteger("z"), nbttagcompound4.getInteger("i"), nbttagcompound4.getInteger("t"));
                }
            }
        }*/

        return chunk;
    }
}
