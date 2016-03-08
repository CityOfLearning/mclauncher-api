package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;

/**
 * Any response we can get from YggDrassil login system
 */
class YDResponse {
	private String error, message;

	public YDResponse(JSONObject json) {
		if (json.containsKey("error")) {
			this.setError(json.get("error").toString());
		}
		if (json.containsKey("errorMessage")) {
			this.setMessage(json.get("errorMessage").toString());
		}
	}

	final String getError() {
		return this.error;
	}

	final String getMessage() {
		return this.message;
	}

	final void setError(String error) {
		this.error = error;
	}

	final void setMessage(String message) {
		this.message = message;
	}
}
