package nl.hypothermic.ofts.game.world.biome;

import nl.hypothermic.ofts.game.world.Biome;

public class BiomeMushrooms extends Biome {

    public BiomeMushrooms(int i) {
        super(i);
        this.A = (byte) 110; // 110 = mycelium
        this.J.clear();
        this.K.clear();
        this.L.clear();
    }
}
