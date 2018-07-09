package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet2 extends Packet {
	
	// 2: HANDSHAKE
	
	public String data;
	
	public Packet2() {
		super(2);
	}
	
	public Packet2(String data) {
		super(2);
		this.data = data;
	}
	
	public String getUsername() {
		return data.split(";")[0];
	}
	
	public String getRequestedHost() {
		return data.split(";")[1];
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		if (Server.authOnlineMode) {
            Server.authLoginKey = Long.toString(Server.random.nextLong(), 16);
            return new Packet2(Server.authLoginKey);
        } else {
            return new Packet2("-");
        }
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		if (data == null) {
			data = readString(dis, 64);
		}
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		writeString(data, dos);
	}

	@Override public int getSize() {
		return 8 + this.data.length();
	}
}
