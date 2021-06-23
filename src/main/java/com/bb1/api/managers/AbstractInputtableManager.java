package com.bb1.api.managers;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.providers.Provider;

public abstract class AbstractInputtableManager<P extends Provider, I> extends AbstractManager<P> {
	
	private final Set<I> input = new HashSet<I>();
	
	protected AbstractInputtableManager() { }
	
	public final void register(I input) {
		this.input.add(input);
		onInput(input);
	}
	
	public final Set<I> getInput() { return this.input; }
	
	protected abstract void onInput(I input);

}
