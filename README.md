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




What works: 
- HungerDropEvent
- RespawnPlayerToDeathPointEvent
- config.yml
- command on/off
- in-memory db



Add features: 
- cancel PlayerMoveEvent for a X seconds after hunter respawn, so the players can get away from hunter if they kill him
- write documentation on how to use the plugin
- add template for Impact Client hunter configuration
- 

Freeze player:
In the method to turn on the hunter
- toggle invulnerability to false
- toggle it to true
- keep it to true for X seconds
- i onplayermove disable the movement if the player is hunter and invulnerability is true
- disable vulnerabiliry after X seconds
- disable this functionality if the vulnerability is true in config.yml and fallback to current logic