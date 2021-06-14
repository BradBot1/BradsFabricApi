# Translations

Translations allow for mods to have different messages sent to players based on their selected language

> For translations the [`Server Translations`](https://github.com/arthurbambou/Server-Translations) API is used by default

Translations are provided via the TranslationProvider.

## Creating a translation

Translations are records that have a:
* translation key *(the main key used for identifying the translation)*
* translation map *(a map that contains all default translations)*
  * key *(the language key)*
  * value *(the default translation for the language key)*

To create a translation you just instantiate the record like so:

```java
// Create the default translation map
Map<String, String> defaultTranslations = new HashMap<String, String>();
// Add the translation for the us
defaultTranslations.put("en_us", "This is an example translation!");
// And then the UK
defaultTranslations.put("en_gb", "This is an example translation!");
// And add a spanish translation
defaultTranslations.put("es_es", "¡Esta es una traducción de ejemplo!");
// Create the translation with the key and default translation map
Translation translation = new Translation("example.translation", defaultTranslations);
```

## Registering translations

To add translations you need to use the TranslationProvider's information event *(for more information on provider events look at `provider.md`)*

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