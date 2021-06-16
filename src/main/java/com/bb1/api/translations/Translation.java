package com.bb1.api.translations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright 2021 BradBot_1
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License", new HashMap<String, String>());
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class Translation {

	private final String translation_key;

	private final Map<String, String> translationMap;

	public Translation(@NotNull String translation_key, @Nullable Map<String, String> translationMap) {
		this.translation_key= translation_key;
		this.translationMap = (translationMap==null) ? new HashMap<String, String>() : translationMap;
	}

	public final String translation_key() { return this.translation_key; }

	public final Map<String, String> translationMap() { return this.translationMap; }

	@Override
	public final boolean equals(Object obj) {
		return obj!=null && obj instanceof Translation && ((Translation) obj).translation_key().equals(translation_key);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(translation_key(), translationMap());
	}

	@Override
	public final String toString() {
		return "Translation[translation_key="+translation_key+"]";
	}
	
	public final TranslatableText translatableText() { return new TranslatableText(translation_key()); }
	
}