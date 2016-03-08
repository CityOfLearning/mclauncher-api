package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.util.FileUtils;

public final class YDProfileIO implements IProfileIO {
	private final File dest;

	public YDProfileIO(File mcInstance) {
		this.dest = new File(mcInstance, "launcher_profiles.json");
	}

	@Override
	public IProfile[] read() throws Exception {
		JSONObject root = (JSONObject) JSONValue.parse(new FileReader(this.dest));

		JSONObject authDatabase = (JSONObject) root.get("authenticationDatabase");
		IProfile[] result = new IProfile[authDatabase.size()];
		int i = 0;
		for (String key : authDatabase.keySet()) {
			result[i] = new YDAuthProfile((JSONObject) authDatabase.get(key));
			++i;
		}
		return result;
	}

	@Override
	public void write(IProfile[] profiles) throws Exception {
		JSONObject jRoot, authDb;
		if (!this.dest.exists()) {
			FileUtils.createFileSafely(this.dest);
			jRoot = new JSONObject();
			authDb = new JSONObject();
		} else {
			MCLauncherAPI.log
					.fine("Existing profile storage file found. Loading profiles in case they would be overwritten.");
			jRoot = (JSONObject) JSONValue.parse(new FileInputStream(this.dest));
			authDb = (JSONObject) jRoot.get("authenticationDatabase");
		}

		for (IProfile p : profiles) {
			if (!(p instanceof YDAuthProfile)) {
				throw new IllegalArgumentException("You can only save YDAuthProfile with this system!");
			}
			YDAuthProfile profile = (YDAuthProfile) p;
			authDb.put(profile.getUUID().replace("-", ""), profile.toJSON());
		}
		jRoot.put("authenticationDatabase", authDb);
		FileWriter fw = new FileWriter(this.dest);
		jRoot.writeJSONString(fw, JSONStyle.NO_COMPRESS);
		fw.flush();
		fw.close();
	}

}
