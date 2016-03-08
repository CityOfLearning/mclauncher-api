package sk.tomsik68.mclauncher.impl.login.legacy;

import java.util.List;

import sk.tomsik68.mclauncher.api.login.ESessionType;
import sk.tomsik68.mclauncher.api.login.ISession;

final class LegacySession implements ISession {
	private final String userName, sessionID, uuid, downloadTicket, lastVersion;

	public LegacySession(String user, String sessid, String uuid, String dlTicket, String lVersion) {
		this.userName = user;
		this.sessionID = sessid;
		this.uuid = uuid;
		this.downloadTicket = dlTicket;
		this.lastVersion = lVersion;
	}

	public String getDownloadTicket() {
		return this.downloadTicket;
	}

	public String getLastVersion() {
		return this.lastVersion;
	}

	@Override
	public List<Prop> getProperties() {
		return null;
	}

	@Override
	public String getSessionID() {
		return this.sessionID;
	}

	@Override
	public ESessionType getType() {
		return ESessionType.LEGACY;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public String getUUID() {
		return this.uuid;
	}

}
