# Providers

Providers are ways to give mods features without them actually having to contain said features, this is done by having a provider provide the feature to them.

## What is the point of providers?

Providers enable developers to create mods that rely on features they do not support themselves, this then means the server owner can select a mod that provides said feature to the server and them to work together easily.
<br>
To put it simply it enables server owners to mix and match mods together and ensure their functionality.

## The default providers

By default BFAPI has 4 providers *(that can be enabled and disabled via the config)* that allow for simple features to be easily added to minecraft, these are:

* TranslationProvider
* PermissionProvider
* CommandProvider
* GameRuleProvider

They all add differing features for developers to use, for example the GameRuleProvider can be used to quickly create gamerules that can be used for other features.

## Provider events

There is two events that currently exist for providers, these are:

* [The registration event](#the-registration-event)
* [The information event](#the-information-event)

### The registration event

This event is called when a provider is registered via the `Loader.registerProvider(provider)` method, it signals to mods that the provider is ready to be used.
<br>
To catch the event use the following code:

```java
// Register an event handler
Events.PROVIDER_REGISTRATION_EVENT.register( (event) -> {
    // Do stuff here
});
```

Then to check the provider being registered you can use casting like so:

```java
// Register an event handler
Events.PROVIDER_REGISTRATION_EVENT.register( (event) -> {
    // We will check for the CommandProvider being registered
    Provider provider = event.getProvider();
    // To do this we can just use the instanceof keyword and some pattern matching
    if (provider instanceof CommandProvider commandProvider) {
        // In here you can use commandProvider to interact with the CommandProvider
    }
});
```

### The information event

This event is called when a provider wants to collect information, for example the TranslationProvider calls it to collect translations to register
<br>
To catch the event use the following code:

```java
// Register an event handler
Events.PROVIDER_INFO_EVENT.register( (event) -> {
    // Do stuff here
});
```

Then we can give the provider information like so:

```java
// Create the translation to give to TranslationProvider
Translation translation = ...;
// Register an event handler
Events.PROVIDER_INFO_EVENT.register( (event) -> {
    // We will check for the TranslationProvider requesting information and give it a translation
    Provider provider = event.getProvider();
    // To do this we can just use the instanceof keyword and some pattern matching
    if (provider instanceof TranslationProvider translationProvider) {
        // Now we give the event our translation
        event.give(translation);
    }
});
```

## Extras

If you ever need a provider you can always use `Loader.getProvider(providerClass)`