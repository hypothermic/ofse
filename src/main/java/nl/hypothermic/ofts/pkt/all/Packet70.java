package nl.hypothermic.ofts.pkt.all;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;

public class Packet70 extends Packet {

	// 70: BED

	public static final String bedChat[] = {
			"tile.bed.notValid", null, null, "gameMode.changed"
	};

	/**
	 * Either 1 or 2. 1 indicates to begin raining, 2 indicates to stop raining.
	 */
	public int bedState;

	/** Used only when reason = 3. 0 is survival, 1 is creative. */
	public int gameMode;

	public Packet70() {
		super(70);
	}

	public Packet70(int bedState, int gameMode) {
		super(70);
		this.bedState = bedState;
		this.gameMode = gameMode;
	}

	public Packet70(DataInputStream dis) throws IOException {
		super(70);
		this.read(dis);
	}

	@Override public Packet react(AcceptedConnection ac) {
		// TODO
		return null;
	}

	@Override public void read(DataInputStream dis) throws IOException {
		bedState = dis.readByte();
		gameMode = dis.readByte();
	}

	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeByte(this.bedState);
		dos.writeByte(this.gameMode);
	}

	@Override public int getSize() {
		return 2;
	}
}
