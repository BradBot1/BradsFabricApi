package com.bb1.fabric.bfapi.permissions.database;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.PERMISSION_DATABASES;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.Input;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SimplePermissionDatabase implements IPermissionDatabase {
	
	private static final String SAVE_PATH = FabricLoader.getInstance().getGameDir().toFile().getAbsolutePath()+File.separatorChar+"permissions"+File.separatorChar;
	
	protected final Map<UUID, Set<String>> _permissions = new ConcurrentHashMap<UUID, Set<String>>();
	
	protected final Identifier id;
	
	public SimplePermissionDatabase(Identifier id) {
		this.id = id;
		new File(SAVE_PATH+id.getNamespace()+File.separatorChar).mkdirs();
		File file = new File(SAVE_PATH+id.getNamespace()+File.separatorChar+id.getNamespace()+".dat");
		if (file.exists()) {
			ExceptionWrapper.execute(Input.of(this._permissions), (i)->{
				NbtCompound tag = NbtIo.readCompressed(file);
				for (String uuids : tag.getKeys()) {
					Set<String> set = new HashSet<String>();
					for (NbtElement node : tag.getList(uuids, NbtElement.STRING_TYPE)) {
						set.add(node.asString());
					}
					i.get().put(UUID.fromString(uuids), set);
				}
			});
		}
		GameObjects.GameEvents.SERVER_STOP.addHandler((event)->{
			ExceptionWrapper.execute(Input.of(this._permissions), (i)->{
				NbtCompound main = new NbtCompound();
				for (Entry<UUID, Set<String>> entry : i.get().entrySet()) {
					NbtList inner = new NbtList();
					for (String node : entry.getValue()) {
						inner.add(NbtString.of(node));
					}
					main.put(entry.getKey().toString(), inner);
				}
				new File(SAVE_PATH+id.getNamespace()+File.separatorChar).mkdirs();
				File file2 = new File(SAVE_PATH+id.getNamespace()+File.separatorChar+id.getNamespace()+".dat");
				file2.createNewFile();
				NbtIo.writeCompressed(main, file2);
			});
		});
	}

	@Override
	public boolean canHavePermission(Field<Entity> field, Permission permission) { return true; } // this is a simple db so this isnt needed

	@Override
	public boolean hasPermission(Field<Entity> field, Permission permissionNode) {
		return this._permissions.getOrDefault(field.getObject().getUuid(), new HashSet<String>()).contains(permissionNode.node()) || field.getObject().hasPermissionLevel(permissionNode.level().toInt());
	}

	@Override
	public void setPermission(Field<Entity> field, Permission permissionNode, boolean value) {
		Set<String> set = this._permissions.getOrDefault(field.getObject().getUuid(), new HashSet<String>());
		if (value) { set.add(permissionNode.node()); } else { set.remove(permissionNode.node()); }
		this._permissions.putIfAbsent(field.getObject().getUuid(), set);
	}
	
	@Override
	public int getExtensionValue(Field<Entity> field, Permission permissionNode) {
		var collection = this._permissions.getOrDefault(field.getObject().getUuid(), new HashSet<String>()).stream().filter((str)->str.startsWith(permissionNode.node())).toList();
		if (collection.size()<=0) { return 0; }
		int value = 0;
		for (String str : collection) {
			try {
				int i = Integer.parseUnsignedInt(str.replace(permissionNode.node(), ""));
				if (i>value) { value = i; }
			} catch (Exception e) { }
		}
		return value;
	}
	
	@Override
	public void setPermission(Field<Entity> field, Permission permissionNode, int extensionValue) {
		var collection = this._permissions.getOrDefault(field.getObject().getUuid(), new HashSet<String>());
		collection.removeIf((n)->n.startsWith(permissionNode.node()));
		collection.add(permissionNode.node()+"."+extensionValue);
		this._permissions.putIfAbsent(field.getObject().getUuid(), collection);
	}

	@Override
	public void register(@Nullable Identifier name) {
		Registry.register(PERMISSION_DATABASES, this.id, this);
	}

}
