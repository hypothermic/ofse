package nl.hypothermic.ofts.game;

import java.util.Random;

import nl.hypothermic.ofts.game.entity.AxisAlignedBB;
import nl.hypothermic.ofts.game.entity.EntityTypes;
import nl.hypothermic.ofts.nbt.NBTTagCompound;
import nl.hypothermic.ofts.nbt.NBTTagDouble;
import nl.hypothermic.ofts.nbt.NBTTagFloat;
import nl.hypothermic.ofts.nbt.NBTTagList;

public abstract class Entity {

    private static int entityCount = 0;
    public int id;
    public double be;
    public boolean bf;
    public Entity passenger;
    public Entity vehicle;
    public World world;
    public double lastX;
    public double lastY;
    public double lastZ;
    public double locX;
    public double locY;
    public double locZ;
    public double motX;
    public double motY;
    public double motZ;
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;
    public final AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean positionChanged;
    public boolean bz;
    public boolean bA;
    public boolean velocityChanged;
    protected boolean bC;
    public boolean bD;
    public boolean dead;
    public float height;
    public float width;
    public float length;
    public float bI;
    public float bJ;
    public float fallDistance;
    private int b;
    public double bL;
    public double bM;
    public double bN;
    public float bO;
    public float bP;
    public boolean bQ;
    public float bR;
    protected Random random;
    public int ticksLived;
    public int maxFireTicks;
    private int fireTicks;
    protected boolean bV;
    public int noDamageTicks;
    private boolean justCreated;
    protected boolean fireProof;
    private double e;
    private double f;
    public boolean bZ;
    public int ca;
    public int cb;
    public int cc;
    public boolean cd;
    public boolean ce;

    public Entity() {
        this.id = entityCount++;
        this.be = 1.0D;
        this.bf = false;
        this.boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.bA = false;
        this.velocityChanged = false;
        this.bD = true;
        this.dead = false;
        this.height = 0.0F;
        this.width = 0.6F;
        this.length = 1.8F;
        this.bI = 0.0F;
        this.bJ = 0.0F;
        this.fallDistance = 0.0F;
        this.b = 1;
        this.bO = 0.0F;
        this.bP = 0.0F;
        this.bQ = false;
        this.bR = 0.0F;
        this.random = new Random();
        this.ticksLived = 0;
        this.maxFireTicks = 1;
        this.fireTicks = 0;
        this.bV = false;
        this.noDamageTicks = 0;
        this.justCreated = true;
        this.fireProof = false;
        this.bZ = false;
        this.setPosition(0.0D, 0.0D, 0.0D);
    }

    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity) object).id == this.id : false;
    }

    public int hashCode() {
        return this.id;
    }

    public void die() {
        this.dead = true;
    }

    public void setPosition(double d0, double d1, double d2) {
        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        float f = this.width / 2.0F;
        float f1 = this.length;

        this.boundingBox.c(d0 - (double) f, d1 - (double) this.height + (double) this.bO, d2 - (double) f, d0 + (double) f, d1 - (double) this.height + (double) this.bO + (double) f1, d2 + (double) f);
    }

    public void setLocation(double d0, double d1, double d2, float f, float f1) {
        this.lastX = this.locX = d0;
        this.lastY = this.locY = d1;
        this.lastZ = this.locZ = d2;
        this.lastYaw = this.yaw = f;
        this.lastPitch = this.pitch = f1;
        this.bO = 0.0F;
        double d3 = (double) (this.lastYaw - f);

        if (d3 < -180.0D) {
            this.lastYaw += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.lastYaw -= 360.0F;
        }

        this.setPosition(this.locX, this.locY, this.locZ);
        this.width = f;
        this.length = f1;
    }

    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        this.bL = this.lastX = this.locX = d0;
        this.bM = this.lastY = this.locY = d1 + (double) this.height;
        this.bN = this.lastZ = this.locZ = d2;
        this.yaw = f;
        this.pitch = f1;
        this.setPosition(this.locX, this.locY, this.locZ);
    }
    
    /** c(...) */
    public boolean matchesNbtData(NBTTagCompound nbttagcompound) {
        String s = this.getName();

        if (!this.dead && s != null) {
            nbttagcompound.setString("id", s);
            this.toNbtData(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }
    
    /** d(...) */
    public void toNbtData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setTag("Pos", this.a(new double[] { this.locX, this.locY + (double) this.bO, this.locZ}));
        nbttagcompound.setTag("Motion", this.a(new double[] { this.motX, this.motY, this.motZ}));
        nbttagcompound.setTag("Rotation", this.a(new float[] { this.yaw, this.pitch}));
        nbttagcompound.setFloat("FallDistance", this.fallDistance);
        nbttagcompound.setShort("Fire", (short) this.fireTicks);
        //nbttagcompound.setShort("Air", (short) this.getAirTicks());
        nbttagcompound.setBoolean("OnGround", this.onGround);
        this.toNbt(nbttagcompound);
    }

    /** e(...) */
    public void fromNbtData(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.getTagList("Rotation");

        this.motX = ((NBTTagDouble) nbttaglist1.tagAt(0)).data;
        this.motY = ((NBTTagDouble) nbttaglist1.tagAt(1)).data;
        this.motZ = ((NBTTagDouble) nbttaglist1.tagAt(2)).data;
        if (Math.abs(this.motX) > 10.0D) {
            this.motX = 0.0D;
        }

        if (Math.abs(this.motY) > 10.0D) {
            this.motY = 0.0D;
        }

        if (Math.abs(this.motZ) > 10.0D) {
            this.motZ = 0.0D;
        }

        this.lastX = this.bL = this.locX = ((NBTTagDouble) nbttaglist.tagAt(0)).data;
        this.lastY = this.bM = this.locY = ((NBTTagDouble) nbttaglist.tagAt(1)).data;
        this.lastZ = this.bN = this.locZ = ((NBTTagDouble) nbttaglist.tagAt(2)).data;
        this.lastYaw = this.yaw = ((NBTTagFloat) nbttaglist2.tagAt(0)).data;
        this.lastPitch = this.pitch = ((NBTTagFloat) nbttaglist2.tagAt(1)).data;
        this.fallDistance = nbttagcompound.getFloat("FallDistance");
        this.fireTicks = nbttagcompound.getShort("Fire");
        //this.setAirTicks(nbttagcompound.getShort("Air"));
        this.onGround = nbttagcompound.getBoolean("OnGround");
        this.setPosition(this.locX, this.locY, this.locZ);
        this.yaw = this.yaw % 360.0F;
        this.pitch = this.pitch % 360.0F;
        this.fromNbt(nbttagcompound);
    }
    
    /** aX(...) */
    protected final String getName() {
        return EntityTypes.b(this);
    }

    protected abstract void fromNbt(NBTTagCompound nbttagcompound);

    protected abstract void toNbt(NBTTagCompound nbttagcompound);

    protected NBTTagList a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.appendTag(new NBTTagDouble((String) null, d0));
        }

        return nbttaglist;
    }

    protected NBTTagList a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.appendTag(new NBTTagFloat((String) null, f));
        }

        return nbttaglist;
    }

    public boolean isAlive() {
        return !this.dead;
    }
}
