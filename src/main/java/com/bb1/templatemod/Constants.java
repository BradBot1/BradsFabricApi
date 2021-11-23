package com.bb1.templatemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Constants {
	
	public static final String NAME = "${name}";
	
	public static final String ID = "${id}";
	
	public static final String VERSION = "${version}";
	
	public static final String PATH = "${path}";
	
	// these are not auto set so you can put what you want as their value
	
	public static final Logger LOGGER = LogManager.getLogger(ID);
	
}
