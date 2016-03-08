package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.impl.common.Observable;

final class MCDownloadLocalVersionList extends Observable<String> implements IVersionList {
	private final File versionsFolder;

	public MCDownloadLocalVersionList(MinecraftInstance mc) {
		this.versionsFolder = new File(mc.getLocation(), "versions");
	}

	@Override
	public LatestVersionInformation getLatestVersionInformation() throws Exception {
		/*
		 * for now, we'll return null. proper solution would be to list all
		 * versions, compare their release time etc but we won't be needing
		 * latest version from this source...
		 */
		return null;
	}

	private File getVersionFolder(String id) {
		return new File(this.versionsFolder, id);
	}

	@Override
	public IVersion retrieveVersionInfo(String id) throws Exception {
		File versionFolder = this.getVersionFolder(id);
		if (!versionFolder.exists()) {
			MCLauncherAPI.log.fine("version folder at '".concat(versionFolder.getAbsolutePath())
					.concat("' doesn't exist or is invalid."));
			return null;
		}
		File jsonFile = new File(versionFolder, versionFolder.getName().concat(".json"));
		MCDownloadVersion result = new MCDownloadVersion((JSONObject) JSONValue.parse(new FileReader(jsonFile)));
		return result;
	}

	@Override
	public void startDownload() throws Exception {
		if (!this.versionsFolder.exists() || this.versionsFolder.isFile()) {
			MCLauncherAPI.log.fine("'versions' folder at '".concat(this.versionsFolder.getAbsolutePath())
					.concat("' doesn't exist or is invalid."));
			return;
		}
		File[] versionFolders = this.versionsFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		for (File versionFolder : versionFolders) {
			// do a quick check whether or not the needed JSON file exists
			File jsonFile = new File(versionFolder, versionFolder.getName().concat(".json"));
			if (jsonFile.exists()) {
				this.notifyObservers(versionFolder.getName());
			}
		}
	}
}
