package sk.tomsik68.mclauncher.impl.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.servers.FoundServerInfo;
import sk.tomsik68.mclauncher.api.servers.IServerFinder;
import sk.tomsik68.mclauncher.impl.common.Observable;

/**
 * Wrapper for thread that listens for servers on LAN.
 */
public final class VanillaServerFinder extends Observable<FoundServerInfo> implements IServerFinder {
	private static final String SOCKET_GROUP_ADDRESS = "224.0.2.60";
	private Thread thread;
	private final InetAddress broadcastAddress;

	public VanillaServerFinder() throws UnknownHostException {
		this.broadcastAddress = InetAddress.getByName(SOCKET_GROUP_ADDRESS);
	}

	@Override
	public boolean isActive() {
		return this.thread.isAlive();
	}

	@Override
	public void run() {
		MCLauncherAPI.log.fine("Starting server finder...");
		// create socket
		MulticastSocket socket = null;
		byte[] buffer = new byte[1024];
		try {
			// assign it to group
			socket = new MulticastSocket(4445);
			socket.setSoTimeout(5000);
			socket.joinGroup(this.broadcastAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// we will use single builder to build all FoundServerInfo objects
		final FoundServerInfoBuilder builder = new FoundServerInfoBuilder();
		builder.finder(this);
		while ((socket != null) && this.isActive()) {
			// try to receive a packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException ign) {
				continue;
			} catch (Exception e) {
				// TODO: add option to handle this error!
			}
			// if packet was received successfully,

			String recvString = new String(packet.getData(), packet.getOffset(), packet.getLength());
			String motd = ServerStringDecoder.parseProperty(recvString, "MOTD");
			Integer port = Integer.parseInt(ServerStringDecoder.parseProperty(recvString, "AD"));
			MCLauncherAPI.log.finer("Discovered server: '".concat(recvString).concat("'"));
			// we can construct FoundServerInfo using given information
			builder.motd(motd).port(port).ip(packet.getAddress().getHostAddress());
			builder.property("recvString", recvString);
			FoundServerInfo server = builder.build();
			// and notify all observers about it
			this.notifyObservers(server);
		}

	}

	@Override
	public void startFinding() {
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public void stop() {
		this.thread.interrupt();
	}

}
