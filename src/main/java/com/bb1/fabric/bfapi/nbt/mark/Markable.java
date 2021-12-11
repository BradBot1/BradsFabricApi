package com.bb1.fabric.bfapi.nbt.mark;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.utils.ExceptionWrapper;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Used to denote a class as markable
 * 
 * @author BradBot_1
 */
public interface Markable {
	
	public static @Nullable Markable getMarkable(@Nullable Object input) {
		return ExceptionWrapper.executeWithReturn(input, (i)->(Markable)i);
	}
	
	public static final String MARK_TAG = "Marks";
	
	public void applyMark(String mark);
	
	public boolean hasMark(String mark);
	
	public Set<String> getMarks();
	
}
