package nl.hypothermic.fireloader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FLTestSuite {
	
	public static void main(String args[]) throws IOException {
		FireLoader fl = new FireLoader(new File("world/"));
		System.out.println(fl.getWorldData().getSeed());
		System.out.println(fl.getChunkAt(0, 0));
	}
}
