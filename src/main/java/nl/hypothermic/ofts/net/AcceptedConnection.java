package nl.hypothermic.ofts.net;

import static nl.hypothermic.ofts.util.LoggingManager.*;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import nl.hypothermic.ofts.NetListenThread;
import nl.hypothermic.ofts.Server;
import nl.hypothermic.ofts.game.Player;
import nl.hypothermic.ofts.pkt.Packet;
import nl.hypothermic.ofts.pkt.PacketReader;
import nl.hypothermic.ofts.pkt.all.Packet0;
import nl.hypothermic.ofts.pkt.all.Packet1;
import nl.hypothermic.ofts.pkt.all.Packet2;
import nl.hypothermic.ofts.pkt.all.Packet255;

public class AcceptedConnection extends Thread {

	public AcceptedConnection(Socket socket) {
		this.socket = socket;
	}

	private Socket socket;
	private AcceptedConnection connection = this;
	public volatile boolean isEstablished = false;

	private DataInputStream dis;
	private ReaderThread reader = new ReaderThread();
	private DataOutputStream dos;
	private WriterThread writer = new WriterThread();
	private static volatile LinkedList<Packet> outq = new LinkedList<Packet>();

	private volatile boolean isDisconnecting = false;

	@Override public void run() {
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 5120));
			reader.start();
			writer.start();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public void disconnect() {
		new Thread() {
			@Override public void run() {
				if (isDisconnecting) {
					return;
				}
				isDisconnecting = true;
				try {
					dos.flush();
				} catch (IOException x1) {
					;
				}
				// reader.interrupt();
				// writer.interrupt();
				reader.stop();
				writer.stop();
				try {
					socket.close();
				} catch (IOException x) {
					x.printStackTrace();
				}
				NetListenThread.connlist.remove(connection);
				info("Disconnect complete");
			}
		}.start();
	}

	/**
	 * Warning: unstable, message might not always display to the end user.
	 * 
	 * @param message
	 *            Kick message
	 */
	public void disconnect(final String message) {
		new Thread() {
			@Override public void run() {
				if (isDisconnecting) {
					return;
				}
				isDisconnecting = true;
				try {
					queuePacket(new Packet255(message));
					Thread.sleep(400);
					dos.flush();
					Thread.sleep(200);
				} catch (Exception x1) {
					;
				}
				// reader.interrupt();
				// writer.interrupt();
				reader.stop();
				writer.stop();
				try {
					socket.close();
				} catch (IOException x) {
					x.printStackTrace();
				}
				NetListenThread.connlist.remove(connection);
				info("Disconnect complete");
			}
		}.start();
	}

	public void queuePacket(Packet packet) {
		outq.add(packet);
	}
	
	public void queuePacket(Packet... packets) {
		for (Packet packet : packets) {
			outq.add(packet);
		}
	}

	private class ReaderThread extends Thread {

		private Packet current;
		private Packet react;

		@Override public void run() {
			while (!this.isInterrupted()) {
				current = PacketReader.read(dis);
				if (current != null) {
					if (current.id != 0) {
						info("Received packet " + current.id);
						react = current.react(connection);
						if (react != null) {
							outq.add(react);
						}
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException x) {
					;
				}
			}
		}
	}

	private class WriterThread extends Thread {

		private Packet temp;
		private Packet0 keepAlive;

		@Override public void run() {
			while (!this.isInterrupted()) {
				try {
					if (outq.size() > 0) {
						temp = outq.getFirst();
						info("Writing packet " + temp.id);
						temp.write(dos);
						dos.flush();
						outq.removeFirst();
						// If it's a kick packet, disconnect this connection.
						if (temp instanceof Packet255) {
							temp.write(dos);
							dos.flush();
							disconnect();
						}
					} else if (isEstablished) {
						keepAlive = new Packet0();
						keepAlive.write(dos);
						dos.flush();
					}
				} catch (SocketException se) {
					if (!se.getMessage().contains("Socket closed")) {
						se.printStackTrace();
					}
					disconnect();
					se.printStackTrace();
				} catch (IOException iox) {
					iox.printStackTrace();
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException x) {
					;
				}
			}
		}
	}
}
