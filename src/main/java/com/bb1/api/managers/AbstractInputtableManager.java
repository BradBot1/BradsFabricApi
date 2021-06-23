package com.bb1.api.managers;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.providers.Provider;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", "");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http;//www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
