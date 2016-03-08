package sk.tomsik68.mclauncher.backend;

import java.util.ArrayList;
import java.util.List;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;

final class AddToListObserver implements IObserver<String> {
	private final List<String> list;

	AddToListObserver() {
		this.list = new ArrayList<String>();
	}

	public List<String> getList() {
		return this.list;
	}

	@Override
	public void onUpdate(IObservable<String> observable, String changed) {
		this.list.add(changed);
	}
}
