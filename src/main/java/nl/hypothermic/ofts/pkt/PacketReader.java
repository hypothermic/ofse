package nl.hypothermic.ofts.pkt;

import java.io.DataInputStream;
import java.io.IOException;

import nl.hypothermic.ofts.pkt.all.Packet0;
import nl.hypothermic.ofts.pkt.all.Packet1;
import nl.hypothermic.ofts.pkt.all.Packet2;
import nl.hypothermic.ofts.pkt.all.Packet202;
import nl.hypothermic.ofts.pkt.all.Packet254;
import nl.hypothermic.ofts.pkt.all.Packet50;
import nl.hypothermic.ofts.pkt.all.Packet51;
import nl.hypothermic.ofts.util.LoggingManager;

public class PacketReader {

	// Read packets as defined in protocol: http://wiki.vg/index.php?title=Protocol&oldid=932

	public static Packet read(DataInputStream dis) {
		Packet temp;
		int i;
		try {
			i = dis.read();
			switch (i) {
			case -1:
				return null;
			case 0:
				return new Packet0();
			case 1:
				return new Packet1();
			case 2:
				String tmp = Packet.readString(dis, 64);
				return new Packet2(tmp);
			case 50:
				return new Packet50();
			case 51:
				return new Packet51(dis);
			case 202:
				return new Packet202();
			case 254:
				return new Packet254();
			}
			LoggingManager.warn("unknown paccket id " + i);
		} catch (IOException x) {
			;
		}
		return null;
	}
}
