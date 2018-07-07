package nl.hypothermic.ofts.game.world.loader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import nl.hypothermic.ofts.game.world.Biome;
import nl.hypothermic.ofts.game.world.WorldChunkManager;
import nl.hypothermic.ofts.game.world.WorldChunkManagerHell;
import nl.hypothermic.ofts.game.world.WorldData;
import nl.hypothermic.ofts.game.world.WorldType;
import nl.hypothermic.ofts.nbt.NBTCompressedStreamTools;
import nl.hypothermic.ofts.nbt.NBTTagCompound;

public class WorldLoaderServer extends WorldLoader {

	public WorldLoaderServer(File file1) {
		super(file1);
	}

	protected int a() {
		return 19133;
	}

	public boolean isConvertable(String s) {
		WorldData worlddata = this.b(s);

		return worlddata != null && worlddata.h() != this.a();
	}

	public boolean convert(String s) {
		ArrayList arraylist = new ArrayList();
		ArrayList arraylist1 = new ArrayList();
		ArrayList arraylist2 = new ArrayList();
		File file1 = new File(this.a, s);
		File file2 = new File(file1, "DIM-1");
		File file3 = new File(file1, "DIM1");

		System.out.println("Scanning folders...");
		this.a(file1, arraylist);
		if (file2.exists()) {
			this.a(file2, arraylist1);
		}

		if (file3.exists()) {
			this.a(file3, arraylist2);
		}

		int i = arraylist.size() + arraylist1.size() + arraylist2.size();

		System.out.println("Total conversion count is " + i);
		WorldData worlddata = this.b(s);
		Object object = null;

		if (worlddata.getType() == WorldType.FLAT) {
			object = new WorldChunkManagerHell(Biome.PLAINS, 0.5F, 0.5F);
		} else {
			object = new WorldChunkManager(worlddata.getSeed(), worlddata.getType());
		}

		this.a(new File(file1, "region"), arraylist, (WorldChunkManager) object, 0, i);
		this.a(new File(file2, "region"), arraylist1, new WorldChunkManagerHell(Biome.HELL, 1.0F, 0.0F), arraylist.size(), i);
		this.a(new File(file3, "region"), arraylist2, new WorldChunkManagerHell(Biome.SKY, 0.5F, 0.0F), arraylist.size() + arraylist1.size(), i);
		worlddata.a(19133);
		if (worlddata.getType() == WorldType.VERSION_1_1f) {
			worlddata.setType(WorldType.NORMAL);
		}

		this.c(s);
		return true;
	}

	private void c(String s) {
		File file1 = new File(this.a, s);

		if (!file1.exists()) {
			System.out.println("Warning: Unable to create level.dat_mcr backup");
		} else {
			File file2 = new File(file1, "level.dat");

			if (!file2.exists()) {
				System.out.println("Warning: Unable to create level.dat_mcr backup");
			} else {
				File file3 = new File(file1, "level.dat_mcr");

				if (!file2.renameTo(file3)) {
					System.out.println("Warning: Unable to create level.dat_mcr backup");
				}
			}
		}
	}

	private void a(File file1, ArrayList arraylist, WorldChunkManager worldchunkmanager, int i, int j) {
		Iterator iterator = arraylist.iterator();

		while (iterator.hasNext()) {
			File file2 = (File) iterator.next();
			this.a(file1, file2, worldchunkmanager, i, j);
		}
	}

	private void a(File file1, File file2, WorldChunkManager worldchunkmanager, int i, int j) {
		try {
			String s = file2.getName();
			RegionFile regionfile = new RegionFile(file2);
			RegionFile regionfile1 = new RegionFile(new File(file1, s.substring(0, s.length() - ".mcr".length()) + ".mca"));

			for (int k = 0; k < 32; ++k) {
				for (int l = 0; l < 32; ++l) {
					if (regionfile.c(k, l) && !regionfile1.c(k, l)) {
						DataInputStream datainputstream = regionfile.a(k, l);

						if (datainputstream == null) {
							System.out.println("Failed to fetch input stream");
						} else {
							NBTTagCompound nbttagcompound = NBTCompressedStreamTools.read((DataInput) datainputstream);

							datainputstream.close();
							NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
							OldChunk oldchunk = OldChunkLoader.a(nbttagcompound1);
							NBTTagCompound nbttagcompound2 = new NBTTagCompound();
							NBTTagCompound nbttagcompound3 = new NBTTagCompound();

							nbttagcompound2.setTag("Level", nbttagcompound3);
							OldChunkLoader.a(oldchunk, nbttagcompound3, worldchunkmanager);
							DataOutputStream dataoutputstream = regionfile1.b(k, l);

							NBTCompressedStreamTools.write(nbttagcompound2, (DataOutput) dataoutputstream);
							dataoutputstream.close();
						}
					}
				}
			}

			regionfile.a();
			regionfile1.a();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	private void a(File file1, ArrayList arraylist) {
		File file2 = new File(file1, "region");
		File[] afile = file2.listFiles(new ChunkFilenameFilter(this));

		if (afile != null) {
			File[] afile1 = afile;
			int i = afile.length;

			for (int j = 0; j < i; ++j) {
				File file3 = afile1[j];

				arraylist.add(file3);
			}
		}
	}
}
