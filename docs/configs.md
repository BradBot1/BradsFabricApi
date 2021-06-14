## How to set up a config

First extend the class *com.bb1.api.config.Config* and set a config name like so:

```java
package ?;

import com.bb1.api.config.Config;
import org.jetbrains.annotations.NotNull;

public class Config extends com.bb1.api.config.Config {

	public Config() { super("configNameGoesHere"); }

}
```

Then you can add values to be saved to and from a config file by using the *com.bb1.api.config.Storable* annotation on a field like so:

```java
package ?;

import com.bb1.api.config.Config;
import org.jetbrains.annotations.NotNull;

public class Config extends com.bb1.api.config.Config {

	public Config() { super("configNameGoesHere"); }

	@Storable public String foo;

}
```

If you wish to save the value under a different name than the field name use this instead:

```java
package ?;

import com.bb1.api.config.Config;
import org.jetbrains.annotations.NotNull;

public class Config extends com.bb1.api.config.Config {

	public Config() { super("configNameGoesHere"); }

	@Storable(key = "bar") public String foo;

}
```

If two values have the same "key" value *(excluding "null")* they will default to field names if possible, elsewise it will throw an IllegalArgumentException. So avoid things like this:

```java
package ?;

import com.bb1.api.config.Config;
import org.jetbrains.annotations.NotNull;

public class Config extends com.bb1.api.config.Config {

	public Config() { super("configNameGoesHere"); }

	@Storable public String bar;

	@Storable(key = "bar") public String foo;

}
```

Finally, to set a default value just set it like you would a normal field, it will only be overriden if a new value has been set. For example:

```java
package ?;

import com.bb1.api.config.Config;
import org.jetbrains.annotations.NotNull;

public class Config extends com.bb1.api.config.Config {

	public Config() { super("configNameGoesHere"); }

	@Storable(key = "bar") public String foo = "defaultValueGoesHere";

}
```

If you attempt to save a value that is not one of the following:

* int
* double
* float
* long
* short
* byte
* boolean
* String
* char
* any form of JsonElement

You will end up with an IllegalArgumentException