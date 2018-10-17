package nl.hypothermic.ofts.game.world.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.game.Entity;
import nl.hypothermic.ofts.game.world.ChunkCoordIntPair;
import nl.hypothermic.ofts.game.world.ChunkSection;
import nl.hypothermic.ofts.util.MathHelper;

public class Chunk {
	/**
	 * Determines if the chunk is lit or not at a light value greater than 0.
	 * It's LIT bois!
	 */
	public static boolean isLit;

	/**
	 * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a logical segment of 16x16x16 blocks, stacked vertically.
	 */
	public ChunkSection[] storageArrays;

	/**
	 * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs.
	 */
	private byte[] blockBiomeArray;

	/**
	 * A map, similar to heightMap, that tracks how far down precipitation can fall.
	 */
	public int[] precipitationHeightMap;

	/** Which columns need their skylightMaps updated. */
	public boolean[] updateSkylightColumns;

	/** Whether or not this Chunk is currently loaded into the World */
	public boolean isChunkLoaded;

	/** Reference to the World object. */
	public int[] heightMap;

	/** The x coordinate of the chunk. */
	public final int xPosition;

	/** The z coordinate of the chunk. */
	public final int zPosition;
	private boolean isGapLightingUpdated;

	/** A Map of ChunkPositions to TileEntities in this chunk */
	public Map chunkTileEntityMap;

	/**
	 * Array of Lists containing the entities in this Chunk. Each List represents a 16 block subchunk.
	 */
	public List[] entityLists;

	/** Boolean value indicating if the terrain is populated. */
	public boolean isTerrainPopulated;

	/**
	 * Set to true if the chunk has been modified and needs to be updated internally.
	 */
	public boolean isModified;

	/**
	 * Whether this Chunk has any Entities and thus requires saving on every tick (.m)
	 */
	public boolean hasEntities;

	/** The time according to World.worldTime when this chunk was last saved */
	public long lastSaveTime;
	public boolean seenByPlayer;

	/**
	 * Contains the current round-robin relight check index, and is implied as the relight check location as well.
	 */
	private int queuedLightChecks;
	boolean field_35638_u;

	public Chunk(int par2, int par3) {
		this.storageArrays = new ChunkSection[16];
		this.blockBiomeArray = new byte[256];
		this.precipitationHeightMap = new int[256];
		this.updateSkylightColumns = new boolean[256];
		this.isGapLightingUpdated = false;
		this.chunkTileEntityMap = new HashMap();
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.seenByPlayer = false;
		this.queuedLightChecks = 4096;
		this.field_35638_u = false;
		this.entityLists = new List[16];
		this.xPosition = par2;
		this.zPosition = par3;
		this.heightMap = new int[256];

		for (int var4 = 0; var4 < this.entityLists.length; ++var4) {
			this.entityLists[var4] = new ArrayList();
		}

		Arrays.fill(this.precipitationHeightMap, -999);
		Arrays.fill(this.blockBiomeArray, (byte) -1);
	}

	public Chunk(byte[] par2ArrayOfByte, int par3, int par4) {
		this(par3, par4);
		int var5 = par2ArrayOfByte.length / 256;

		for (int var6 = 0; var6 < 16; ++var6) {
			for (int var7 = 0; var7 < 16; ++var7) {
				for (int var8 = 0; var8 < var5; ++var8) {
					/*
					 * FORGE: The following change, a cast from unsigned byte to int, fixes a vanilla bug when generating new chunks that contain a block ID > 127
					 */
					int var9 = par2ArrayOfByte[var6 << 11 | var7 << 7 | var8] & 0xFF;

					if (var9 != 0) {
						int var10 = var8 >> 4;

						if (this.storageArrays[var10] == null) {
							this.storageArrays[var10] = new ChunkSection(var10 << 4);
						}

						this.storageArrays[var10].setExtBlockID(var6, var8 & 15, var7, var9);
					}
				}
			}
		}
	}

