package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import nl.hypothermic.ofts.game.world.ChunkSection;
import nl.hypothermic.ofts.game.world.NibbleArray;
import nl.hypothermic.ofts.game.world.loader.Chunk;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet51 extends Packet {

	// 51: CHUNK DATA

	/** The x-position of the transmitted chunk, in chunk coordinates. */
	public int x;

	/** The z-position of the transmitted chunk, in chunk coordinates. */
	public int z;

	/** The y-position of the lowest chunk Section in the transmitted chunk, in chunk coordinates. */
	public int yMin;

	/** The y-position of the highest chunk Section in the transmitted chunk, in chunk coordinates. */
	public int yMax;

	/** The transmitted chunk data, decompressed. */
	public byte[] chunkData;

	/** Whether to initialize the Chunk before applying the effect of the Packet51MapChunk. */
	public boolean includeInitialize;

	/** The length of the compressed chunk data byte array. */
	public int compressedSize;

	/** A temporary storage for the compressed chunk data byte array. */
	public byte[] rawData = new byte[0];

	public Packet51() {
		super(51);
	}

	public Packet51(DataInputStream dis) throws IOException {
		super(51);
		this.read(dis);
	}
	
	public Packet51(Chunk chunk, boolean includeInitialize, int par3) {
		this(chunk, includeInitialize, par3, chunk.xPosition, chunk.zPosition);
	}

	public Packet51(Chunk chunk, boolean includeInitialize, int par3, int xPos, int zPos) {
		super(51);
		System.out.println("debug 51 - chunkData len=" + 0 + " compressedSize=" + compressedSize);
		this.x = xPos;
		this.z = zPos;
		this.includeInitialize = includeInitialize;

		if (includeInitialize) {
			par3 = 65535;
			chunk.seenByPlayer = true;
		}

		ChunkSection[] var4 = chunk.getBlockStorageArray();
		int var5 = 0;
		int var6 = 0;
		int var7;

		for (var7 = 0; var7 < var4.length; ++var7) {
			if (var4[var7] != null && (!includeInitialize || !var4[var7].getIsEmpty()) && (par3 & 1 << var7) != 0) {
				this.yMin |= 1 << var7;
				++var5;

				if (var4[var7].getBlockMSBArray() != null) {
					this.yMax |= 1 << var7;
					++var6;
				}
			}
		}

		var7 = 2048 * (5 * var5 + var6);

		if (includeInitialize) {
			var7 += 256;
		}

		if (rawData.length < var7) {
			rawData = new byte[var7];
		}

		byte[] var8 = rawData;
		int var9 = 0;
		int var10;

		for (var10 = 0; var10 < var4.length; ++var10) {
			if (var4[var10] != null && (!includeInitialize || !var4[var10].getIsEmpty()) && (par3 & 1 << var10) != 0) {
				byte[] var11 = var4[var10].getBlockLSBArray();
				System.arraycopy(var11, 0, var8, var9, var11.length);
				var9 += var11.length;
			}
		}

		NibbleArray var15;

		for (var10 = 0; var10 < var4.length; ++var10) {
			if (var4[var10] != null && (!includeInitialize || !var4[var10].getIsEmpty()) && (par3 & 1 << var10) != 0) {
				var15 = var4[var10].getBlockMetadataArray();
				System.arraycopy(var15.data, 0, var8, var9, var15.data.length);
				var9 += var15.data.length;
			}
		}

		for (var10 = 0; var10 < var4.length; ++var10) {
			if (var4[var10] != null && (!includeInitialize || !var4[var10].getIsEmpty()) && (par3 & 1 << var10) != 0) {
				var15 = var4[var10].getBlocklightArray();
				System.arraycopy(var15.data, 0, var8, var9, var15.data.length);
				var9 += var15.data.length;
			}
		}

		for (var10 = 0; var10 < var4.length; ++var10) {
			if (var4[var10] != null && (!includeInitialize || !var4[var10].getIsEmpty()) && (par3 & 1 << var10) != 0) {
				var15 = var4[var10].getSkylightArray();
				System.arraycopy(var15.data, 0, var8, var9, var15.data.length);
				var9 += var15.data.length;
			}
		}

		if (var6 > 0) {
			for (var10 = 0; var10 < var4.length; ++var10) {
				if (var4[var10] != null && (!includeInitialize || !var4[var10].getIsEmpty()) && var4[var10].getBlockMSBArray() != null && (par3 & 1 << var10) != 0) {
					var15 = var4[var10].getBlockMSBArray();
					System.arraycopy(var15.data, 0, var8, var9, var15.data.length);
					var9 += var15.data.length;
				}
			}
		}

		if (includeInitialize) {
			byte[] var17 = chunk.getBiomeArray();
			System.arraycopy(var17, 0, var8, var9, var17.length);
			var9 += var17.length;
		}

		Deflater var16 = new Deflater(-1);

		try {
			var16.setInput(var8, 0, var9);
			var16.finish();
			System.out.println("var9" + var9);
			this.chunkData = new byte[var9];
			this.compressedSize = var16.deflate(this.chunkData);
		} finally {
			var16.end();
		}
	}

	@Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void read(DataInputStream dis) throws IOException {
		this.x = dis.readInt();
		this.z = dis.readInt();
		this.includeInitialize = dis.readBoolean();
		this.yMin = dis.readShort();
		this.yMax = dis.readShort();
		this.compressedSize = dis.readInt();
		/*this.field_48110_h = */dis.readInt();

		if (rawData.length < this.compressedSize) {
			rawData = new byte[this.compressedSize];
		}

		dis.readFully(rawData, 0, this.compressedSize);
		int var2 = 0;
		int var3;

		for (var3 = 0; var3 < 16; ++var3) {
			var2 += this.yMin >> var3 & 1;
		}

		var3 = 12288 * var2;

		if (this.includeInitialize) {
			var3 += 256;
		}

		this.chunkData = new byte[var3];
		Inflater var4 = new Inflater();
		var4.setInput(rawData, 0, this.compressedSize);

		try {
			var4.inflate(this.chunkData);
		} catch (DataFormatException var9) {
			throw new IOException("Bad compressed data format");
		} finally {
			var4.end();
		}
	}

	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(51);
		dos.writeInt(this.x);
		dos.writeInt(this.z);
		dos.writeBoolean(this.includeInitialize);
		dos.writeShort((short) (this.yMin & 65535));
		dos.writeShort((short) (this.yMax & 65535));
		dos.writeInt(this.compressedSize);
		dos.writeInt(/*this.field_48110_h*/ 0);
		System.out.println("bufsize=" + this.chunkData.length + ", csize=" + this.compressedSize);
		dos.write(this.chunkData, 0, this.compressedSize);
	}

	@Override public int getSize() {
		return 17 + this.compressedSize;
	}

	@Override public String toString() {
		return "Packet51 [x=" + this.x + ", z=" + this.z + ", yMin=" + this.yMin + ", yMax=" + this.yMax + ", chunkData=" + Arrays.toString(this.chunkData) + ", includeInitialize=" + this.includeInitialize + ", compressedSize=" + this.compressedSize + "]";
	}
}
