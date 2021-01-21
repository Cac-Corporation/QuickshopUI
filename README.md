# QuickshopUI
## QuickShop add-on plugin that add an interface to teleport to players shop if they set a teleport point.

### Commands :
The principal command of this plugin is ```/pshop``` that opens an interactive GUI where you can see all the players
that have a shop **AND** that have set a teleport point.

You can set your teleport point by using ```/pshop setwarp```.</br>
You can also delete this point if you have to build new things around your shop with ```/pshop delwarp```
</br></br></br>

### Data storage :

---
OBSOLETE</br>
At this time, all the information about player teleport point are stored in the config.yml file.

This is the sample of stored data:
```yaml
locations:
  CacTt4ck:
    ==: org.bukkit.Location
    world: world
    x: -1.5369284583591143
    y: 69.0
    z: -10.786006194617253
    pitch: 3.4837098
    yaw: -82.01683
  Player2:
    ==: org.bukkit.Location
    world: world
    x: -1.5369284583591143
    y: 69.0
    z: -10.786006194617253
    pitch: 3.4837098
    yaw: -82.01683
```
---
UPDATED

All the information about player teleport point are stored in the ```database.db``` Database file that is created
at the start of the server if doesn't exist.
---
