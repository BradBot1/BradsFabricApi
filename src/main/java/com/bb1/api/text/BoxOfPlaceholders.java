package com.bb1.api.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.bb1.api.adapters.AbstractAdapter;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.TextParser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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
public class BoxOfPlaceholders extends AbstractAdapter implements TextProvider {

	public BoxOfPlaceholders() { super("placeholder-api"); }

	@Override
	protected void load() { TextManager.getInstance().registerProvider(this); }
	
	@Override
	public Text parse(@NotNull Text text, @Nullable ServerPlayerEntity player) {
		return TextParser.parse(placeHolder(text, player).asTruncatedString(256));
	}
	
	private Text placeHolder(@NotNull Text text, @Nullable ServerPlayerEntity player) {
		if (player==null) return PlaceholderAPI.parseText(text, Loader.getMinecraftServer());
		return PlaceholderAPI.parseText(text, player);
	}

}
