package com.bb1.api.commands.tab;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
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
public class TabableString implements ITabable {
	
	private List<Text> string = new ArrayList<Text>();
	
	public TabableString(String string) {
		this.string.add(new LiteralText(string));
	}
	
	@Override
	public List<Text> getTabable(ServerCommandSource commandSource, String[] params) {
		return string;
	}
	
	@Override
	public String getTabableName() {
		return string.get(0).asTruncatedString(32);
	}
	
}