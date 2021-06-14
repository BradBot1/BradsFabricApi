## Adding a new datapack feature with json
First you need to create a new class that extends *JsonDatapackAddon* like so:

```java
public class ExampleDatapackAddon extends JsonDatapackAddon {
	
	public ExampleDatapackAddon() {
		super(new Identifier("MOD_ID:PATH"), "PATH");
	}
	
	@Override
	public void load(@NotNull JsonElement json, @Nullable Identifier resourceIdentifier) throws Throwable {
		// Load the file here
	}
	
	@Override
	public void unloadAll() {
		// Unload things here
	}
	
}
```

In this example replace *MOD_ID* with your mods id, and then swap PATH with your datapack path. *(a datapack path is the route to get to the files for the addon, for example you could use "example" to get all files in the minecraft/example directory in a datapack)*
<br>
And that is all their is to it, you simply handle the JsonElement provided to create the desired feature

## Example
In this example we will print any value that is provided in the datapack file

```java
public class ExampleDatapackAddon extends JsonDatapackAddon {
	
	public ExampleDatapackAddon() {
		super(new Identifier("example:printer"), "printer");
	}
	
	@Override
	public void load(@NotNull JsonElement json, @Nullable Identifier resourceIdentifier) throws Throwable {
		for (JsonElement jsonElement : json.getAsJsonArray()) {
			System.out.println(jsonElement.getAsString());
		}
	}
	
	@Override
	public void unloadAll() { } // We do not need to unload anything
	
}
```

Then all the datapack has to do is have a *.json* file in the minecraft/printer directory that contains an array with text for it to be printed.
<br>
Here is a tree diagram of the structure, the file *example.json* should contain a JsonArray with the contents to be printed
<pre>
packRoot
|-pack.mcmeta
|-data
  |-minecraft
    |-printer
      |-example.json
</pre>
## Creating a DatapackAddon type
If you want to handle data from datapacks in a form other then json you can create your own *DatapackAddon*!
<br>
Just create an abstract class that extends *DatapackAddon* and then use the reload method to handle the files, for an example look at the *JsonDatapackAddon* class
<br>
(You can get the contents of a file with the method *read(resourceManager, identifier)*, it returns a list with the contents as a string, each entry is a line of the file)