package nl.hypothermic.ofts.pkt;

import java.io.DataInputStream;
import java.io.IOException;

import nl.hypothermic.ofts.pkt.all.Packet0;
import nl.hypothermic.ofts.pkt.all.Packet1;
import nl.hypothermic.ofts.pkt.all.Packet10;
import nl.hypothermic.ofts.pkt.all.Packet11;
import nl.hypothermic.ofts.pkt.all.Packet13;
import nl.hypothermic.ofts.pkt.all.Packet2;
import nl.hypothermic.ofts.pkt.all.Packet202;
import nl.hypothermic.ofts.pkt.all.Packet250;
import nl.hypothermic.ofts.pkt.all.Packet254;
import nl.hypothermic.ofts.pkt.all.Packet3;
import nl.hypothermic.ofts.pkt.all.Packet30;
import nl.hypothermic.ofts.pkt.all.Packet33;
import nl.hypothermic.ofts.pkt.all.Packet4;
import nl.hypothermic.ofts.pkt.all.Packet50;
import nl.hypothermic.ofts.pkt.all.Packet51;
import nl.hypothermic.ofts.pkt.all.Packet6;
import nl.hypothermic.ofts.pkt.all.Packet70;
import nl.hypothermic.ofts.pkt.all.Packet8;
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
				return new Packet0(dis);
			case 1:
				return new Packet1(dis);
			case 2:
				return new Packet2(dis);
			case 3:
				return new Packet3(dis);
			case 4:
				return new Packet4(dis);
			case 6:
				return new Packet6(dis);
			case 8:
				return new Packet8(dis);
			case 10:
				return new Packet10(dis);
			case 11:
				return new Packet11(dis);
			case 13:
				return new Packet13(dis);
			case 30:
				return new Packet30(dis);
			case 33:
				return new Packet33(dis);
			case 50:
				return new Packet50(dis);
			case 51:
				return new Packet51(dis);
			case 70:
				return new Packet70(dis);
			case 202:
				return new Packet202(dis);
			case 250:
				return new Packet250(dis);
			case 254:
				return new Packet254(dis);
			// don't add 255: server to client only.
			}
			LoggingManager.warn("unknown paccket id " + i);
		} catch (IOException x) {
			x.printStackTrace();
		}
		return null;
	}
}
