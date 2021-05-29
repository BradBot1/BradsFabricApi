package com.bb1.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {
	
	public static <T> Set<T> convert(List<T> list) {
		Set<T> set = new HashSet<T>();
		for (T t : list) {
			set.add(t);
		}
		return set;
	}
	
	public static <T> List<T> convert(Set<T> set) {
		List<T> list = new ArrayList<T>();
		for (T t : set) {
			set.add(t);
		}
		return list;
	}
	
}
