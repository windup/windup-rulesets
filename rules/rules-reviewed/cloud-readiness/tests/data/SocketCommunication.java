import java.net.Socket;
import java.net.MulticastSocket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.channels.NetworkChannel;
import java.nio.channels.MulticastChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import java.net.ServerSocket;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.ServerSocketChannel;

public class SocketCommunication {
	
	private class MySocket extends Socket {}
	private class MyMulticastSocket extends MulticastSocket {}
	private class MyDatagramSocket extends DatagramSocket {}
	private class MyInetSocketAddress extends InetSocketAddress {}
	private class MyNetworkChannel implements NetworkChannel {}
	private class MyMulticastChannel implements MulticastChannel {}
	private class MyDatagramChannel extends DatagramChannel {}
	private class MyAsynchronousSocketChannel extends AsynchronousSocketChannel {}
	private class MySocketChannel extends SocketChannel {}
	
	private class MyServerSocket extends ServerSocket {}
	private class MyAsynchronousServerSocketChannel extends AsynchronousServerSocketChannel {}
	private class MyServerSocketChannel extends ServerSocketChannel {}
	
	public static void main(String argv[]) {
		Socket s = new Socket();
		s.getInputStream();
		MulticastSocket ms = new MulticastSocket();
		ms.close();
		DatagramSocket ds = new DatagramSocket();
		ds.close();
		InetSocketAddress isa = new MyInetSocketAddress();
		isa.isUnresolved();
		NetworkChannel nc = new MyNetworkChannel();
		nc.getLocalAddress();
		MulticastChannel mc = new MyMulticastChannel();
		mc.close();
		DatagramChannel dc =  new MyDatagramChannel();
		dc.getLocalAddress();
		AsynchronousSocketChannel asc = new MyAsynchronousSocketChannel();
		asc.getLocalAddress();
		SocketChannel sc = new MySocketChannel();
		sc.isConnected();
		
		ServerSocket ss = new ServerSocket();
		ss.close();
		AsynchronousServerSocketChannel assd = new MyAsynchronousServerSocketChannel();
		assd.getLocalAddress();
		ServerSocketChannel ssc = new MyServerSocketChannel();
		ssc.getLocalAddress();
	}
}