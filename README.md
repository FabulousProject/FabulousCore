[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/) 
[![rultor](https://www.rultor.com/b/FabulousProject/FabulousCore)]

[![Stars](https://img.shields.io/github/stars/FabulousProject/FabulousCore)](https://github.com/FabulousProject/FabulousCore/stargazers)
[![CodeFactor](https://www.codefactor.io/repository/github/fabulousproject/fabulouscore/badge?s=926aefc55e5fe4570a75af712259b4bfd3c02dcb)](https://www.codefactor.io/repository/github/fabulousproject/fabulouscore)
[![Release](https://jitpack.io/v/FabulousProject/FabulousCore.svg)](https://jitpack.io/#FabulousProject/FabulousCore)

The FabulousCore gives to you create a SignGUI, WorldBorder and send colored/timed message to player(s) with easy way.

## How to use (Bukkit)
```xml
<repositories>
  <repository>
    <id>jitpack</id>
    <url>https://jitpack.io/</url>
  </repository>
</repositories>
```

```xml
<dependencies>
	<dependency>
	    <groupId>com.github.FabulousProject.FabulousCore</groupId>
	    <artifactId>bukkit</artifactId> 
	    <version>${version}</version>
	</dependency>
</dependencies>
```

### Gradle

```groovy
plugins {
  id "com.github.johnrengelman.shadow" version "7.0.0"
}
```

```groovy
repositories {
  maven {
    url "https://jitpack.io"
  }
}
```

```groovy
dependencies {
  implementation("com.github.FabulousProject.FabulousCore:${version}")
}
```

## Getting Started

### Registering the library

```java
final class MyPlugin extends JavaPlugin {

  private BukkitCore core;

  @Override
  public void onEnable() {
    this.core = new BukkitCore(this);
    this.core.init(this); //then you can access with BukkitCore.instance().foo();
    
    new SomeClassesThatNeedSmartInventory(this.core).foo();
    new SomeOtherClasses(this.core).foo();
  }
}
```

### Creating a SignGUI
```java
SignGUI sign = BukkitCore.instance().apiManager().signManager()
        .create() // creates a new instance.
        .setType(SignGUI.SignType.OAK) // Types: OAK, ACACIA, BIRCH, SPRUCE, CRIMSON, DARK_OAK, JUNGLE.
        .withLines("Hi!", "how are u?", "must be four", "lines!")
        .setCallback(new IOpenable<String[]>() {
            @Override
            public void whenOpen(String[] strings) {
                player.sendMessage("opened!");
            }

            @Override
            public void whenClose(String[] strings) {
                player.sendMessage("closed!");
            }
        })
        .open(player);
```

### <b>NOTICE: If you have any questions, suggestions or issues with the core, please contact me on GitHub Issues section or Alpho320#9202 (Discord)</b>
