package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;

final class YDPasswordLoginRequest implements IJSONSerializable {
	private static final JSONObject agentObj = new JSONObject();

	static {
		agentObj.put("name", "Minecraft");
		agentObj.put("version", 1);
	}

	private final String user, pass, token;

	public YDPasswordLoginRequest(String username, String password, String token) {
		this.user = username;
		this.pass = password;
		this.token = token;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("agent", agentObj);
		obj.put("clientToken", this.token);
		obj.put("username", this.user);
		obj.put("password", this.pass);
		return obj;
	}

}
