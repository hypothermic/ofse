package nl.hypothermic.ofts.game.world;

import java.util.ArrayList;
import java.util.List;

import nl.hypothermic.ofts.game.world.biome.BiomeBeach;
import nl.hypothermic.ofts.game.world.biome.BiomeBigHills;
import nl.hypothermic.ofts.game.world.biome.BiomeDesert;
import nl.hypothermic.ofts.game.world.biome.BiomeForest;
import nl.hypothermic.ofts.game.world.biome.BiomeHell;
import nl.hypothermic.ofts.game.world.biome.BiomeIcePlains;
import nl.hypothermic.ofts.game.world.biome.BiomeJungle;
import nl.hypothermic.ofts.game.world.biome.BiomeMushrooms;
import nl.hypothermic.ofts.game.world.biome.BiomeOcean;
import nl.hypothermic.ofts.game.world.biome.BiomePlains;
import nl.hypothermic.ofts.game.world.biome.BiomeRiver;
import nl.hypothermic.ofts.game.world.biome.BiomeSwamp;
import nl.hypothermic.ofts.game.world.biome.BiomeTaiga;
import nl.hypothermic.ofts.game.world.biome.BiomeTheEnd;

public abstract class Biome {

    public static final Biome[] biomes = new Biome[256];
    public static final Biome OCEAN = (new BiomeOcean(0)).b(112).a("Ocean").b(-1.0F, 0.4F);
    public static final Biome PLAINS = (new BiomePlains(1)).b(9286496).a("Plains").a(0.8F, 0.4F);
    public static final Biome DESERT = (new BiomeDesert(2)).b(16421912).a("Desert").j().a(2.0F, 0.0F).b(0.1F, 0.2F);
    public static final Biome EXTREME_HILLS = (new BiomeBigHills(3)).b(6316128).a("Extreme Hills").b(0.2F, 1.3F).a(0.2F, 0.3F);
    public static final Biome FOREST = (new BiomeForest(4)).b(353825).a("Forest").a(5159473).a(0.7F, 0.8F);
    public static final Biome TAIGA = (new BiomeTaiga(5)).b(747097).a("Taiga").a(5159473).b().a(0.05F, 0.8F).b(0.1F, 0.4F);
    public static final Biome SWAMPLAND = (new BiomeSwamp(6)).b(522674).a("Swampland").a(9154376).b(-0.2F, 0.1F).a(0.8F, 0.9F);
    public static final Biome RIVER = (new BiomeRiver(7)).b(255).a("River").b(-0.5F, 0.0F);
    public static final Biome HELL = (new BiomeHell(8)).b(16711680).a("Hell").j().a(2.0F, 0.0F);
    public static final Biome SKY = (new BiomeTheEnd(9)).b(8421631).a("Sky").j();
    public static final Biome FROZEN_OCEAN = (new BiomeOcean(10)).b(9474208).a("FrozenOcean").b().b(-1.0F, 0.5F).a(0.0F, 0.5F);
    public static final Biome FROZEN_RIVER = (new BiomeRiver(11)).b(10526975).a("FrozenRiver").b().b(-0.5F, 0.0F).a(0.0F, 0.5F);
    public static final Biome ICE_PLAINS = (new BiomeIcePlains(12)).b(16777215).a("Ice Plains").b().a(0.0F, 0.5F);
    public static final Biome ICE_MOUNTAINS = (new BiomeIcePlains(13)).b(10526880).a("Ice Mountains").b().b(0.2F, 1.2F).a(0.0F, 0.5F);
    public static final Biome MUSHROOM_ISLAND = (new BiomeMushrooms(14)).b(16711935).a("MushroomIsland").a(0.9F, 1.0F).b(0.2F, 1.0F);
    public static final Biome MUSHROOM_SHORE = (new BiomeMushrooms(15)).b(10486015).a("MushroomIslandShore").a(0.9F, 1.0F).b(-1.0F, 0.1F);
    public static final Biome BEACH = (new BiomeBeach(16)).b(16440917).a("Beach").a(0.8F, 0.4F).b(0.0F, 0.1F);
    public static final Biome DESERT_HILLS = (new BiomeDesert(17)).b(13786898).a("DesertHills").j().a(2.0F, 0.0F).b(0.2F, 0.7F);
    public static final Biome FOREST_HILLS = (new BiomeForest(18)).b(2250012).a("ForestHills").a(5159473).a(0.7F, 0.8F).b(0.2F, 0.6F);
    public static final Biome TAIGA_HILLS = (new BiomeTaiga(19)).b(1456435).a("TaigaHills").b().a(5159473).a(0.05F, 0.8F).b(0.2F, 0.7F);
    public static final Biome SMALL_MOUNTAINS = (new BiomeBigHills(20)).b(7501978).a("Extreme Hills Edge").b(0.2F, 0.8F).a(0.2F, 0.3F);
    public static final Biome JUNGLE = (new BiomeJungle(21)).b(5470985).a("Jungle").a(5470985).a(1.2F, 0.9F).b(0.2F, 0.4F);
    public static final Biome JUNGLE_HILLS = (new BiomeJungle(22)).b(2900485).a("JungleHills").a(5470985).a(1.2F, 0.9F).b(1.8F, 0.2F);
    public String y;
    public int z;
    public byte A;
    public byte B;
    public int C;
    public float D;
    public float E;
    public float F;
    public float G;
    public int H;
    //public BiomeDecorator I;
    protected List J;
    protected List K;
    protected List L;
    private boolean R;
    private boolean S;
    public final int id;
    //protected WorldGenTrees N;
    //protected WorldGenBigTree O;
    //protected WorldGenForest P;
    //protected WorldGenSwampTree Q;

    protected Biome(int i) {
        this.A = (byte) 2; // 2 = grass
        this.B = (byte) 3; // 3 = dirt
        this.C = 5169201;
        this.D = 0.1F;
        this.E = 0.3F;
        this.F = 0.5F;
        this.G = 0.5F;
        this.H = 16777215;
        this.J = new ArrayList();
        this.K = new ArrayList();
        this.L = new ArrayList();
        this.S = true;
        this.id = i;
        biomes[i] = this;
    }

    private Biome a(float f, float f1) {
        if (f > 0.1F && f < 0.2F) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        } else {
            this.F = f;
            this.G = f1;
            return this;
        }
    }

    private Biome b(float f, float f1) {
        this.D = f;
        this.E = f1;
        return this;
    }

    private Biome j() {
        this.S = false;
        return this;
    }

    protected Biome b() {
        this.R = true;
        return this;
    }

    protected Biome a(String s) {
        this.y = s;
        return this;
    }

    protected Biome a(int i) {
        this.C = i;
        return this;
    }

    protected Biome b(int i) {
        this.z = i;
        return this;
    }

    public boolean c() {
        return this.R;
    }

    public boolean d() {
        return this.R ? false : this.S;
    }

    public boolean e() {
        return this.G > 0.85F;
    }

    public float f() {
        return 0.1F;
    }

    public final int g() {
        return (int) (this.G * 65536.0F);
    }

    public final int h() {
        return (int) (this.F * 65536.0F);
    }

    public final float i() {
        return this.F;
    }
}
