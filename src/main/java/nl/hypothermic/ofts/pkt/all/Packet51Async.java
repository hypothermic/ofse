package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
// import java.util.zip.Deflater; // CraftBukkit
import java.util.zip.Inflater;

import nl.hypothermic.ofts.game.world.ChunkSection;
import nl.hypothermic.ofts.game.world.NibbleArray;
import nl.hypothermic.ofts.game.world.loader.Chunk;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet51Async extends Packet {
	
	// This packet was imported from the old CraftBukkit sources
	// For testing only, this one doesn't work either.
	// It should be routed through the compression thread!!

    public int a;
    public int b;
    public int c;
    public int d;
    public byte[] buffer;
    public boolean f;
    public int size; // CraftBukkit - private -> public
    private int h;
    public byte[] rawData = new byte[0]; // CraftBukkit

    public Packet51Async() {
    	super(51);
    }

    public Packet51Async(Chunk chunk, boolean flag, int i) {
        super(51);
        this.a = chunk.xPosition;
        this.b = chunk.zPosition;
        this.f = flag;
        if (flag) {
            i = '\uffff';
            chunk.seenByPlayer = true;
        }

        ChunkSection[] achunksection = chunk.getBlockStorageArray();
        int j = 0;
        int k = 0;

        int l;

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].getIsEmpty()) && (i & 1 << l) != 0) {
                this.c |= 1 << l;
                ++j;
                if (achunksection[l].getBlockMSBArray() != null) {
                    this.d |= 1 << l;
                    ++k;
                }
            }
        }

        l = 2048 * (5 * j + k);
        if (flag) {
            l += 256;
        }

        if (rawData.length < l) {
            rawData = new byte[l];
        }

        byte[] abyte = rawData;
        int i1 = 0;

        int j1;

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].getIsEmpty()) && (i & 1 << j1) != 0) {
                byte[] abyte1 = achunksection[j1].getBlockLSBArray();

                System.arraycopy(abyte1, 0, abyte, i1, abyte1.length);
                i1 += abyte1.length;
            }
        }

        NibbleArray nibblearray;

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].getIsEmpty()) && (i & 1 << j1) != 0) {
                nibblearray = achunksection[j1].getBlockMetadataArray(); // PRESUME (.i();)
                System.arraycopy(nibblearray.data, 0, abyte, i1, nibblearray.data.length);
                i1 += nibblearray.data.length;
            }
        }

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].getIsEmpty()) && (i & 1 << j1) != 0) {
                nibblearray = achunksection[j1].getBlocklightArray(); // PRESUME (.j();)
                System.arraycopy(nibblearray.data, 0, abyte, i1, nibblearray.data.length);
                i1 += nibblearray.data.length;
            }
        }

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].getIsEmpty()) && (i & 1 << j1) != 0) {
                nibblearray = achunksection[j1].getSkylightArray(); // PRESUME (.k();)
                System.arraycopy(nibblearray.data, 0, abyte, i1, nibblearray.data.length);
                i1 += nibblearray.data.length;
            }
        }

        if (k > 0) {
            for (j1 = 0; j1 < achunksection.length; ++j1) {
                if (achunksection[j1] != null && (!flag || !achunksection[j1].getIsEmpty()) && achunksection[j1].getBlockMSBArray() != null && (i & 1 << j1) != 0) {
                    nibblearray = achunksection[j1].getBlockMSBArray();
                    System.arraycopy(nibblearray.data, 0, abyte, i1, nibblearray.data.length);
                    i1 += nibblearray.data.length;
                }
            }
        }

        if (flag) {
            byte[] abyte2 = chunk.getBiomeArray();

            System.arraycopy(abyte2, 0, abyte, i1, abyte2.length);
            i1 += abyte2.length;
        }

        /* CraftBukkit start - Moved compression into its own method.
        byte[] abyte = data; // CraftBukkit - uses data from above constructor
        Deflater deflater = new Deflater(-1);

        try {
            deflater.setInput(abyte, 0, i1);
            deflater.finish();
            this.buffer = new byte[i1];
            this.size = deflater.deflate(this.buffer);
        } finally {
            deflater.end();
        } */
        this.rawData = abyte;
        // CraftBukkit end
    }
    
    @Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}

    public void read(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOEXception
        this.a = datainputstream.readInt();
        this.b = datainputstream.readInt();
        this.f = datainputstream.readBoolean();
        this.c = datainputstream.readShort();
        this.d = datainputstream.readShort();
        this.size = datainputstream.readInt();
        this.h = datainputstream.readInt();
        if (rawData.length < this.size) {
            rawData = new byte[this.size];
        }

        datainputstream.readFully(rawData, 0, this.size);
        int i = 0;

        int j;

        for (j = 0; j < 16; ++j) {
            i += this.c >> j & 1;
        }

        j = 12288 * i;
        if (this.f) {
            j += 256;
        }

        this.buffer = new byte[j];
        Inflater inflater = new Inflater();

        inflater.setInput(rawData, 0, this.size);

        try {
            inflater.inflate(this.buffer);
        } catch (DataFormatException dataformatexception) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }
    }

    public void write(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
    	dataoutputstream.write(51);
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeInt(this.b);
        dataoutputstream.writeBoolean(this.f);
        dataoutputstream.writeShort((short) (this.c & '\uffff'));
        dataoutputstream.writeShort((short) (this.d & '\uffff'));
        dataoutputstream.writeInt(this.size);
        dataoutputstream.writeInt(this.h);
        try {
			Thread.sleep(150);
		} catch (InterruptedException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
		}
        System.out.println("WRITING BUFFER - Null:" + (buffer == null));
        try {
			Thread.sleep(150);
		} catch (InterruptedException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
		}
        dataoutputstream.write(this.buffer, 0, this.size);
    }

    public int getSize() {
        return 17 + this.size;
    }

	public String toString() {
		return "Packet51Async [a=" + this.a + ", b=" + this.b + ", c=" + this.c + ", d=" + this.d + ", buffer=" + Arrays.toString(this.buffer) + ", f=" + this.f + ", size=" + this.size + ", h=" + this.h + ", rawData=" + Arrays.toString(this.rawData) + "]";
	}
}
