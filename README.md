# QuickshopUI
## QuickShop add-on plugin that add an interface to teleport to players shop if they set a teleport point.

The principal command of this plugin is ```/pshop``` that opens an interactive GUI where you can see all the players
that have a shop **AND** that have set a teleport point.

You can set your teleport point by using ```/setpshop```.</br>
You can also delete this point if you have to build new things around your shop with ```/delpshop```

At this time, all the information about player teleport point are stored in the config.yml file.</br>
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