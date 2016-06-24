package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.util.List;

import sk.tomsik68.mclauncher.api.login.ESessionType;
import sk.tomsik68.mclauncher.api.login.ISession;

final class YDSession implements ISession {
	private final String username, sessid, uuid;
	private final YDUserObject user;

	public YDSession(YDLoginResponse r) {
		username = r.getSelectedProfile().getName();
		sessid = r.getSessionID();
		uuid = r.getSelectedProfile().getId();
		user = r.getUserObject();
	}

	@Override
	public List<Prop> getProperties() {
		return user.getProperties();
	}

	@Override
	public String getSessionID() {
		return sessid;
	}

	@Override
	public ESessionType getType() {
		// TODO it doesn't have to be mojang
		return ESessionType.MOJANG;
	}

	@Override
	public String getUsername() {
		return username;
	}

	YDUserObject getUserObject() {
		return user;
	}

	@Override
	public String getUUID() {
		return uuid;
	}
}
