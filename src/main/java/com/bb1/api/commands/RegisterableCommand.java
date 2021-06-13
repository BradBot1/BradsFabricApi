package com.bb1.api.commands;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.Loader;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.providers.PermissionProvider;
import com.bb1.api.translations.DefaultTranslations;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class RegisterableCommand {
	
	protected Command c;
	
	protected RegisterableCommand(Command c) {
		this.c = c;
	}
	
	public Command getInner() {
		return c;
	}
	
	public String[] copyAfter(String[] params, int index) {
		String[] s = new String[params.length-(index+1)];
		for (int i = 0; i < s.length; i++) {
			s[i] = params[i+index+1];
		}
		return s;
	}
	
	public int execute(ServerCommandSource commandSource, String commandLabel, String[] args) {
		ServerPlayerEntity player = Loader.getServerPlayerEntity(commandSource);
		for (int i = 0; i < args.length; i++) {
			try {
				ITabable t = c.getParams()[i];
				if (t instanceof TabableSubCommand) {
					TabableSubCommand ts = (TabableSubCommand) t;
					SubCommand sub = ts.getSubCommand(args[i]);
					PermissionProvider provider = Loader.getProvider(PermissionProvider.class);
					if (provider!=null && !provider.hasPermission(player, sub.getPermission().permission())) {
						commandSource.sendFeedback(DefaultTranslations.NEED_PERMISSIONS, false);
						return 0;
					}
					return sub.execute(commandSource, commandLabel, copyAfter(args, i));
				} else {
					continue;
				}
			} catch (Exception ignore) { continue; /*goto the next loop*/}
		}
		if (c.isPlayerOnlyCommand()) {
			if (player!=null) {
				return this.c.execute(commandSource, commandLabel, args);
			} else {
				commandSource.sendFeedback(DefaultTranslations.PLAYER_ONLY_COMMAND, false);
				return 0;
			}
		} else {
			return this.c.execute(commandSource, commandLabel, args);
		}
	}
	
	public List<Text> tabComplete(ServerCommandSource commandSource, String alias, String[] args) {
		if (c.getParams()==null || c.getParams().length==0) {
			return new ArrayList<Text>();
		} else {
			for (int i = 0; i < args.length-1; i++) {
				try {
					ITabable t = c.getParams()[i];
					if (t instanceof TabableSubCommand) {
						try {
							TabableSubCommand t2 = (TabableSubCommand) t;
							SubCommand s = t2.getSubCommand(args[i]);
							ITabable[] t3 = s.getParams();
							ITabable t4 = t3[args.length-i-2];
							return t4.getTabable(commandSource, args);
						} catch (Exception ignore) {
							return new ArrayList<Text>();
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
			try {
				return c.getParams()[args.length-1].getTabable(commandSource, args);
			} catch (Exception ignore) {
				return new ArrayList<Text>();
			}
		}
	}
}