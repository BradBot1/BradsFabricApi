package com.bb1.fabric.bfapi.text.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.config.Config;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.TextUtils;
import com.google.common.collect.ImmutableMap;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TextParser {
	
	public static final TextParser GLOBAL = new TextParser(ImmutableMap.of()) {
		// TODO: utilise TEXT_PARSER_LOOKUPS
	};
	/**
	 * Used as {@link #wrappingCharacter}KEY{@link #wrappingCharacter}
	 */
	protected char wrappingCharacter = '$';
	
	protected Map<TextParserKey, TextParserLookup> lookups = new HashMap<TextParserKey, TextParserLookup>();
	
	private TextParserKey[][] keys;
	
	public TextParser() {
		
	}
	
	public TextParser(Map<TextParserKey, TextParserLookup> lookups) {
		this.lookups = lookups;
	}
	
	public void setWrappingCharacter(char c) {
		this.wrappingCharacter = c;
	}
	
	public void addLookup(TextParserKey key, TextParserLookup lu) {
		this.lookups.put(key, lu);
		buildKeys();
	}
	
	public Text parseText(Text text, @Nullable ServerCommandSource recipient) { // TODO: make this not inefficient lol
		AbstractConfigSerializable<Text> ser = Config.getSerializer(Text.class, true);
		// Text.of(null)
		for (TextParserKey[] priority : this.keys) {
			for (TextParserKey key : priority) {
				int occurrence = 0;
				
			}
		}
		/*String jsonStr = GSON.toJson(ser.serialize(Field.of(text)));
		for (TextParserKey[] priority : this.keys) {
			for (TextParserKey key : priority) {
				int occurrence = 0;
				while (jsonStr.contains(key.key())) {
					jsonStr = jsonStr.replaceFirst(key.key(), this.lookups.get(key).lookUp(recipient, key, ++occurrence));
				}
			}
		}
		for (Entry<TextParserKey, TextParserLookup> entry : this.lookups.entrySet()) {
			final String key = entry.getKey().key();
			jsonStr.replaceFirst(key, jsonStr);
		}*/
		return ser.deserialize(Field.of(null));
	}
	
	protected void buildKeys() {
		SortedMap<Integer, List<TextParserKey>> map = new TreeMap<Integer, List<TextParserKey>>();
		for (Entry<TextParserKey, TextParserLookup> entry : this.lookups.entrySet()) {
			final int priority = entry.getKey().priority();
			List<TextParserKey> l = map.getOrDefault(priority, new LinkedList<TextParserKey>());
			l.add(entry.getKey());
			map.putIfAbsent(priority, l);
		}
		this.keys = new TextParserKey[map.size()][];
		int index = this.keys.length;
		for (Entry<Integer, List<TextParserKey>> entry : map.entrySet()) {
			this.keys[index] = new TextParserKey[entry.getValue().size()];
			int index2 = 0;
			for (TextParserKey key : entry.getValue()) {
				this.keys[index][index2++] = key;
			}
			index--;
		}
	}
	
	private Text func(Text value, @Nullable ServerCommandSource recipient) {
		StringBuffer text = new StringBuffer(value.asString());
		for (TextParserKey[] priority : this.keys) {
			for (TextParserKey key : priority) {
				int occurrence = 0;
				// text.
			}
		}
		final MutableText replacement = new LiteralText("");
		for (Text t : value.getSiblings()) {
			replacement.append(t);
		}
		return replacement;
	}
	
}
