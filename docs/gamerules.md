# GameRules

GameRules are simple feature for users to use that allow them to modify how your mod works without opening a config.
<br>
GameRules can be easily created via BFAPI due to the GameRuleProvider.

## How to create a GameRule

In this example we will be creating a StringGameRule, first create the instance of the GameRule like so:

```java
public static final StringGameRule EXAMPLE_GAMERULE = new StringGameRule("name", "defaultValue", category);
```

So then what does each field do?
<br>
The first field *(name)* is used to specify the GameRule's name in the `/gamerule` command, all names must be unique.
<br>
The second field *(defaultValue)* is, as the name suggests, the value that will be assigned to the GameRule if it doesn't already have a value
<br>
The third field *(category)* is an enum provided by Minecraft, you can just set this to Category.MISC

## How to register a GameRule

> The register method in the GameRule class automatically does the following section, so use that where possible

To register GameRules you must give it to the GameRuleProvider during its information event, you can do this like so

```java
// Create the GameRule to be registered
final GameRule<?> exampleGameRule = ...;
// Add a handler to the info event
Events.PROVIDER_INFO_EVENT.register((event) -> {
    // Check if its the GameRuleProvider
    if (event.getProvider() instanceof GameRuleProvider) {
        // If it is give the event the GameRule
	    event.give(exampleGameRule);
	}
});
```