	/**
	 * Metadata sensitive Chunk constructor for use in new ChunkProviders that use metadata sensitive blocks during generation.
	 * 
	 * @param world
	 *            The world this chunk belongs to
	 * @param ids
	 *            A ByteArray containing all the BlockID's to set this chunk to
	 * @param metadata
	 *            A ByteArray containing all the metadata to set this chunk to
	 * @param chunkX
	 *            The chunk's X position
	 * @param chunkZ
	 *            The Chunk's Z position
	 */
	public Chunk(byte[] ids, byte[] metadata, int chunkX, int chunkZ) {
		this(chunkX, chunkZ);
		int height = ids.length / 256;

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				for (int y = 0; y < height; ++y) {
					int index = x << 11 | z << 7 | y;
					int id = ids[index] & 0xFF;
					int meta = metadata[index] & 0x0F;

					if (id != 0) {
						int chunkY = y >> 4;

						if (storageArrays[chunkY] == null) {
							storageArrays[chunkY] = new ChunkSection(chunkY << 4);
						}

						storageArrays[chunkY].setExtBlockID(x, y & 15, z, id);
						storageArrays[chunkY].setExtBlockMetadata(x, y & 15, z, meta);
					}
				}
			}
		}
	}

	/**
	 * Checks whether the chunk is at the X/Z location specified
	 */
	public boolean isAtLocation(int par1, int par2) {
		return par1 == this.xPosition && par2 == this.zPosition;
	}

	/**
	 * Returns the value in the height map at this x, z coordinate in the chunk
	 */
	public int getHeightValue(int par1, int par2) {
		return this.heightMap[par2 << 4 | par1];
	}

	/**
	 * Returns the topmost ExtendedBlockStorage instance for this Chunk that actually contains a block.
	 */
	public int getTopFilledSegment() {
		for (int var1 = this.storageArrays.length - 1; var1 >= 0; --var1) {
			if (this.storageArrays[var1] != null) {
				return this.storageArrays[var1].getYLocation();
			}
		}
		return 0;
	}

	/**
	 * Returns the ExtendedBlockStorage array for this Chunk.
	 */
	public ChunkSection[] getBlockStorageArray() {
		return this.storageArrays;
	}

	/**
	 * Return the ID of a block in the chunk.
	 */
	public int getBlockId(int par1, int par2, int par3) {
		if (par2 >> 4 >= this.storageArrays.length || par2 >> 4 < 0) {
			return 0;
		} else {
			ChunkSection var4 = this.storageArrays[par2 >> 4];
			return var4 != null ? var4.getExtBlockID(par1, par2 & 15, par3) : 0;
		}
	}

	/**
	 * Return the metadata corresponding to the given coordinates inside a chunk.
	 */
	public int getBlockMetadata(int par1, int par2, int par3) {
		if (par2 >> 4 >= this.storageArrays.length || par2 >> 4 < 0) {
			return 0;
		} else {
			ChunkSection var4 = this.storageArrays[par2 >> 4];
			return var4 != null ? var4.getExtBlockMetadata(par1, par2 & 15, par3) : 0;
		}
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	public void addEntity(Entity entity) {
        this.hasEntities = true;
        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);

        if (i != this.xPosition || j != this.zPosition) {
            System.out.println("Wrong location! " + entity);
            Thread.dumpStack();
        }

        int k = MathHelper.floor(entity.locY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entityLists.length) {
            k = this.entityLists.length - 1;
        }

        entity.bZ = true;
        entity.ca = this.xPosition;
        entity.cb = k;
        entity.cc = this.zPosition;
        this.entityLists[k].add(entity);
    }

    public void b(Entity entity) {
        this.a(entity, entity.cb);
    }

    public void a(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.entityLists.length) {
            i = this.entityLists.length - 1;
        }

        this.entityLists[i].remove(entity);
    }

	/**
	 * Turns unknown blocks into air blocks to avoid crashing Minecraft.
	 */
	public void removeUnknownBlocks() {
		ChunkSection[] var1 = this.storageArrays;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			ChunkSection var4 = var1[var3];
		}
	}
	
	/**
	 * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty (true) or not (false).
	 */
	public boolean getAreLevelsEmpty(int par1, int par2) {
		if (par1 < 0) {
			par1 = 0;
		}

		if (par2 >= 256) {
			par2 = 255;
		}

		for (int var3 = par1; var3 <= par2; var3 += 16) {
			ChunkSection var4 = this.storageArrays[var3 >> 4];

			if (var4 != null && !var4.getIsEmpty()) {
				return false;
			}
		}

		return true;
	}

	public void setStorageArrays(ChunkSection[] par1ArrayOfExtendedBlockStorage) {
		this.storageArrays = par1ArrayOfExtendedBlockStorage;
	}
	/**
	 * Returns an array containing a 16x16 mapping on the X/Z of block positions in this Chunk to biome IDs.
	 */
	public byte[] getBiomeArray() {
		return this.blockBiomeArray;
	}

	/**
	 * Accepts a 256-entry array that contains a 16x16 mapping on the X/Z plane of block positions in this Chunk to biome IDs.
	 */
	public void setBiomeArray(byte[] par1ArrayOfByte) {
		this.blockBiomeArray = par1ArrayOfByte;
	}
	
	public ChunkCoordIntPair getChunkCoordIntPair() {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }
}