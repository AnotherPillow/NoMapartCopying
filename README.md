# NoMapartCopying

A PaperMC plugin designed to allow a map author to make their maps uncopyable. This was written over several months (large breaks) and is fairly simple overall.

## Usage for server owners

1. Download the latest jar from the [releases page](https://github.com/AnotherPillow/NoMapartCopying/releases), from a Github workflow if applicable or build from source
    - To build from source, download this repository and run `./gradlew.bat build` (or just `./gradlew build` if not on Windows)
2. Place the resulting .jar in your server's plugins folder.

### Config

- `locker-item` is the item used to prevent copying on maps. Must be an unnamespaced material name. Default is `RED_STAINED_GLASS_PANE`
- `rename-item` whether or not to rename the new map item to make a red name.
- `close-cartography-attempted-use` due to limitations of 1.19.2, no event for cartography table crafts is available (this remains up until 1.21). This means preventing cartography table crafts is difficult. If enabled, this option will make it so anyone who attempts to place a locked map (regardless of owner) into a cartography table (a second item insertion/it being the second may be necessary) will have the UI closed before it can be crafted (this is not likely to be 100% reliable).

## Usage for players

1. [Create a map](https://minecraft.wiki/w/Map#Mapping)
2. Create your desired copies. You won't be able to make any more after it is locked. 
3. Combine it with a red stained glass pane in a crafting table
![locking-recipe.png](marketing/locking-recipe.png)
4. You may now safely distribute this map. It cannot be copied and this lore will stay. If a player attempts to copy it, no item will be created in the crafting table.
![cannot-copy.png](marketing/cannot-copy.png)
## Inspiration

The skyblock.net community, including [this post](https://skyblock.onl/threads/option-to-disable-mapart-copying.144363/).


## LICENSE

GNU AGPL 3.0