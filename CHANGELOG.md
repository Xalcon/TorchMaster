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