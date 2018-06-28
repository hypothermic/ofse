package nl.hypothermic.ofts.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.net.AcceptedConnection;

public abstract class Packet {

	public final int id;

	public Packet(final int id) {
		this.id = id;
	}

	public abstract Packet react(AcceptedConnection ac);

	public abstract void read(DataInputStream dis) throws IOException;

	public abstract void write(DataOutputStream dos) throws IOException;

	/**
	 * Get packet's size in bytes
	 * 
	 * @return packet size in bytes
	 */
	public abstract int getSize();

	// ----------------------- //

	/**
	 * Reads a String from the DataInputStream
	 */
	public static String readString(DataInputStream datainputstream, int i) throws IOException {
		short short1 = datainputstream.readShort();

		if (short1 > i) {
			throw new IOException("Received string length longer than maximum allowed (" + short1 + " > " + i + ")");
		} else if (short1 < 0) {
			throw new IOException("Received string length is less than zero! Weird string!");
		} else {
			StringBuilder stringbuilder = new StringBuilder();

			for (int j = 0; j < short1; ++j) {
				stringbuilder.append(datainputstream.readChar());
			}

			return stringbuilder.toString();
		}
	}

	/**
	 * Writes a String to the DataOutputStream
	 */
	public static void writeString(String string, DataOutputStream dos) throws IOException {
		if (string.length() > 32767) {
			throw new IOException("String too large");
		} else {
			dos.writeShort(string.length());
			dos.writeChars(string);
		}
	}
}
