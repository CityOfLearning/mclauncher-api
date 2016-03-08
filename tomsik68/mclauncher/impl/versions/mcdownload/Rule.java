package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.regex.Pattern;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.impl.common.Platform;

final class Rule {
	/**
	 * All possible actions for rules
	 */
	// they're pretty self-explanatory. There's really nothing more to write
	// about it.
	// maybe just the fact that Action is not the only thing that decides
	// whether a Rule is effective
	public enum Action {
		ALLOW, DISALLOW
	}

	private final Action action;
	private IOperatingSystem restrictedOs;

	private String restrictedOsVersionPattern;

	public Rule(JSONObject json) {
		this.action = Action.valueOf(json.get("action").toString().toUpperCase());
		if (json.containsKey("os")) {
			JSONObject os = (JSONObject) json.get("os");
			this.restrictedOs = Platform.osByName(os.get("name").toString());
			if (json.containsKey("version")) {
				this.restrictedOsVersionPattern = os.get("version").toString();
			}
		}
	}

	/**
	 *
	 * @return True if this rule is effective, false otherwise
	 */
	public boolean applies() {
		// if there's no OS specified, it applies to all OSs
		if (this.getRestrictedOs() == null) {
			return true;
		} else {
			// if our OS is the restricted OS
			if (this.getRestrictedOs() == Platform.getCurrentPlatform()) {
				// see if there's a version specified
				if (this.restrictedOsVersionPattern == null) {
					// if there's no version, it applies to all versions
					return true;
				} else {
					// if there's a version specified, compile it to a pattern
					// and try to match it against system property "os.version"
					boolean result = Pattern.matches(this.restrictedOsVersionPattern, System.getProperty("os.version"));
					return result;
				}
			} else {
				// our OS is not restricted by this rule
				return false;
			}
		}
	}

	public Action getAction() {
		return this.action;
	}

	public IOperatingSystem getRestrictedOs() {
		return this.restrictedOs;
	}

	public String getRestrictedOsVersionPattern() {
		return this.restrictedOsVersionPattern;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Rule:{");
		sb.append("Action:").append(this.action).append(',');
		if (this.restrictedOs != null) {
			sb.append("OS:").append(this.restrictedOs.getDisplayName()).append(',');
		}
		if (this.restrictedOsVersionPattern != null) {
			sb.append("version:").append(this.restrictedOsVersionPattern);
		}
		sb.append('}');
		return sb.toString();
	}
}
