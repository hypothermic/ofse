package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet0 extends Packet {
	
	// 0: KEEP ALIVE
	// warning: as you can see by all the comments, this is for some reason a very funky and buggy packet, please don't use and ignore them if notchian client sends.

	public Packet0() {
		super(0);
	}
	
	public Packet0(DataInputStream dis) throws IOException {
		super(0);
		this.read(dis);
	}
	
	@Override public Packet react(AcceptedConnection ac) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		// we *should* be checking if the response matches our last random number we sent,
		// but it doesn't matter much for now. see documentation.
		
		// reminder not to fuck up again because this took me 4 months to figure out: be sure to actually read the packet!!!!!!
		dis.readInt();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		
		// ---- debug TODO - use example no. for now
		//dos.writeInt(Server.random.nextInt());
		dos.writeInt(957759560);
	}

	@Override public int getSize() {
		return 4;
	}

	@Override public String toString() {
		return "Packet0 [id=" + this.id + "]";
	}
}
