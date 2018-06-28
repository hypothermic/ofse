package nl.hypothermic.ofts.pkt.all;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.game.Player;
import nl.hypothermic.ofts.game.WorldType;
import nl.hypothermic.ofts.net.AcceptedConnection;
import nl.hypothermic.ofts.pkt.Packet;
import nl.hypothermic.ofts.util.LoggingManager;

public class Packet1 extends Packet {
	
	// 1: LOGIN
	
	public int protocolVersion; // 1.2.5 == 29
	public String username;
    public WorldType worldType;
    public int gamemode;
    public int dim;
    public byte diff;
    public byte g; // unused, remains 0
    public byte maxPlayers;

	public Packet1() {
		super(1);
	}
	
	public Packet1(int protocolVersion, String username, WorldType worldType, int gamemode, int dimension, byte difficulty, byte unused, byte maxPlayers) {
		super(1);
        this.username = username;
        this.protocolVersion = protocolVersion;
        this.worldType = worldType;
        this.dim = dimension;
        this.diff = difficulty;
        this.gamemode = gamemode;
        this.g = unused;
        this.maxPlayers = maxPlayers;
    }
	
	@Override public Packet react(final AcceptedConnection ac) {
		if (protocolVersion != 29) {
			return new Packet255("Tekkit Classic modpack only.");
		}
		// Handle offline login
		if (!Server.authOnlineMode) {
			ac.isEstablished = true;
			Player pl = new Player(ac, this.username, false);
			Server.players.add(pl);
			LoggingManager.info("Player \"" + pl.username + "\" (pktname=\"" + this.username + "\", entityId=" + pl.entityId + ") has joined the server.");
			ac.queuePacket(new Packet1(pl.entityId, "", WorldType.NORMAL, Server.gamemode, Server.dimension, (byte) Server.difficulty, (byte) 0, (byte) Server.maxPlayers));
			ac.queuePacket(new Packet6(16, 64, 16));
			return null;
		} else {
		// Handle online login and username verification via Mojang servers (not working yet??? TODO test!)
			new Thread() {
				public void run() {
			        try {
			            URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(username, "UTF-8") + "&serverId=" + URLEncoder.encode(Server.authLoginKey, "UTF-8"));
			            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
			            String s1 = bufferedreader.readLine();

			            bufferedreader.close();
			            if (s1.equals("YES")) {
			            	ac.isEstablished = true;
			    			Player pl = new Player(ac, username, true);
			    			Server.players.add(pl);
			    			LoggingManager.info("Verified player \"" + pl.username + "\" (pktname=\"" + username + "\", entityId=" + pl.entityId + ") has joined the server.");
			    			ac.queuePacket(new Packet1(pl.entityId, "", WorldType.NORMAL, Server.gamemode, Server.dimension, (byte) Server.difficulty, (byte) 0, (byte) Server.maxPlayers));
			    			ac.queuePacket(new Packet6(16, 64, 16));
			            } else {
			                ac.queuePacket(new Packet255("Authentication error: Failed to verify username!"));
			            }
			        } catch (Exception exception) {
			        	ac.queuePacket(new Packet255("Internal authentication error [" + exception + "]"));
			            exception.printStackTrace();
			        }
			    }
			}.start();
			return null;
		}
	}
	
	@Override public void read(DataInputStream dis) throws IOException {
		this.protocolVersion = dis.readInt();
        this.username = readString(dis, 16);
        
        String s = readString(dis, 16);
        this.worldType = WorldType.getType(s);
        if (this.worldType == null) {
            this.worldType = WorldType.NORMAL;
        }
        
        this.gamemode = dis.readInt();
        this.dim = dis.readInt();
        this.diff = dis.readByte();
        this.g = dis.readByte();
        this.maxPlayers = dis.readByte();
	}
	
	@Override public void write(DataOutputStream dos) throws IOException {
		dos.write(id);
		dos.writeInt(this.protocolVersion);
        writeString(this.username, dos);
        if (this.worldType == null) {
            writeString("", dos);
        } else {
            writeString(this.worldType.name(), dos);
        }

        dos.writeInt(this.gamemode);
        dos.writeInt(this.dim);
        dos.writeByte(this.diff);
        dos.writeByte(this.g);
        dos.writeByte(this.maxPlayers);
	}

	@Override public int getSize() {
		int i = 0;

        if (this.worldType != null) {
            i = this.worldType.name().length();
        }

        return 4 + this.username.length() + 4 + 7 + 7 + i;
	}
}
