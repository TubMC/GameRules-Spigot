# GameRules [Spigot Edition]

A simple [GameRules](https://github.com/TubMC/GameRules) implementation that provides support for [SpigotMC](https://hub.spigotmc.org/)

> This project depends upon [CommandAPI](https://github.com/JorelAli/CommandAPI)!

## Implementation Specific Methods

The following methods are added to all Gamerule's in this implementation:

| Name     | Parameters    | Return Type   | Function                                          |
|----------|---------------|---------------|---------------------------------------------------|
|isVanilla||boolean|If the Gamerule comes from vanilla|
|toSpigot||GameRule|Converts the GameRule into it's Spigot counterpart|

The following methods are added to all GameruleType's in this implementation:

| Name     | Parameters    | Return Type   | Function                                          |
|----------|---------------|---------------|---------------------------------------------------|
|getSpigotCommandAPIArgument||[Argument](https://commandapi.jorel.dev/9.3.0/arguments.html)|Returns the [Argument](https://commandapi.jorel.dev/9.3.0/argument_custom.html) used to parse and validate the value provided in /gamerule|