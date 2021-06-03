package com.bb1.api.datapacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public abstract class DatapackAddon {
	/**
	 * The identifier to this {@link DatapackAddon}
	 */
	@NotNull private final Identifier identifier;
	/**
	 * The path to the files
	 * <br>
	 * For example the path <i>custom/item/</i>
	 */
	@NotNull protected final String path;
	/**
	 * The extension of all files
	 * <br>
	 * For example the path <i>.json</i>
	 */
	@NotNull protected final String fileExtension;
	
	public DatapackAddon(@NotNull Identifier identifier, @NotNull String path, @Nullable String fileExtension) {
		this.identifier = identifier;
		this.path = path;
		this.fileExtension = (fileExtension==null) ? ".json" : fileExtension;
	}
	
	@NotNull
	public final Identifier getIdentifier() { return this.identifier; }
	
	@NotNull
	public final String getPath() { return this.path; }
	
	@NotNull
	public final String getExtension() { return this.fileExtension; }
	
	public abstract void reload(@NotNull ResourceManager resourceManager, @Nullable Collection<Identifier> identifiers);
	
	@Nullable
	protected List<String> read(ResourceManager resourceManager, Identifier id) {
		try {
			ArrayList<String> r = new ArrayList<String>();
			Scanner s = new Scanner(resourceManager.getResource(id).getInputStream());
			while (s.hasNext()) {
		    	r.add(s.nextLine());
			}
			s.close();
			return r;
		} catch (Throwable t) {
			return null;
		}
	}

}
