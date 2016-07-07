package sk.tomsik68.mclauncher.impl.common;

import java.util.HashSet;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;

public class Observable<E> implements IObservable<E> {
	private final HashSet<IObserver<E>> observers = new HashSet<IObserver<E>>();

	@Override
	public void addObserver(IObserver<E> obs) {
		this.observers.add(obs);
	}

	@Override
	public void deleteObserver(IObserver<E> obs) {
		this.observers.remove(obs);
	}

	@Override
	public void notifyObservers(E changedObj) {
		for (IObserver<E> obs : this.observers) {
			obs.onUpdate(this, changedObj);
		}
	}

}
