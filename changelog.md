### v1.7.1
* FIX: Fixed an issue with the Feral Flare Lantern that could lead to chunk corruption when placed close to the world height limit.
* NEW: Added a configuration option to limit the amount of lights a Feral Flare Lantern can place.
  * Warning: Setting this value in conjunction with the radius and light level setting too high can still lead to chunk corruption! Dont be stupid. You dont need a light at every possible block in a chunk.

### v1.7.0
* CHANGE: The Mega Torch and Dread Lamp now work in a cube instead of a cylinder. This should make it much easier to setup torches without overlap or deadspots. The Size of the cube is Range + 1 in each direction. A range of 64 (default) will result in a cube of 129 by 129 blocks with the torch as its center.
* NEW [Beta]: Mega Torches can now visualize the range at which they work. Use any dye to render a visual representation of the torches working volume. Use any dye or an empty hand to disable the renderer. The renderer will also disable itself when you change dimensions or get out of viewing range.
* NEW: Feral Flare Lanterns can now be configured to only place lights in line of sight. Simply rightclick the lantern to open the gui. At the moment this mechanic feels a bit clunky, so there will be changes in a future release.
* NEW: Mega Torches should now be able to suppress those scary cave ambient sounds

### v1.6.4
* FIX: reworked torch storage system to use capabilities instead of a global file.
  This fixes an issue with torches not working in most modded dimensions after a restart.
  You might need to replace your torches and lamps for the changes to take effect.

### v1.6.3
* FIX: Moved russian language files to correct location

### v1.6.2
* Updated to latest stable MCForge Version (14.23.3.2655)
* FIX: Feral Flare Lantern now uses the configuration option for its tick rate
* FIX: Torches should no longer work across multiple world saves
* FIX: Added small workaround to prevent a crash during world startup on certain modpacks
* FIX: Improved Terrain lighter compatibility with replaceable blocks
* CHANGE: Removed some Logging Spam (and added more :D well, just for some errors)

### v1.6.1
* Fix Mega Torch and Dread Lamp not working (oops...)

### v1.6.0
* Add Feral Flare Lantern (Illuminates a big area with a configurable minimum light level, default 16 radius)
* Add Frozen Pearl (Removes nearby invisible feral flare light sources)

### v1.5.3
* Changed buildscript and configuration to be compatible with 1.12 and 1.12.1+

### v1.5.2
* Update to forge 14.22.0.2445
* Marked as stable release
* WARNING: IF YOU UPDATE FROM 1.4.3 OR EARLIER YOU WILL NEED TO REPLACE YOUR TORCHES AND LAMPS!

### v1.5.1
* Switched torchmaster logging to debug instead of info to reduce log spam

### v1.5.0
* Added beginner tooltips to the mega torch, dread lamp and terrain lighter
* Added persistent torch registry (Allows torches and lamps to work while not being chunk loaded)
* MC 1.12 versions of this mod are now signed, yey!

### v1.4.3
* Updated to forge 14.21.0.2363, fixes a crash on startup due to the forge registry changes.
