package nl.hypothermic.ofts;

import static nl.hypothermic.ofts.util.LoggingManager.*;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import nl.hypothermic.ofts.net.AcceptedConnection;

public class NetListenThread extends Thread {
	
	private ServerSocket ss;
	public static ArrayList<AcceptedConnection> connlist = new ArrayList<AcceptedConnection>();
	
	public NetListenThread() {
		try {
			ss = new ServerSocket(Server.netLocalPort);
		} catch (BindException be) {
			crit("Port " + Server.netLocalPort + " is already in use!");
		} catch (IOException iox) {
			iox.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (!this.isInterrupted()) {
			Socket socket = null;
			try {
	            socket = ss.accept();
	            try {
                    sleep(100);
                } catch (InterruptedException interruptedexception) {
                    ;
                }
	        } catch (IOException iox) {
	            iox.printStackTrace();
	        }
			if (socket != null) {
				new AcceptedConnection(socket).start();
				info(socket.getInetAddress() + ":" + socket.getPort() + " has connected!");
			}
		}
	}
}
