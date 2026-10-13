package com.lurarender.proxy;

import com.lurarender.GLProfileGuard;
import com.lurarender.MobileFallbackRenderer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        GLProfileGuard.logBackendSummary();
    }

    public void init(FMLInitializationEvent event) {
        MobileFallbackRenderer.activateIfNeeded();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
