# LuaRender

LuaRender is a mobile-focused rendering compatibility layer and optimization scaffold for Minecraft 1.7.10, derived from the GTNewHorizons/Angelica codebase and designed to avoid the invalid OpenGL ES profile path that can crash when a launcher or GL backend does not expose the expected capabilities.

This repository is intentionally conservative:
- it preserves the vanilla rendering path by default;
- it detects OpenGL capabilities before requesting a Core/ES profile;
- it exposes a mobile fallback renderer for Holy GL4ES / OpenGL 2.1-like environments;
- it keeps the integration isolated so it can coexist with the existing modpack without forcing a full Angelica replacement.

## Baseline reference

The project is derived from the upstream Angelica codebase:
- https://github.com/GTNewHorizons/Angelica
- commit reviewed for architecture: 0cae2be1f6506d6079a2f04fae9f9178b2ce3eb0

## Design goals

1. Never request an OpenGL ES context profile unless the underlying implementation actually supports it.
2. Avoid forcing GL 3.3 Core when the active backend is really GL 2.1 / legacy compatibility.
3. Keep a vanilla-safe fallback available if a GPU/backend is not capable of the optimizations.
4. Favor stable FPS and safe startup over aggressive optimization in unsupported environments.
5. Keep this project independent from the original Angelica installation and avoid duplicate library class loading.

## Mobile compatibility strategy

The root issue behind the crash pattern is the invalid use of `ContextAttribs.withProfileES(...)` for unsupported versions. In the OpenGL ES path, the LWJGL API only supports profile ES when the requested version is effectively GL 2.0 or above, and a bad profile or an invalid backend path can throw `IllegalArgumentException` before the display is created.

This project addresses that by:
- detecting real OpenGL/ES versions before creating the display context;
- never forcing a desktop Core profile against a GL2.1/ES backend;
- leaving rendering to the vanilla pipeline when compatibility cannot be proven;
- logging the reason for any disabled optimization path.

## Current status

This repository is intentionally a compatibility-focused starting point, not a full Angelica reimplementation. The work here is structured around the safe bootstrap layer and the mobile fallback backbone needed to avoid the crash class described in the Angelica issue.

## Build status

Actual build/test execution is not performed in this environment because the Java/Forge toolchain and Android launcher environment are not available here. The project is organized for a standard Forge 1.7.10 build and can be compiled in a normal dev environment.

## Installing in a Forge pack

1. Build the JAR with ForgeGradle.
2. Place it in the `mods` folder.
3. Launch the pack with the target launcher.
4. Check logs for `LuaRender` and `GLProfileGuard` messages.

## Known limitations

- This is not a drop-in Angelica replacement.
- This repo does not claim full mobile optimization parity yet.
- Actual FPS gains and compatibility with DBC Super must be measured on the target Android device.

## License

Licensed under MIT unless otherwise noted by upstream Angelica components used as references.
