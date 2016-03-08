package sk.tomsik68.mclauncher.impl.common;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

final class UnknownOS implements IOperatingSystem {
	private File workDir;

	@Override
	public String getArchitecture() {
		return System.getProperty("sun.arch.data.model");
	}

	@Override
	public String getDisplayName() {
		return "Unknown";
	}

	@Override
	public String getMinecraftName() {
		return "unknown";
	}

	@Override
	public File getWorkingDirectory() {
		if (this.workDir != null) {
			return this.workDir;
		}
		String userHome = System.getProperty("user.home");
		this.workDir = new File(userHome, ".minecraft");
		MCLauncherAPI.log.fine("Minecraft working directory: ".concat(this.workDir.getAbsolutePath()));
		return this.workDir;
	}

	@Override
	public boolean isCurrent() {
		// this is determined in OperatingSystems
		return false;
	}

}
