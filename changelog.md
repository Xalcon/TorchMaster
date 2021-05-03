## v2.3.8
- Improve compatibility with Apotheosis, may break compat with other mods if the mod is "doing it wrong" #150

## v2.3.7
- Fix phantoms not being blocked by shroomdog27
- Add kr-kr Translation by myoun
- Add zh-cn Translation by 1478599553

## v2.3.6
- Update to MC 1.16.5

## v2.3.5-alpha
- Update to MC 1.16.4

## v2.3.4-alpha
- Update to MC 1.16.3

## v2.3.3-alpha
- Update to MC 1.16.2 (Forge 33.0.0)

## v2.3.2-alpha
- replace InvisibleLight Blocks from Feral Flare Lantern with actual light emitting Air Blocks. This should improve compatibility with other mods and MC itself.

## v2.3.1-alpha
- add aggresiveSpawnChecks configuration option. This will override the spawn checks of other mods if they returned "ALLOW" earlier in the chain.
- Remove fatal logging of config changes on disk

### v2.3.0-alpha
 - Initial Port to MC 1.16

### v2.2.1
 - Add missing Translations [thanks to ChloeDawn]
 - Do not log debug statements by default [thanks to ChloeDawn]

### v2.2.0
 - Last feature release for MC 1.15, sorry no terrain lighter :/
 - Fix dread lamp not working at all
 - Fix dread lamp hit box

### v2.1.0-rc1
 - Upgrade to MC 1.15.2

### v2.1.0-rc0
 - Initial Port to 1.15.1

### v2.0.0-rc2
 - Fixed **feral flare lantern** and **dreadlamp** not dropping when broken.

### v2.0.0-rc1
 - Fixed tesselating crash when placing a feral flare lantern

### v2.0.0-rc0
 - first release candidate, written from scratch
 - Improved Mega Torch from first 1.14 release
 - Added Dread Lamp
 - Added Feral Flare Lantern (LoS feature is currently missing but will come in the next release candidate)
 - Added Frozen Pearl (Not really that useful anymore since the lantern now removes all lights when it gets removed, but just in case)
 - Added entity black/white listing for megatorch and dreadlamp via config
