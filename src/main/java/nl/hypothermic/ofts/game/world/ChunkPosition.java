package nl.hypothermic.ofts.game.world;

import nl.hypothermic.ofts.util.MathHelper;
import nl.hypothermic.ofts.util.Vec3D;

public class ChunkPosition {

    public final int x;
    public final int y;
    public final int z;

    public ChunkPosition(int i, int j, int k) {
        this.x = i;
        this.y = j;
        this.z = k;
    }

    public ChunkPosition(Vec3D vec3d) {
        this(MathHelper.floor(vec3d.xCoord), MathHelper.floor(vec3d.yCoord), MathHelper.floor(vec3d.zCoord));
    }

    public boolean equals(Object object) {
        if (!(object instanceof ChunkPosition)) {
            return false;
        } else {
            ChunkPosition chunkposition = (ChunkPosition) object;

            return chunkposition.x == this.x && chunkposition.y == this.y && chunkposition.z == this.z;
        }
    }

    public int hashCode() {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}
