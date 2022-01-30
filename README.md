# BFAPI

## Recipes

BFAPI has support for complex recipes making customising a server easier

### Recipe types

These are used to identify what type of recipe you want to make

|Type|Identifier|
|----|----------|
|Shapeless Crafting|minecraft:shapeless_crafting|
|Shaped Crafting|minecraft:shaped_crafting|

### Recipe results

Recipes can have custom 'results' that are given to the player after they craft an item, for example xp can be given

|Result ID|Description|Expects|
|---------|-----------|-------|
|xp|Gives the player the amount of xp levels specified|Integer|
|run|A json object with two keys, server and player. Server is a JsonArray of commands the server should execute. Player is a JsonArray of commands the player should run|JsonObject|
|advancements|A list of advancements identifiers to be awarded|JsonArray|
|permissions|A list of permissions to be awarded|JsonArray|
|items|A list of items to be given to the player|JsonArray|
|messages|A list of Text to be sent to the player|JsonArray|

### Recipe requirements

Recipes can have a plethora of requirements that are used to resrict access to a recipe

|Requirement ID|Description|Expects|
|xp|Ensures the player has the amount of xp levels specified|Integer|
|advancements|Ensures the player has the advancements specified|JsonArray|
|permissions|Ensures the player has the permissions specified|JsonArray|

### Interacting with recipes and adding custom requirements/features/types

Adding custom requirements/features/types to recipes is simpler then it sounds, there are simple interfaces and methods that you can utilise to make it easier

#### Adding a requirement for recipes to use

Adding recipe requirements is very easy

To add a requirement you will need to use the `addRequirementBuilder` method on `AbstractRecipe`

```java
AbstractRecipe.addRequirementBuilder(); // like this!
```

The `addRequirementBuilder` method takes two inputs, an id in the form of a string and an IRecipeRequirement

##### Example of adding a requirement to recipes

Here we are going to add a requirement that ensures the player has a specified amount of health

```java
AbstractRecipe.addRequirementBuilder("healthRequirement", (jsonElement)->{
	final int amount = jsonElement.getAsInt(); // we get the amount specified in the json here
	return new IRecipeRequirement() { // we build an instance of IRecipeRequirement

		@Override
		public boolean canCraft(Field<Entity> crafter) { // this method controls if the player can craft the item
			return crafter.getObject().getHealth() >= amount; // we are checking the the given entity has the health required to craft the item
		}

		@Override
		public JsonObject addToObject(JsonObject object) { // here we build a serializer to allow for saving to json if the requirement is used in a recipe in a config
			object.addProperty("healthRequirement", amount); // the id here should be the same as the one used in the `addRequirementBuilder` method
			return object; // we need to return the object
		}
		
	};
});
```

#### Adding a result for recipes to use

Adding recipe result is as easy as a requirement

To add a requirement you will need to use the `addResultBuilder` method on `AbstractRecipe`

```java
AbstractRecipe.addResultBuilder(); // like this!
```

The `addResultBuilder` method takes two inputs, an id in the form of a string and an IRecipeResult

##### Example of adding a result to recipes

Here we are going to add a result that kicks the player

```java
AbstractRecipe.addResultBuilder("kick", (jsonElement)->{
	final String message = jsonElement.getAsString(); // we get the kick reason specified in the json here
	return new IRecipeResult() {

		@Override
		public void onCraft(Field<Entity> crafter) {
			if (crafter.getObject() instanceof PlayerEntity pe) { // we ensure the crafter is a player
				pr.networkConnection.disconnect(new LiteralText(message)); // we kick them with the message provided
			}
		}

		@Override
		public JsonObject addToObject(JsonObject object) {
			object.add("kick", message); // like in IRecipeResult this should be the same id as used in addResultBuilder
			return object;
		}
		
	};
});
```

#### Adding a recipe type

Unlike adding requirements and results this takes alot to do

