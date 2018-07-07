package nl.hypothermic.ofts.game.world.biome;

import nl.hypothermic.ofts.game.world.Biome;

public class BiomeTheEnd extends Biome {

    public BiomeTheEnd(int i) {
        super(i);
        this.J.clear();
        this.K.clear();
        this.L.clear();
        this.A = (byte) 3; // 3 = dirt
        this.B = (byte) 3; // 3 = dirt
    }
}
