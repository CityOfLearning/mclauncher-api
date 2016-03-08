package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.impl.common.Observable;

/**
 * Unified version list for {@link MCDownloadVersion}s. Contains local versions
 * as well as remote.
 */
public final class MCDownloadVersionList extends Observable<String> implements IVersionList, IObserver<String> {
	private final MCDownloadLocalVersionList localVersionList;
	private final MCDownloadOnlineVersionList onlineVersionList;

	/**
	 * Creates new MCDownloadVersionList which fetches local JSON files from
	 * given minecraft instance
	 * 
	 * @param mc
	 *            where to fetch JSON files from
	 */
	public MCDownloadVersionList(MinecraftInstance mc) {
		this.onlineVersionList = new MCDownloadOnlineVersionList();
		this.localVersionList = new MCDownloadLocalVersionList(mc);

		this.onlineVersionList.addObserver(this);
		this.localVersionList.addObserver(this);
	}

	@Override
	public LatestVersionInformation getLatestVersionInformation() throws Exception {
		return this.onlineVersionList.getLatestVersionInformation();
	}

	@Override
	public void onUpdate(IObservable<String> observable, String changed) {
		this.notifyObservers(changed);
	}

	void resolveInheritance(MCDownloadVersion version) throws Exception {
		// version's parent needs to be resolved first
		if (version.getInheritsFrom() != null) {
			MCDownloadVersion parent = (MCDownloadVersion) this.retrieveVersionInfo(version.getInheritsFrom());
			this.resolveInheritance(parent);
			version.doInherit(parent);
		}
	}

	@Override
	public IVersion retrieveVersionInfo(String id) throws Exception {
		IVersion result;
		result = this.localVersionList.retrieveVersionInfo(id);
		if (result == null) {
			result = this.onlineVersionList.retrieveVersionInfo(id);
		}

		if (result != null) {
			this.resolveInheritance((MCDownloadVersion) result);
		}
		return result;
	}

	@Override
	public void startDownload() throws Exception {
		this.localVersionList.startDownload();
		this.onlineVersionList.startDownload();
	}
}
