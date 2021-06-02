package com.bb1.api.commands;

import java.util.ArrayList;
import java.util.List;

import com.bb1.api.Loader;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.translations.DefaultTranslations;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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
		if (c.getPermission()!=null && !Permissions.check(commandSource, c.getPermission())) {
			commandSource.sendFeedback(DefaultTranslations.NEED_PERMISSIONS, false);
			return 0;
		}
		ServerPlayerEntity player = Loader.getServerPlayerEntity(commandSource);
		for (int i = 0; i < args.length; i++) {
			try {
				ITabable t = c.getParams()[i];
				if (t instanceof TabableSubCommand) {
					TabableSubCommand ts = (TabableSubCommand) t;
					SubCommand sub = ts.getSubCommand(args[i]);
					if (c.getPermission()!=null && !Permissions.check(commandSource, sub.getPermission())) {
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
		if (c.getPermission()!=null && !Permissions.check(commandSource, c.getPermission())) {
			return new ArrayList<Text>(); // Just default to showing the '?' error
		}
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