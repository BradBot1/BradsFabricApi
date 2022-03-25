package com.bb1.fabric.bfapi.text.parser;

import net.minecraft.util.Identifier;

public record TextParserKey(Identifier key, int priority) {
	
	public String getKeyAsString() {
		return this.key.toString();
	}
	
}
