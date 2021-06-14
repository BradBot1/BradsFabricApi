# Commands

These create a bukkit like *(handling section only)* command system, with simple command handling and registration.
<br>
All commands are registered via the CommandProvider

## How to create a command

To create a command, simply do the following:

```java
Command command = new Command("commandName") {

    Override
	public int execute(ServerCommandSource source, String alias, String[] params) {
		source.sendFeedback(new LiteralText("Hello there!"), false);
		return 1;
	}
	
};
```

## How to register a Command

> The register method in the Command class automatically does the following section, so use that where possible

To register Commands you must give it to the CommandProvider during its information event, you can do this like so

```java
// Create the Command to be registered
final Command command = ...;
// Add a handler to the info event
Events.PROVIDER_INFO_EVENT.register((event) -> {
    // Check if its the CommandProvider
    if (event.getProvider() instanceof CommandProvider) {
        // If it is give the event the Command
	    event.give(command);
	}
});
```

*You can also register permissions from the same handler, but the `.register()` method does that for you*

## Extras

You can use `Loader.getServerPlayerEntity(source)` to get an exceptionless, nullable player