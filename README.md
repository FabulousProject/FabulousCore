
<p align="center">
![img](images/fabulouscore.png)

<h1 align="center">FabulousCore</h1>

<p align="center">
 <b>
      The FabulousCore gives to you create a SignGUI, WorldBorder, cooldowns, minigame, interactable objects, chain based item creating and send sound & colored/timed message(s) to player(s) with easy way.
</b>
</p>


<p align="center">
    <a href="https://discord.gg/aRn7E7Ws2n">
        <img alt="Discord" src="https://img.shields.io/discord/597922723762536510?color=7289DA&label=Discord&logo=discord&logoColor=7289DA">
    </a>
    <a href="https://www.patreon.com/join/alpho320">
        <img alt="Patreon" src="https://img.shields.io/badge/-Support_on_Patreon-F96854.svg?logo=patreon&style=flat&logoColor=white">
    </a> 
    <br>
    <img alt="Latest" src="https://jitpack.io/v/FabulousProject/FabulousCore.svg">
    <a href="https://app.codacy.com/gh/FabulousProject/FabulousCore/dashboard">
        <img alt="quality" src="https://img.shields.io/codacy/grade/1538be190da6406aa6a2bc711b2478a2">
    </a>
    <img alt="Last Updated" src="https://img.shields.io/github/last-commit/FabulousProject/FabulousCore">
    <br>
    <a href="https://github.com/FabulousProject/FabulousCore/stargazers">
        <img alt="Stars" src="https://img.shields.io/github/stars/FabulousProject/FabulousCore">
    </a>
    <img alt="Maintained" src="https://img.shields.io/maintenance/yes/2021"> 
</p>

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

<builds>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.3.1-SNAPSHOT</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
          <configuration>
            <minimizeJar>true</minimizeJar>
            <createDependencyReducedPom>false</createDependencyReducedPom>
            <!-- Relocations(Optional)
            <relocations>
              <relocation>
                <pattern>me.alpho320.fabulous.core</pattern>
                <shadedPattern>[YOUR_PLUGIN_PACKAGE].shade</shadedPattern>
              </relocation>
            </relocations>
            -->
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</builds>
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
    this.core.init(); //then you can access with BukkitCore.instance().foo();
    
    new SomeClassesThatNeedBukkitCore(this.core).foo();
    new SomeOtherClasses(this.core).foo();
  }
}
```

### Creating a SignGUI
```java
SignGUI sign = BukkitCore.instance().sign()
	.create() // creates a new instance.
	.setType(SignGUI.SignType.OAK) // Types: OAK, ACACIA, BIRCH, SPRUCE, CRIMSON, DARK_OAK, JUNGLE.
	.withLines("Hi!", "how are u?", "must be four", "lines!")
	.whenOpen(strings -> {			
		player.sendMessage("opened!");
	    }
	)
	.whenClose(strings -> {
		player.sendMessage("closed!");
	    }
	)
	.open(player);
```

### Creating a ItemStack
```java
ItemStack item = new BukkitItemCreator()
        .type(Material.IRON_SWORD)
        .name(BukkitCore.instance().message().colored("&6&lAMAZING SWORD!"))
        .amount(3)
        .damage(1)
        .lore(
                "awsome lore",
                "really!"
        )
        .enchant(Enchantment.DAMAGE_ALL, 10)
        .modelData(20)
        .flag(ItemFlag.HIDE_ATTRIBUTES)
        .glow()
        .create();   
```

### Sending a Sound
```java
BukkitCore.instance().sound().send(
        player,
        Sound.ENTITY_PLAYER_LEVELUP
);  
```

### Serialize a Location
```java
BukkitCore.instance().location().serialize(
        player.getLocation()
); 
```

### Deserialize a Location
```java
BukkitCore.instance().location().deserialize(
        mySerializedString
); 
```

### Getting a Random Element From List
```java
T randomElement = new RandomSelect<>(myList).choose();

Player target = new RandomSelect<>(playerList).choose();
```

### <b>NOTICE: If you have any questions, suggestions or issues with the core, please contact me on GitHub Issues section or Alpho320#9202 (Discord)</b>
