package nl.hypothermic.fireloader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import nl.hypothermic.ofts.game.Entity;
import nl.hypothermic.ofts.game.entity.EntityTypes;
import nl.hypothermic.ofts.game.world.ChunkSection;
import nl.hypothermic.ofts.game.world.NibbleArray;
import nl.hypothermic.ofts.game.world.WorldData;
import nl.hypothermic.ofts.game.world.loader.Chunk;
import nl.hypothermic.ofts.nbt.NBTCompressedStreamTools;
import nl.hypothermic.ofts.nbt.NBTTagCompound;
import nl.hypothermic.ofts.nbt.NBTTagList;

/**==========================================**\
    FireLoader - quickly load Anvil worlds.
            v1.0.0 - 12/07/2018
          
    Copyright (c) admin@hypothermic.nl, 2018
     with significant parts made by others
   which were released under the public domain
\**==========================================**/

public class FireLoader {
	
	private final File dirBase;
	private final File dirNether;
	private final File dirEnd;
	private final File dirRegion;
	private final File fileLevelData;
	
	private final ArrayList<RegionFile> regionFiles = new ArrayList<RegionFile>();
	
	// ========= PUBLIC METHODS ========= //

	public FireLoader(File worldDir) {
		this.dirBase = worldDir;
		this.fileLevelData = new File(this.dirBase, "level.dat");
		this.dirNether = new File(this.dirBase, "DIM-1/");
		this.dirEnd = new File(this.dirBase, "DIM1/");
		this.dirRegion = new File(this.dirBase, "region/");
	}

	public WorldData getWorldData() throws IOException {
		NBTTagCompound nbttagcompound = NBTCompressedStreamTools.readCompressed((InputStream) (new FileInputStream(fileLevelData)));
		return new WorldData(nbttagcompound.getCompoundTag("Data"));
	}
	
	public boolean hasChunkAt(int xPos, int zPos) {
		return getRegionFile(xPos, zPos).hasChunk(xPos, zPos);
	}
	
	public Chunk getChunkAt(int xPos, int zPos) {
		try {
			return loadChunkWithCache(xPos, zPos);
		} catch (IOException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
			return null;
		}
	}

	// ===== FUNKY AND COMPLEX STUFF ===== //

	private Chunk loadChunkWithCache(int i, int j) throws IOException {
		NBTTagCompound nbttagcompound = null;

		//DataInputStream datainputstream = RegionFileCache.b(this.dirBase, i, j);
		DataInputStream datainputstream = getRegionFile(i, j).getChunkDataInputStream(i, j);

		if (datainputstream == null) {
			System.out.println("No region file for chunk at [" + i + ", " + j + "]");
			return null;
		}

		nbttagcompound = NBTCompressedStreamTools.read((DataInput) datainputstream);

		return this.loadChunkFromNbt(i, j, nbttagcompound);
	}
	
	private RegionFile getRegionFile(int i, int j) {
		RegionFile rf = new RegionFile(new File(dirRegion, "r." + (i >> 5) + "." + (j >> 5) + ".mca"));
		regionFiles.add(rf);
		return rf;
	}

	private Chunk loadChunkFromNbt(int i, int j, NBTTagCompound nbttagcompound) {
		if (!nbttagcompound.hasKey("Level")) {
			System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
			return null;
		} else if (!nbttagcompound.getCompoundTag("Level").hasKey("Sections")) {
			System.out.println("Chunk file at " + i + "," + j + " is missing block data, skipping");
			return null;
		} else {
			Chunk chunk = this.loadChunkFromNbt(nbttagcompound.getCompoundTag("Level"));

			if ((chunk.xPosition != i) || (chunk.zPosition != j)) {
				System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
				nbttagcompound.getCompoundTag("Level").setInteger("xPos", i); // CraftBukkit - .getCompound("Level")
				nbttagcompound.getCompoundTag("Level").setInteger("zPos", j); // CraftBukkit - .getCompound("Level")
				chunk = this.loadChunkFromNbt(nbttagcompound.getCompoundTag("Level"));
			}

			return chunk;
		}
	}

	private Chunk loadChunkFromNbt(NBTTagCompound nbttagcompound) {
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

		// TODO
		/*
		 * NBTTagList nbttaglist2 = nbttagcompound.getTagList("TileEntities");
		 * 
		 * if (nbttaglist2 != null) { for (int i1 = 0; i1 < nbttaglist2.tagCount(); ++i1) { NBTTagCompound nbttagcompound3 = (NBTTagCompound) nbttaglist2.tagAt(i1); TileEntity tileentity = TileEntity.c(nbttagcompound3);
		 * 
		 * if (tileentity != null) { chunk.addTileEntity(tileentity); } } }
		 * 
		 * if (nbttagcompound.hasKey("TileTicks")) { NBTTagList nbttaglist3 = nbttagcompound.getTagList("TileTicks");
		 * 
		 * if (nbttaglist3 != null) { for (int j1 = 0; j1 < nbttaglist3.tagCount(); ++j1) { NBTTagCompound nbttagcompound4 = (NBTTagCompound) nbttaglist3.tagAt(j1);
		 * 
		 * world.d(nbttagcompound4.getInteger("x"), nbttagcompound4.getInteger("y"), nbttagcompound4.getInteger("z"), nbttagcompound4.getInteger("i"), nbttagcompound4.getInteger("t")); } } }
		 */

		return chunk;
	}
}
