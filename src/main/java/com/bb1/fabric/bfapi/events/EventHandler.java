package com.bb1.fabric.bfapi.events;

import com.bb1.fabric.bfapi.utils.Inputs.Input;

@FunctionalInterface
public interface EventHandler<I extends Input<?>> {
	
	public void handle(I input);
	
}
