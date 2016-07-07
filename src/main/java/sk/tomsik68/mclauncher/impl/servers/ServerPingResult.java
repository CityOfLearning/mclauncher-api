package sk.tomsik68.mclauncher.impl.servers;

public final class ServerPingResult {
	private boolean successful = false;
	private Throwable error = null;
	private PingedServerInfo info;

	ServerPingResult(PingedServerInfo info) {
		this(info, null, true);
	}

	private ServerPingResult(PingedServerInfo info, Throwable err, boolean successful) {
		error = err;
		this.info = info;
		this.successful = successful;
	}

	ServerPingResult(Throwable error) {
		this(null, error, false);
	}

	public Throwable getError() {
		return error;
	}

	public PingedServerInfo getInfo() {
		return info;
	}

	public boolean isSuccessful() {
		return successful;
	}

}