First of you need a working implementation of `AbstractRecipe` that builds to the wanted recipe (*you can look at [the source](https://github.com/BradBot1/BradsFabricApi/blob/master/src/main/java/com/bb1/fabric/bfapi/recipe/ShapelessCraftingRecipe.java) for a better example of this*)

Then you just need to register it with the `addRecipeBuilder` on `AbstractRecipe`

## Events

Events have been changed to make them more modular, now all events must be registered and have an identifier

### Creating an event, an example

> Just want a list of events? If so [click me!](#out-of-the-box-events)

Creating an event is super simple, you don't even need to make it statically accessible!

To create a simple event we can do as follows

```java
// First we need an identifier the mod will be under,
// it should always start with your modid if its generated by your event
// if its called from a mixin and is correspondent to a minecraft event you can use the minecraft identifier
// The identifiers path should be a simple name for the event, here we are going to make an event to handle
// and send internal messages: so we aptly name it message
Identifier identifier = new Identifier("MODID", "message");
// The class/type inside the Input<> is what will actually be sent over an event
// here we use a `String` as that is what we will send our message as
// The Event constructor takes in an identifier and a boolean
// the boolean dictates if the event should be automatically registered
Event<Input<String>> messageEvent = new Event<Input<String>>(identifier, true);
```

And that's it! Your message event has been created and can now be used by other developers

### Handling an event, an example

> This example will talk about handling the event created in [the prior example](#creating-an-event-an-example), so its recommended that you read it for context

#### Direct handling

The simplest way to handle an event is to directly add a handler to it

This can be done by either getting a [static instance](#getting-an-event-from-an-access-point) or [grabbing it from the registry](#getting-an-event-from-the-registry)

So when you have an instance of the event how do you add a handler?

In this example I will be using [the registry](#getting-an-event-from-the-registry) to get [the event](#creating-an-event-an-example)

```java
// Using the registry to get the event
Event<Input<String>> messageEvent = (Event<Input<String>>) BFAPIRegistry.EVENTS.get(new Identifier("MODID", "message"));
messageEvent.addHandler((input)->{
	// Here you can handle the event, any arguments will be passed through an event
});
```

##### Getting an event from the registry

All events should be registered to the `BFAPIRegistry#EVENTS` registry, this means we can get an event by simply querying it with the events identifier

```java
// It is vital that we use the same identifier as before
Identifier identifier = new Identifier("MODID", "message");
// When we pass the identifier to the registry we receive any event with the corresponding identifier
// however, since we get a generic event back we need to cast to the appropriate event
// Since we have to cast we need to use a unchecked suppression to avoid warnings
@SuppressWarnings("unchecked")
Event<Input<String>> messageEvent = (Event<Input<String>>) BFAPIRegistry.EVENTS.get(identifier);
```

##### Getting an event from an access point

If an event has a public access point you can simply grab it from there

```java
// Just generic direct access
Event<Input<?>> example = ExampleEvent.INSTANCE;
```

#### Reference handling

If you don't want to [get an event from the registry](#getting-an-event-from-the-registry) and don't have [an access point](#getting-an-event-from-an-access-point) you can use an EventListener to handle the event

Here I will still be using [the event created in a prior example](#creating-an-event-an-example)

```java
public class ExampleListener implements EventListener {
	
	public ExampleListener() {
		// Here we auto register all events
		// Doing it in the constructor removes complexity
		register();
	}
	
	// We use an annotation to say what event to bind to
	// The event must be on the BFAPIRegistry#EVENTS registry
	@EventHandler(eventIdentifier = "MODID:message")
	public void handleMessageEvent(Input<String> event) {
		// You can handle the event like normal here
	}
	
}
```

#### Reference handling with decomposition

If you want a simpler approach to dealing with inputs then you can have an events input decomposed into the inner arguments

Here I will still be using [the event created in a prior example](#creating-an-event-an-example)

```java
public class ExampleListener implements EventListener {
	
	public ExampleListener() {
		// Here we auto register all events
		// Doing it in the constructor removes complexity
		register();
	}
	
	// We use an annotation to say what event to bind to
	// The event must be on the BFAPIRegistry#EVENTS registry
	@EventHandler(eventIdentifier = "MODID:message", decomposeArguments = true)
	public void handleMessageEvent(String message) {
		// You can handle the event like normal here
		// However you will notice that we now have decomposed/direct access to the inputs inner values
		// this can make your code look cleaner when handling events
	}
	
}
```

##### Controlling event binding

By default an event is not required, if it isn't bound to then it nothing will happen other than a simple log to console

You can modify this behaviour in the `EventHandler` annotation as it has a few fields

|Field|DataType|Description|Default|
|-----|--------|-----------|-------|
|eventIdentifier|String|The identifer of the event in string form|N/A|
|required|boolean|If the event is required, if it is an exception will be thrown if it fails to bind|false|
|logOnFailedBinding|Boolean|If a message should be logged when the event fails to bind|true|

### 'Out of the box' events

> THIS IS OUT OF DATE, I have added alot of events since then and plan to update this documentation soon, however, I might move to githubs wiki feature for this

#### Minecraft events

> Minecraft events use the generic minecraft:*id* identifier

|Event|Description|Identifer|Gives|
|-----|-----------|---------|-----|
|CommandRegistrationEvent|Called when minecraft wishes to register all commands, you should use this to create commands|minecraft:command_registration|DualInput\<CommandDispatcher\<ServerCommandSource\>, CommandManager.RegistrationEnvironment\>|

#### BFAPI events

> BFAPI events use the generic bfapi:*id* identifier

|Event|Description|Identifer|Gives|
|-----|-----------|---------|-----|
|LoadedEvent|Called when BFAPI is finished loading|bfapi:loaded|Input\<Loader\>|

## DEPRECATION NOTICE

#### Simply

Any field/method/class marked by the `@Deprecated` annotation will be removed after a month (*roughly*)

#### Also

This is alongside any helper functions that utilise it

For example if a helper function takes in a depricated object as an argument it may be removed without being marked itself as it relies upon something that no longer exists. The method may not be marked itself

**If a class/method no longer works as intended and cannot be repaired it may be removed without a deprecation notice**

