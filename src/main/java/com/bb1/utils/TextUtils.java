package com.bb1.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TextUtils {
	
	public static List<Text> copyPartialMatches(String token, Collection<Text> collection) {
    	List<Text> set = new ArrayList<Text>();
    	final String check = token.toLowerCase();
    	final int textSize = token.length();
        for (Text string : collection) {
            if (string.asTruncatedString(textSize).toLowerCase().startsWith(check)) {
            	set.add(string);
            }
        }
        return set;
    }
	
	public static List<Text> sortAlphabetically(Collection<Text> collection) {
		List<Text> list = new ArrayList<Text>();
		List<String> list2 = new ArrayList<String>();
		Collections.sort(list2);
		for (String string : list2) {
			list.add(new LiteralText(string));
		}
		return list;
	}
	
	public static Set<Text> convert(Collection<String> text) {
		Set<Text> set = new HashSet<Text>();
		for (String s : text) {
			set.add(new LiteralText(s));
		}
		return set;
	}
	
}
