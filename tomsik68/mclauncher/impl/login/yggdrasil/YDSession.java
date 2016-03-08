package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.util.List;

import sk.tomsik68.mclauncher.api.login.ESessionType;
import sk.tomsik68.mclauncher.api.login.ISession;

final class YDSession implements ISession {
	private final String username, sessid, uuid;
	private final YDUserObject user;

	public YDSession(YDLoginResponse r) {
		this.username = r.getSelectedProfile().getName();
		this.sessid = r.getSessionID();
		this.uuid = r.getSelectedProfile().getId();
		this.user = r.getUserObject();
	}

	@Override
	public List<Prop> getProperties() {
		return this.user.getProperties();
	}

	@Override
	public String getSessionID() {
		return this.sessid;
	}

	@Override
	public ESessionType getType() {
		// TODO it doesn't have to be mojang
		return ESessionType.MOJANG;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	YDUserObject getUserObject() {
		return this.user;
	}

	@Override
	public String getUUID() {
		return this.uuid;
	}
}
