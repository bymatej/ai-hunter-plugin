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
- remove Hibernate
- remove H2 DB
- add Citizens NPC plugin as a dependency (package as fat-jar)
- re-create the logic for AI hunter: 
  - 10 seconds freeze after hunter creation
  - equip with certain items
  - make not invincible by default
  - make everything configurable in yml
  
 
 