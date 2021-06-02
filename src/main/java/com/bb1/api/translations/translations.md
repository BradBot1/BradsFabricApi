For translations the [this](https://github.com/arthurbambou/Server-Translations) API is used

## Registering translations

To add translations you need to get *TranslationManager* via one of the following

```java
TranslationManager translationManager = TranslationManager.get();
```

**OR**

```
Event.LOAD_EVENT.register((event)->{
	TranslationManager translationManager = event.getTranslationManager();
});
```

Then you just need to use the *set* method to add the translation!
<br>
If you are only registering it so that the translation can be added later you can just use the *setIfNotPresent* method with the following arguments

```java
translationManager.setIfNotPresent(TRANSLATION_KEY, TranslationManager.DEFAULT_LANG, TRANSLATION_KEY);
```

## Getting translations

If you need to translate something quickly you can use the *translate* method. *However beware that it will not translate any key that has not been added to the manager instance, so for example minecraft translations that havn't been modified via the manager will not work!*

## Extras

If you ever need a list of all translations use the *convertLangToJson* method and loop through it like so:

```java
for (Entry<String, JsonElement> entry : translationManager.convertLangToJson().entrySet()) {
	for (Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
		final String lang = entry.getKey();
		final String translation_key = entry2.getKey();
		final String translation = entry2.getValue().getAsString();
	}
}
```