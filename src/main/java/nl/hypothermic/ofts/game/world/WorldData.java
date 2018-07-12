package nl.hypothermic.ofts.game.world;

import java.util.List;

import nl.hypothermic.ofts.game.Player;
import nl.hypothermic.ofts.nbt.NBTTagCompound;

public class WorldData {

    private long seed;
    private WorldType type;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long time;
    private long lastPlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerData;
    private int dimension;
    private String name;
    private int version;
    private boolean isRaining;
    private int rainTicks;
    private boolean isThundering;
    private int thunderTicks;
    private int gameType;
    private boolean useMapFeatures;
    private boolean hardcore;

    public WorldData(NBTTagCompound nbttagcompound) {
        this.type = WorldType.NORMAL;
        this.hardcore = false;
        this.seed = nbttagcompound.getLong("RandomSeed");
        if (nbttagcompound.hasKey("generatorName")) {
            String s = nbttagcompound.getString("generatorName");

            this.type = WorldType.getType(s);
            if (this.type == null) {
                this.type = WorldType.NORMAL;
            } else if (this.type.c()) {
                int i = 0;

                if (nbttagcompound.hasKey("generatorVersion")) {
                    i = nbttagcompound.getInteger("generatorVersion");
                }

                this.type = this.type.a(i);
            }
        }

        this.gameType = nbttagcompound.getInteger("GameType");
        if (nbttagcompound.hasKey("MapFeatures")) {
            this.useMapFeatures = nbttagcompound.getBoolean("MapFeatures");
        } else {
            this.useMapFeatures = true;
        }

        this.spawnX = nbttagcompound.getInteger("SpawnX");
        this.spawnY = nbttagcompound.getInteger("SpawnY");
        this.spawnZ = nbttagcompound.getInteger("SpawnZ");
        this.time = nbttagcompound.getLong("Time");
        this.lastPlayed = nbttagcompound.getLong("LastPlayed");
        this.sizeOnDisk = nbttagcompound.getLong("SizeOnDisk");
        this.name = nbttagcompound.getString("LevelName");
        this.version = nbttagcompound.getInteger("version");
        this.rainTicks = nbttagcompound.getInteger("rainTime");
        this.isRaining = nbttagcompound.getBoolean("raining");
        this.thunderTicks = nbttagcompound.getInteger("thunderTime");
        this.isThundering = nbttagcompound.getBoolean("thundering");
        this.hardcore = nbttagcompound.getBoolean("hardcore");
        if (nbttagcompound.hasKey("Player")) {
            this.playerData = nbttagcompound.getCompoundTag("Player");
            this.dimension = this.playerData.getInteger("Dimension");
        }
    }

    public WorldData(WorldData worlddata) {
        this.type = WorldType.NORMAL;
        this.hardcore = false;
        this.seed = worlddata.seed;
        this.type = worlddata.type;
        this.gameType = worlddata.gameType;
        this.useMapFeatures = worlddata.useMapFeatures;
        this.spawnX = worlddata.spawnX;
        this.spawnY = worlddata.spawnY;
        this.spawnZ = worlddata.spawnZ;
        this.time = worlddata.time;
        this.lastPlayed = worlddata.lastPlayed;
        this.sizeOnDisk = worlddata.sizeOnDisk;
        this.playerData = worlddata.playerData;
        this.dimension = worlddata.dimension;
        this.name = worlddata.name;
        this.version = worlddata.version;
        this.rainTicks = worlddata.rainTicks;
        this.isRaining = worlddata.isRaining;
        this.thunderTicks = worlddata.thunderTicks;
        this.isThundering = worlddata.isThundering;
        this.hardcore = worlddata.hardcore;
    }

    public NBTTagCompound a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.a(nbttagcompound, this.playerData);
        return nbttagcompound;
    }

    public NBTTagCompound a(List list) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Player entityhuman = null;
        NBTTagCompound nbttagcompound1 = null;

        if (list.size() > 0) {
            entityhuman = (Player) list.get(0);
        }

        if (entityhuman != null) {
            nbttagcompound1 = new NBTTagCompound();
            entityhuman.toNbtData(nbttagcompound1);
        }

        this.a(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void a(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        nbttagcompound.setLong("RandomSeed", this.seed);
        nbttagcompound.setString("generatorName", this.type.getName());
        nbttagcompound.setInteger("generatorVersion", this.type.getVersion());
        nbttagcompound.setInteger("GameType", this.gameType);
        nbttagcompound.setBoolean("MapFeatures", this.useMapFeatures);
        nbttagcompound.setInteger("SpawnX", this.spawnX);
        nbttagcompound.setInteger("SpawnY", this.spawnY);
        nbttagcompound.setInteger("SpawnZ", this.spawnZ);
        nbttagcompound.setLong("Time", this.time);
        nbttagcompound.setLong("SizeOnDisk", this.sizeOnDisk);
        nbttagcompound.setLong("LastPlayed", System.currentTimeMillis());
        nbttagcompound.setString("LevelName", this.name);
        nbttagcompound.setInteger("version", this.version);
        nbttagcompound.setInteger("rainTime", this.rainTicks);
        nbttagcompound.setBoolean("raining", this.isRaining);
        nbttagcompound.setInteger("thunderTime", this.thunderTicks);
        nbttagcompound.setBoolean("thundering", this.isThundering);
        nbttagcompound.setBoolean("hardcore", this.hardcore);
        if (nbttagcompound1 != null) {
            nbttagcompound.setCompoundTag("Player", nbttagcompound1);
        }
    }

    public long getSeed() {
        return this.seed;
    }

    public int getSpawnX() {
        return this.spawnX;
    }

    public int getSpawnY() {
        return this.spawnY;
    }

    public int getSpawnZ() {
        return this.spawnZ;
    }

    public long getTime() {
        return this.time;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setTime(long newTime) {
        this.time = newTime;
    }

    public void setSpawn(int i, int j, int k) {
        this.spawnX = i;
        this.spawnY = j;
        this.spawnZ = k;
    }

    public void setWorldName(String newName) {
        this.name = newName;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int newVersion) {
        this.version = newVersion;
    }

    public boolean isThundering() {
        return this.isThundering;
    }

    public void setThundering(boolean flag) {
        this.isThundering = flag;
    }

    public int getThunderDuration() {
        return this.thunderTicks;
    }

    public void setThunderDuration(int newThunderDuration) {
        this.thunderTicks = newThunderDuration;
    }

    public boolean hasStorm() {
        return this.isRaining;
    }

    public void setStorm(boolean flag) {
        this.isRaining = flag;
    }

    public int getWeatherDuration() {
        return this.rainTicks;
    }

    public void setWeatherDuration(int i) {
        this.rainTicks = i;
    }

    public int getGameType() {
        return this.gameType;
    }

    public boolean shouldGenerateMapFeatures() {
        return this.useMapFeatures;
    }

    public void setGameType(int i) {
        this.gameType = i;
    }

    public boolean isHardcore() {
        return this.hardcore;
    }

    public WorldType getType() {
        return this.type;
    }

    public void setType(WorldType worldtype) {
        this.type = worldtype;
    }
}
