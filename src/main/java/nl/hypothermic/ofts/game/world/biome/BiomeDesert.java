package nl.hypothermic.ofts.game.world.biome;

import java.util.Random;

import nl.hypothermic.ofts.game.World;
import nl.hypothermic.ofts.game.world.Biome;

public class BiomeDesert extends Biome {

    public BiomeDesert(int i) {
        super(i);
        this.K.clear();
        this.A = (byte) 12; // 12 = sand id
        this.B = (byte) 12; // 12 = sand id
    }

    public void a(World world, Random random, int i, int j) {
        ;
    }
}
