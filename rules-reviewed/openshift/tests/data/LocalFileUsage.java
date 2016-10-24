package com.something;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

public class LocalFileUsage {

    final static String filename = "C:/some.tmp";

    final static String filename1 = "/tmp/output/123.txt";

    public static void main(String[] args) {
	try {
	    RandomAccessFile randomFile1 = new RandomAccessFile("/tmp/some.tmp", "rwd");
	    RandomAccessFile randomFile2 = new RandomAccessFile(new File("/tmp/some.tmp"), "rwd");
	    new PrintWriter(filename1);

	    new PrintStream(filename);

	    new FileWriter("file.txt");

	    new FileReader(filename);
            new FileReader("/var/opt/log");
            

	    new ZipFile("/tmp/compressesd.zip");

	    File.createTempFile("test", "tmp");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void nioPaths() {
	Paths.get("/tmp/lock.id");
    }
}
