package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.util.UUID;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.ISession;

final class YDLogoutRequest implements IJSONSerializable {
	private final ISession session;
	private final UUID clientToken;

	public YDLogoutRequest(ISession session, UUID clientToken) {
		this.session = session;
		this.clientToken = clientToken;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("accessToken", this.session.getSessionID());
		jsonObj.put("clientToken", this.clientToken.toString());
		return jsonObj;
	}

}
