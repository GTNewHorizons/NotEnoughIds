# NotEnoughIds

A forked and heavily updated version of https://www.curseforge.com/minecraft/mc-mods/notenoughids for Minecraft 1.7.10

## Version Matrix

Ultimately the current release of NEID provides extended Block IDs and extended Metadata. Both are extended to 16-bits with
a maximum value of 32,767. Below is a table describing the various versions of this fork of NEID, and how they relate to the
original and newer features.

| NEID Version | Comment                                                                      |
| ------------ |------------------------------------------------------------------------------|
| 1.4.X        | Largely exact same as original mod with very minor tweaks to keep it working |
| 1.5.X        | Complete re-write of most of the mod from ASM to Mixins, same functionality  |
| 2.X and up   | Extended block metadata to 16-bits(maximum value of 32,767)                  |

## Depending On Metadata Increase In Another Mod

**NOTE:** This API is available as of version 2.2.0 of NEID.

In order to use the metadata, you should not create a hard dependency on the `neid` Mod ID, but rather on the `idextender` Mod ID.
NEID bundles this "dummy" mod alongside itself. This Mod ID and API are intended to be able to be placed inside other ID extension mods,
so that you can create a hard dependency on ANY ID extension mod which implements this simple API.

Here is an example of how to use it, you could put this code in your Mod's pre-init for example:

```java
import idextender;

int maxMeta = 15;

if (Loader.isModLoaded("idextender")) {
    maxMeta = (Integer) Class.forName("idextender.Metadata").getDeclaredMethod("maximumMetadata").invoke(null);
}
```

You can also query for the number of bits the metadata value is extended to, rather than the maximum value:

```java
import idextender;

int metadataBits = 4;

if (Loader.isModLoaded("idextender")){
    metadataBits = (Integer) Class.forName("idextender.Metadata").getDeclaredMethod("metadataBits").invoke(null);
}
```

If for some reason, you need to know what parent mod is providing the `idextender` mod, you can do the following, which will give you
back the Mod ID of the actual mod that has provided `idextender`. For example, `neid`:

```java
// This will give back the value "neid" which is NotEnoughID's Mod ID
String parentIDExtender = (String) Class.forName("idextender.IDExtender").getDeclaredMethod("parentModId").invoke(null);
```

## Implementing the shared idextender mod

If you are working on an ID Extension mod and would like to include the idextender shared mod. You can simply copy the `idextender`
package from NEID, and change the return values to fit your mod.

## License

GTNH modifications Copyright (c) 2021-2022 The GTNH Team

This code is licensed LGPL v3.0 or later. Feel free to use our changes, just give back any changes you make to the community as well!

Original decompiled code Copyright (c) fewizz and was released under the MIT license.
