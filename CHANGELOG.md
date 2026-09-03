## v21.1.11
- Fix a NullPointerException crash on Fabric and NeoForge when hovering a button on the Feral Flare Lantern or Megatorch/Dreadlamp [Fabric + Neoforge] 

## v21.1.10
- Allow dread lamps and mega torches to be waterlogged
  - Dread Lamps are excluded since invisible lights cannot be placed underwater for now.
- Add line of sight support for feral flare lantern
  - Right click a lantern in the world like any other block to configure it.
  - Line of Sight off (default): Lantern will place lights anywhere in its working range
  - Line of Sight on: Lantern will not attempt to place lights behind walls
- Add volume renderer overlay with color cycler for Dread Lamp and Megatorch
  - Right click in the world to show the simple config menu.
  - Visualization is per-client
  - Allows configuring different colors for easier differentiation depending on surroundings
  - Renders the big box volume and a small 1-block sized box around the torch. The 1-block light allows seeing the torch through blocks.
- Fix invisible light blockstate warnings in log during startup

## v21.1.9

- Fix crash on fabric when joining a world

## v21.1.8
- Fix Frozen Pearl Recipe (PR by SrNadien)

## v21.1.7
- Add support to prevent phantoms from spawning from player insomnia mechanics

## v21.1.6
- Re-Added Frozen Pearl to remove residual lights from the Feral Flare Lantern

## v21.1.5
- Remove spawn logging in debug log by default to reduce potential wear on storage drives on both fabric and neoforge. \
  \
  The default behavior for modded minecraft is to log debug messages to a debug.log.
  Torchmaster can be quite verbose when it comes to logging on debug.
  Since these logs are usually useless during normal play, they will be force-disabled be default (only for torchmaster).
  If debug logging is required, launch the game with `-Dtorchmaster.enableDebugLogging=1`

## v21.1.4
- Fix crash during setup of a village siege on fabric
- Add missing torchmaster command from previous versions

## v21.1.3
- Fix spam related to config auto fixing on neoforge

## v21.1.2
- Fix crash when spawning a warden in an ancient city on fabric mod loader

## v21.1.1
- Improve compatibility with other fabric mods
- Re-enable mob spawn blocking during chunk generation inside blocking volumes
- Fix spawn blocking of spawners not working when `blockOnlyNaturalSpawns` is set to `false`
- Fix Feral Flare Lantern not rendering its stand

## v21.1.0
- Improve blocking logic
- FIX: game should no longer crash when architectury is installed
- Known Issue: Mob spawning during chunk generation will not be blocked. (Fabric only) - a fix will be provided in a later version

## v21.0.1 
- Fix invisible light blocks not being replaceable by other blocks ([#234](https://github.com/Xalcon/TorchMaster/issues/234))
- Update to MC 1.21.1

## v21.0.0
- First release for Minecraft 1.21 (Fabric, Neoforge)7