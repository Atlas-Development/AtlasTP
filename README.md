# AtlasTP
A simple teleport plugin for the [Sponge](https://spongepowered.org/) server software.

It is intended to be a very simple plugin that overrides the vanilla TP commands, adds some extra functionality and makes it permission based. This is mainly useful for smaller servers and creative servers.

## Commands
- `/tp` The standard teleport command, which allows for direct teleport to another player teleporting a specific player to a different player.
  - A single argument leads to the sending player teleporting to the specified player.
    
    Requires the `atlastp.command.tp` permission node.
  - Two arguments leads to the first player teleporting to the second player.
    
    Also requires the `atlastp.command.tpother` permission node.
- `/tpa` Asks a player if the sending player can teleport to them.
  
  Requires the `atlastp.command.tpa` permission node.
- `/tpahere` Asks a player if they want to teleport to them.
  
  Requires the `atlastp.command.tpahere` permission node.
- `/tpaccept` and `/tpdeny` Accept the TP request from a specific player or deny them.
  
  Requires the `atlastp.command.tparesponse.tpaccept` or the `atlastp.command.tparesponse.tpdeny` permission node.

### Example
If the player *Notch* wants to teleport to the player *jeb_*, then they can send a TPA request to *jeb_* with the command `/tpa jeb_`. 
Now *jeb_* can accept the TPA with `/tpaccept Notch`. When they do this, *Notch* will be teleported to *jeb_*.

## Features (TODO)
- [x] `/tp` command
- [ ] `/tpa` system
  - [x] Send TP requests to another person
    - [x] `/tpa` command
    - [x] `/tpahere` command
  - [x] Accept TP requests from another person
  - [ ] List current TP requests
  - [x] TP request expiration
     
### Possible Features
- [ ] Teleport request cooldown
- [ ] Ignore TP requests from specific players

## License
This project is licensed under the [Apache 2.0 License](LICENSE).
