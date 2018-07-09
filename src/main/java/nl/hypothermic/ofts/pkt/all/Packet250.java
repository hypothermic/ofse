package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet250 extends Packet {

	// 250: CUSTOM PAYLOAD (forge & bukkit plugins)

	public String channel;

	public short length; // ofse: int -> short
	public byte[] data;

	public Packet250() {
		super(250);
	}
	
	public Packet250(DataInputStream dis) throws IOException {
		super(250);
		this.read(dis);
	}

	@Override public Packet react(AcceptedConnection ac) {
		return null;
	}

	@Override public void read(DataInputStream dis) throws IOException {
		this.channel = readString(dis, 16);
		this.length = dis.readShort();

		//if (length > 0 && length < 32767) { // ofse: removed *if*
			data = new byte[length];
			dis.readFully(data);
		//}
	}

	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		writeString(channel, dos);
		dos.writeShort((short) length);

		if (data != null) {
			dos.write(data);
		}
	}

	@Override public int getSize() {
		return 4 + channel.length() * 2 + length;
	}
}
