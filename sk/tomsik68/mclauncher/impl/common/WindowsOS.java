package sk.tomsik68.mclauncher.impl.common;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

final class WindowsOS implements IOperatingSystem {
	private File workDir; // cached working directory

	@Override
	public String getArchitecture() {
		return System.getProperty("sun.arch.data.model");
	}

	@Override
	public String getDisplayName() {
		return "Microsoft Windows";
	}

	@Override
	public String getMinecraftName() {
		return "windows";
	}

	@Override
	public File getWorkingDirectory() {
		if (this.workDir != null) {
			return this.workDir;
		}
		String appData = System.getenv("APPDATA");
		if (appData != null) {
			this.workDir = new File(appData, ".minecraft");
		} else {
			this.workDir = new File(System.getProperty("user.home"), ".minecraft");
		}
		MCLauncherAPI.log.fine("Minecraft working directory: ".concat(this.workDir.getAbsolutePath()));
		return this.workDir;
	}

	@Override
	public boolean isCurrent() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
