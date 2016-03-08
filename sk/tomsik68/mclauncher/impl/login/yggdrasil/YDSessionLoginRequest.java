package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;

final class YDSessionLoginRequest implements IJSONSerializable {
	private final String sessionID, clientToken;

	public YDSessionLoginRequest(String sessid, String clientToken) {
		this.sessionID = sessid;
		this.clientToken = clientToken;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("clientToken", this.clientToken);
		json.put("accessToken", this.sessionID);
		json.put("selectedProfile", null);
		return json;
	}

}
