package com.bb1.api.text;

import java.util.UUID;

import com.bb1.api.Loader;
import com.bb1.api.managers.AbstractManager;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
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
public class TextManager extends AbstractManager<TextProvider> {

	private static final TextManager INSTANCE = new TextManager();
	
	public static TextManager getInstance() { return INSTANCE; }
	
	private TextManager() { }
	
	private PlayerManager manager = Loader.getMinecraftServer().getPlayerManager();
	
	public Text parse(String text, UUID player) { return parse(new LiteralText(text), player); }
	
	public Text parse(Text text, UUID player) {
		ServerPlayerEntity serverPlayerEntity = manager.getPlayer(player);
		Text text2 = text;
		for (TextProvider provider : getProviders()) {
			text2 = provider.parse(text2, serverPlayerEntity);
		}
		return text2;
	}

	@Override
	protected void onRegister(TextProvider provider) { }

}
