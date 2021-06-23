package com.bb1.api.text;

import java.util.UUID;

import com.bb1.api.Loader;
import com.bb1.api.managers.AbstractManager;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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
