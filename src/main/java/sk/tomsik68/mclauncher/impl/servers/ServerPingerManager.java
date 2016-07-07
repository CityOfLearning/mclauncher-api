package sk.tomsik68.mclauncher.impl.servers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

public final class ServerPingerManager {
	private static final int THREAD_COUNT = 2;
	private static final ServerPingerManager instance = new ServerPingerManager();

	public static final ServerPingerManager getInstance() {
		return instance;
	}

	private final ExecutorService threadPool;

	private ServerPingerManager() {
		threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
	}

	//////////////////////////////////////////////////////////////////////////////

	public Future<ServerPingResult> pingServer(ServerInfo server) {
		return threadPool.submit(new ServerPinger(server, new Protocol47PingPacketFactory()));
	}

}
