package sk.tomsik68.mclauncher.impl.common;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

final class MacintoshOS implements IOperatingSystem {
	private File workDir;

	@Override
	public String getArchitecture() {
		return System.getProperty("sun.arch.data.model");
	}

	@Override
	public String getDisplayName() {
		return "MAC OS";
	}

	@Override
	public String getMinecraftName() {
		return "osx";
	}

	@Override
	public File getWorkingDirectory() {
		if (workDir != null) {
			return workDir;
		}
		/*
		 * String path = System.getProperty("user.home")+
		 * "/Library/Application Support/minecraft"; try { workDir = new
		 * File(new URI("file:///"+ path.replaceAll(" ", "%20"))); } catch
		 * (URISyntaxException e) { e.printStackTrace(); }
		 */
		workDir = new File(System.getProperty("user.home"), "Library/Application Support/minecraft");
		MCLauncherAPI.log.fine("Minecraft working directory: ".concat(workDir.getAbsolutePath()));
		return workDir;
	}

	@Override
	public boolean isCurrent() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}

}
