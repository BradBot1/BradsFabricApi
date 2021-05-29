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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class RegisterableCommand {
	
	protected Command c;
	
	protected RegisterableCommand(Command c) {
		this.c = c;
	}
	
	public Command getInner() {
		return c;
	}
	
	public String[] removeBefore(String[] params, int index) {
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < params.length; i++) {
			if (i<=index) continue;
			strings.add(params[i]);
		}
		return strings.toArray(new String[strings.size()]);
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
					return ts.getCustomSubCommandFromName(args[i]).execute(commandSource, commandLabel, removeBefore(args, i));
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
			return new ArrayList<>();
		} else {
			for (int i = 0; i < args.length-1; i++) {
				try {
					ITabable t = c.getParams()[i];
					if (t instanceof TabableSubCommand) {
						try {
							TabableSubCommand t2 = (TabableSubCommand) t;
							SubCommand s = t2.getCustomSubCommandFromName(args[i]);
							ITabable[] t3 = s.getParams();
							ITabable t4 = t3[args.length-i-2];
							return t4.getTabable(commandSource, args);
						} catch (Exception ignore) { return new ArrayList<Text>() {private static final long serialVersionUID = -17124577512450984L;{
							add(new LiteralText("?"));
						}};}
					}
				} catch (Exception e) {
					continue;
				}
			}
			try {
				return c.getParams()[args.length-1].getTabable(commandSource, args);
			} catch (Exception ignore) { return new ArrayList<Text>() {private static final long serialVersionUID = -17124577512450984L;{
				add(new LiteralText("?"));
			}};}
		}
	}
}