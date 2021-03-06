package nl.hypothermic.ofts.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase
{
    public int[] field_48447_a;

    public NBTTagIntArray(String par1Str)
    {
        super(par1Str);
    }

    public NBTTagIntArray(String par1Str, int[] par2ArrayOfInteger)
    {
        super(par1Str);
        this.field_48447_a = par2ArrayOfInteger;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput par1DataOutput) throws IOException
    {
        par1DataOutput.writeInt(this.field_48447_a.length);

        for (int var2 = 0; var2 < this.field_48447_a.length; ++var2)
        {
            par1DataOutput.writeInt(this.field_48447_a[var2]);
        }
    }

    /**
     * Read the actual data contents of the tag, implemented in NBT extension classes
     */
    void load(DataInput par1DataInput) throws IOException
    {
        int var2 = par1DataInput.readInt();
        this.field_48447_a = new int[var2];

        for (int var3 = 0; var3 < var2; ++var3)
        {
            this.field_48447_a[var3] = par1DataInput.readInt();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return (byte)11;
    }

    public String toString()
    {
        return "[" + this.field_48447_a.length + " bytes]";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        int[] var1 = new int[this.field_48447_a.length];
        System.arraycopy(this.field_48447_a, 0, var1, 0, this.field_48447_a.length);
        return new NBTTagIntArray(this.getName(), var1);
    }

    public boolean equals(Object par1Obj)
    {
        if (!super.equals(par1Obj))
        {
            return false;
        }
        else
        {
            NBTTagIntArray var2 = (NBTTagIntArray)par1Obj;
            return this.field_48447_a == null && var2.field_48447_a == null || this.field_48447_a != null && this.field_48447_a.equals(var2.field_48447_a);
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(this.field_48447_a);
    }
}
