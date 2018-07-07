package nl.hypothermic.ofts.game.world.biome;

import nl.hypothermic.ofts.game.Block;
import nl.hypothermic.ofts.game.world.Biome;

public class BiomeBeach extends Biome {

    public BiomeBeach(int i) {
        super(i);
        this.K.clear();
        this.A = (byte) 12; // 12 = sand block id
        this.B = (byte) 12; // 12 = sand block id
    }
}
