package com.lurarender;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = LuaRender.MOD_ID,
    name = LuaRender.MOD_NAME,
    version = LuaRender.VERSION,
    acceptableRemoteVersions = "*",
    acceptedMinecraftVersions = "[1.7.10]"
)
public class LuaRender {
    public static final String MOD_ID = "lurarender";
    public static final String MOD_NAME = "LuaRender";
    public static final String VERSION = "0.1.0-experimental";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("[LuaRender] Initializing mobile-safe render compatibility layer.");
        GLProfileGuard.logBackendSummary();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("[LuaRender] Vanilla render path is preserved unless the backend passes compatibility checks.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("[LuaRender] Ready. Mobile fallback renderer is active only when supported by the host GL context.");
    }
}
