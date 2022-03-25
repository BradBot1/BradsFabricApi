package com.bb1.fabric.bfapi.utils;

import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.config.Config;

import net.minecraft.text.Text;

public final class TextUtils {
	
	public static final Text clone(Text text) {
		AbstractConfigSerializable<Text> ser = Config.getSerializer(Text.class, true);
		return ser.deserialize(Field.of(ser.serialize(Field.of(text))));
	}
	
}
