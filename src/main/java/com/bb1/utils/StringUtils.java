package com.bb1.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtils {
	
	public static List<String> copyPartialMatches(String token, Collection<String> collection) {
    	List<String> set = new ArrayList<String>();
    	final String check = token.toLowerCase();
        for (String string : collection) {
            if (string.toLowerCase().startsWith(check)) {
            	set.add(string);
            }
        }
        return set;
    }
	
}
