# Creating an event

To create an event all you have to do is instance the Event class with the type of what should be handled, for example to create an event for a String you would simply do the following:

```java
public static final Event<String> StringEvent = new Event<String>();
```

### Registering a handler to the event

The event is public, static and final to stop multiple instances being created and make it easily accessible.

To handle the event you would just have to register a listener like so: 

```java
StringEvent.register((event)->{
	// Handle event
});
```

### Calling the event

Finally, you can call you event by using its onEvent(E event) method *(or you can give it to the invoker, either way works)*

```java
StringEvent.onEvent("hello!");
```