package nl.hypothermic.ofts.game.world.generator;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.game.Block;
import nl.hypothermic.ofts.game.world.loader.Chunk;

public class WorldGeneratorFlat extends WorldGenerator {

	@Override public Chunk generate(int chunkPosX, int chunkPosZ) {
		System.out.println("GENERATE CHUNK");
		byte[] var3 = new byte[32768];
		int xvar2 = var3.length / 256;

	        for (int xvar3 = 0; xvar3 < 16; ++xvar3)
	        {
	            for (int xvar4 = 0; xvar4 < 16; ++xvar4)
	            {
	                for (int xvar5 = 0; xvar5 < xvar2; ++xvar5)
	                {
	                    int xvar6 = 0;

	                    if (xvar5 == 0)
	                    {
	                        xvar6 = 7; // bedrock ID
	                    }
	                    else if (xvar5 <= 2)
	                    {
	                        xvar6 = 3; // dirt ID
	                    }
	                    else if (xvar5 == 3)
	                    {
	                        xvar6 = 2; // grass ID
	                    }

	                    var3[xvar3 << 11 | xvar4 << 7 | xvar5] = (byte)xvar6;
	                }
	            }
	        }
        Chunk var4 = new Chunk(var3, chunkPosX, chunkPosZ);

        byte[] var6 = var4.getBiomeArray();

        for (int var7 = 0; var7 < var6.length; ++var7)
        {
            //var6[var7] = (byte)var5[var7].biomeID;
        	
        	// Biome ID for plains is 1 (see MC->BiomeGenBase.java)
        	var6[var7] = 1;
        }

        //var4.generateSkylightMap();
        return var4;
	}
}
