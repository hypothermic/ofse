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
import nl.hypothermic.ofts.game.world.WorldType;
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
	
	public Packet1(DataInputStream dis) throws IOException {
		super(1);
		this.read(dis);
	}

	@Override public Packet react(final AcceptedConnection ac) {
		if (protocolVersion != 29) {
			return new Packet255("Tekkit Classic modpack only. [1]");
		}
		// Handle offline login
		if (!Server.authOnlineMode) {
			Packet1.handleLogin(ac, this, false);
			return null;
		} else {
			// Handle online login and username verification via Mojang servers (not working yet??? TODO test!)
			Packet1 instance = this;
			new Thread() {
				public void run() {
					try {
						URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(username, "UTF-8") + "&serverId=" + URLEncoder.encode(Server.authLoginKey, "UTF-8"));
						BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
						String s1 = bufferedreader.readLine();

						bufferedreader.close();
						if (s1.equals("YES")) {
							Packet1.handleLogin(ac, instance, true);
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

	public static void handleLogin(AcceptedConnection ac, Packet1 packet, boolean isVerified) {
		// --- VANILLA --- //
		Player pl = new Player(ac, packet.username, true);
		Server.players.add(pl);
		LoggingManager.info("Player \"" + pl.username + "\" (pktname=\"" + packet.username + "\", entityId=" + pl.entityId + ") has joined the server.");
		ac.queuePacket(new Packet1(pl.entityId, "", WorldType.NORMAL, Server.gamemode, Server.dimension, (byte) Server.difficulty, (byte) 0, (byte) Server.maxPlayers),
					   new Packet6(1, 72, 1),
					   new Packet202(true, false, true, true),
					   new Packet4(Server.world.getTime()),
					   new Packet13(pl.locX, pl.locY + 1.6200000047683716D, pl.locY, pl.locZ, pl.yaw, pl.pitch, false));
		ac.isEstablished = true;
		pl.startChunkAllocationThread();
		//Server.sendToAllPlayers(new Packet3(pl.username + " has joined the server!"));
		// ---- FORGE ---- // TODO
		/*if (packet.d == 68066119) {
			ForgeHooks.onLogin(manager, pktLogin);

			String[] channels = MessageManager.getInstance().getRegisteredChannels(manager);
			StringBuilder tmp = new StringBuilder();
			tmp.append("Forge");
			for (String channel : channels) {
				tmp.append("\000");
				tmp.append(channel);
			}ew() {
  	  return new ModInventoryView(th
			Packet250 pkt = new Packet250();
			pkt.channel = "REGISTER";
			try {
				pkt.data = tmp.toString().getBytes("UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			pkt.length = (short) pkt.data.length; // TODO: not sure if this int fits into a short.
			ac.queuePacket(pkt);
			NetworkMod[] list = MinecraftForge.getNetworkMods();
			PacketModList pkt = new PacketModList(true);

			for (NetworkMod mod : list) {
				pkt.ModIDs.put(Integer.valueOf(MinecraftForge.getModID(mod)), mod.toString());
			}
			((NetServerHandler) net.getNetHandler()).sendPacket(pkt.getPacket());
		} else {
			ac.disconnect("Tekkit Classic modpack only. [2]"); // User does not have Forge installed
		}*/
		// ----- FML ----- //
		/*Packet250 pkt = new Packet250();
	    pkt.channel = "REGISTER";
	    pkt.data = FMLCommonHandler.instance().getPacketRegistry();
	    pkt.length = (short) pkt.data.length;
	    if (pkt.length > 0) {
	    	ac.queuePacket(packet);
	    }*/
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
			writeString(this.worldType.getName(), dos);
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
			i = this.worldType.getName().length();
		}

		return 4 + this.username.length() + 4 + 7 + 7 + i;
	}

	@Override public String toString() {
		return "Packet1 [protocolVersion=" + this.protocolVersion + ", username=" + this.username + ", worldType=" + this.worldType + ", gamemode=" + this.gamemode + ", dim=" + this.dim + ", diff=" + this.diff + ", g=" + this.g + ", maxPlayers=" + this.maxPlayers + "]";
	}
}
