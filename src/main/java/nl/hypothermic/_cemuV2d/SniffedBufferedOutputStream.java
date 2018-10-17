package nl.hypothermic._cemuV2d;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import nl.hypothermic.ofts.util.LoggingManager;

public class SniffedBufferedOutputStream extends BufferedOutputStream {

	public SniffedBufferedOutputStream(OutputStream out, int x) {
		super(out, x);
	}
	
	@Override
    public synchronized void write(byte[] buffer, int offset, int length) throws IOException {
        super.write(buffer, offset, length);
        log(buffer);
    }
	
	@Override
    public synchronized void write(byte[] bytes) throws IOException {
        super.write(bytes);
        log(bytes);
    }
	
	@Override
    public synchronized void write(int oneByte) throws IOException {
        super.write(oneByte);
        log(oneByte);
    }
	
	private void log(byte[] bytes) {
		LoggingManager.info("OUT - " + bytes.toString());
	}
	
	//private ArrayList<Integer> buf = new ArrayList<Integer>();
	
	private void log(int bytes) {
		/*if (buf.size() > 32) {
			LoggingManager.info("OUT 1 - " + buf.toString());
			buf.clear();
		} else {
			buf.add(bytes);
		}*/
		System.err.print(bytes + " ");
	}
}
