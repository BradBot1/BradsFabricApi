package com.bb1.api.config.command;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.config.Config;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TabableConfigList implements ITabable {

	@Override
	public String getTabableName() {
		return "config";
	}

	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		if (refreshTime<System.currentTimeMillis()) {
			refreshTime = System.currentTimeMillis()+15000l;
			configs.clear();
		}
		if (configs.get()==null) {
			configs = new SoftReference<List<Text>>(getConfigs());
		}
		return configs.get();
	}

	// Caching

	/** Used so if ram is full this is cleared first as it is not constantly needed */
	private SoftReference<List<Text>> configs = new SoftReference<List<Text>>(getConfigs());
	/** Used to force the configs to refresh every 15 seconds */
	private long refreshTime = 0;
	
	public List<Text> getConfigs() {
		List<Text> list = new ArrayList<Text>();
		File directory = new File(Config.CONFIG_DIRECTORY);
		if (directory.exists()) {
			File[] configs = directory.listFiles();
			if (configs==null || configs.length<1) return list;
			for (File config : configs) {
				if (config==null || !config.exists() || !config.isFile()) continue;
				list.add(new LiteralText(config.getName().replaceFirst("[\\.]\\w+$", ""))); // Remove last index
			}
		}
		return list;
	}

}
