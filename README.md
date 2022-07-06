# ai-hunter-plugin
Minecraft Spigot AI hunter plugin used for quick setup of Impact Client AI hunter on servers


Hunter needs to be configurable using config.yml. 

We can configure the amounts of health, tools, weapons and armor (?), and number of blocks (maybe even types of blocks like dirt or cobble stone).

Cannot die from burning (can be configurable to true/false):
- Code here: https://bukkit.org/threads/prevent-player-from-burning.40729/

Challenges: 
- complete the game before hunter kills you (speedrun)
- kill the hunter (must not be invincible in this case, and hunger feed should be limited somehow)
- get a certain item (like diamond) before hunter gets you


Add features: 
- write documentation on how to use the plugin
- add template for Impact Client hunter configuration
- 


Check this out: 
- https://github.com/notnotnotswipez/TerminatorNPC
- https://jd.citizensnpcs.co/allclasses-noframe.html
- https://www.spigotmc.org/threads/executing-baritone-commands-from-a-plugin.533036/

So, instead of relying on Baritone/Impact, we could use NPCs. It does not require an additional account, and it is more lightweight.

### Todos:
- re-create the logic for AI hunter: 
  - 10 seconds freeze after hunter creation
  - equip with certain items
  - make not invincible by default
  - make everything configurable in yml
  
 
# Testing
Build and deploy locally
```
./gradlew clean build && ln -s ~/projects/private/ai-hunter-plugin/build/libs/AiHunterPlugin-0.1-SNAPSHOT.jar ~/projects/private/minecraft/spigot-server-1.17.1/plugins/AiHunterPlugin.jar
```
 
Cleanup, build and deploy locally
```
./gradlew clean build && rm -rf ~/projects/private/minecraft/spigot-server-1.17.1/plugins/AiHunterPlugin/ && rm ~/projects/private/minecraft/spigot-server-1.17.1/plugins/AiHunterPlugin.jar && ln -s ~/projects/private/ai-hunter-plugin/build/libs/AiHunterPlugin-0.1-SNAPSHOT.jar ~/projects/private/minecraft/spigot-server-1.17.1/plugins/AiHunterPlugin.jar
```



# Possible commands
CREATE:
- `aihunter create (creates 1 hunter named Bot Mutant)`
- `aihunter create Gogo (creates 1 hunter named Gogo)` 
- `aihunter create Bot 1 (creates 1 hunter named Bot)` 
- `aihunter create Joe 2 (creates 2 hunters named Joe-1 and Joe-2)`



REMOVE:
- `aihunter remove (removes all hunters)`
- `aihunter remove all (removes all hunters)`
- `auhunter remove Joe-2 (removes hunter named Joe-2)`


LIST/INFO:
- `aihunter list (lists all hunter names and current locations)`
- `aihunter list all (lists all hunter names and current locations)`
- `aihunter info Joe-2 (lists name and current location of a hunter named Joe-2)`


PAUSE:
- `aihunter pause (pauses/freezes all hunters)`
- `aihunter pause all (pauses/freezes all hunters)`
- `aihunter pause Joe-2 (pauses the hunter named Joe-2)`


RESUME:
- `aihunter resume (resumes all hunters)`
- `aihunter resume all (resumes all hunters)`
- `aihunter resume Joe-2 (resumes the hunter named Joe-2)`