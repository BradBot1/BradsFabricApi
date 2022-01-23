package com.bb1.fabric.bfapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
	
	public static final String NAME = "BFAPI";
	
	public static final String ID = "bfapi";
	
	public static final String VERSION = "1.2.2";
	
	public static final String PATH = "com.bb1.fabric";
	
	// these are not auto set so you can put what you want as their value
	
	public static final Logger LOGGER = LogManager.getLogger(ID);
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static final Logger createSubLogger(@NotNull String name) {
		return LogManager.getLogger(ID+" | "+name);
	}
	
}
