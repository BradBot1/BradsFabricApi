package com.bb1.fabric.bfapi.permissions;

public enum PermissionLevel {
	
	DEFAULT(0), // everyone will have it by default
	OP_1(1), // lock to op level one
	OP_2(2), // lock to op level two
	OP_3(3), // lock to op level three
	OP_4(4), // lock to op level four
	NODE(99), // only work if the node is present
	;
	
	private final int _opLevel;
	
	PermissionLevel(int opLevel) {
		this._opLevel = opLevel;
	}
	
	public int toInt() {
		return this._opLevel;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
	
	public static PermissionLevel fromString(String s) {
		for (PermissionLevel lvl : values()) {
			if (lvl.toString().equals(s)) {
				return lvl;
			}
		}
		return NODE;
	}
	
}
