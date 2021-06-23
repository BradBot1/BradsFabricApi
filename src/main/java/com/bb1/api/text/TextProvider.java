package com.bb1.api.text;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.providers.Provider;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface TextProvider extends Provider {
	
	public Text parse(@NotNull Text text, @Nullable ServerPlayerEntity serverPlayerEntity);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("TextProvider | "+getProviderName()); }
	
}
