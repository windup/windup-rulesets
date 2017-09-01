package com.something;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.net.URL;

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
	    
	    AsynchronousFileChannel afc = new MyAsynchronousFileChannel();
	    afc.size();
	    FileChannel fc = new MyFileChannel();
	    fc.size();
	    FileLock fl = new MyFileLock();
	    fl.isValid();
	    DirectoryStream ds = new MyDirectoryStream();
	    ds.iterator();
	    FileSystem fsy = FileSystems.getDefault();
	    fsy.close();
	    Path p = Paths.get("foo", "path");
	    p.endsWith("foo");
	    FileStore fs = Files.getFileStore(p);
	    fs.isReadOnly();
	    
	    // the next 2 lines test that rule 'local-storage-00002' is only fired from the 2nd line
	    URL url1 = new URL("url");
	    URL url2 = new URL("file://");
	    new File("D:/file.ext");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void nioPaths() {
	Paths.get("/tmp/lock.id");
    }
    
    private class MyAsynchronousFileChannel extends AsynchronousFileChannel {}
    private class MyFileChannel extends FileChannel {}
    private class MyFileLock extends FileLock {}
    private class MyDirectoryStream implements DirectoryStream {}
    private class MyFileStore extends FileStore {}
    private class MyFileSystem extends FileSystem {}
    private class MyPath implements Path {}
}
