## v21.0.3
- Fix crash when spawning a warden in an ancient city on fabric mod loader
- Improve compatibility with other fabric mods
- Re-enable mob spawn blocking during chunk generation inside blocking volumes
- Fix spawn blocking of spawners not working when `blockOnlyNaturalSpawns` is set to `false`
- Fix Feral Flare Lantern not rendering its stand

## v21.0.2
- Downgrade to MC 1.21 for fabric compatibility
- FIX: Improve blocking logic (fabric, neoforge)
- FIX: Fixed a crash when architectory is installed alongside torchmaster (fabric)
- Known Issue: Mob spawns during chunk generation will not be blocked. (Only affects configs with very big radius values)

## v21.0.1 
- Fix invisible light blocks not being replaceable by other blocks ([#234](https://github.com/Xalcon/TorchMaster/issues/234))
- Update to MC 1.21.1

## v21.0.0
- First release for Minecraft 1.21 (Fabric, Neoforge)