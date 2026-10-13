package com.lurarender;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MobileFallbackRenderer {
    private static final Logger LOGGER = LogManager.getLogger("LuaRender/MobileFallbackRenderer");

    private MobileFallbackRenderer() {
    }

    public static void activateIfNeeded() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) {
            return;
        }

        final String renderer = mc.getGLVersionString();
        final String vendor = mc.getVersion();
        final boolean isOpenGLES = renderer != null && renderer.toLowerCase().contains("opengl es");

        int major = 0;
        int minor = 0;

        try {
            final String normalized = renderer == null ? "" : renderer.replaceAll("[^"]", "");
            final String[] split = normalized.split("["]");
            if (split.length >= 2) {
                final String versionToken = split[1];
                final String[] versionParts = versionToken.split("\\.");
                if (versionParts.length > 0) {
                    major = Integer.parseInt(versionParts[0].replaceAll("[^0-9]", ""));
                }
                if (versionParts.length > 1) {
                    minor = Integer.parseInt(versionParts[1].replaceAll("[^0-9]", ""));
                }
            }
        } catch (Exception ignored) {
            // Intentionally degrade gracefully; the safe path is to avoid unsupported profile creation.
        }

        final boolean unsafe = GLProfileGuard.shouldRejectUnsafeProfile(major, minor, isOpenGLES, renderer, vendor);
        if (unsafe) {
            LOGGER.warn("[LuaRender] Unsafe GL profile detected. Falling back to the vanilla-safe rendering path instead of forcing ES/Core initialization.");
            return;
        }

        if (GLProfileGuard.isLegacyMobilePath(major, minor, isOpenGLES, renderer)) {
            LOGGER.info("[LuaRender] Legacy mobile GL path detected; safe renderer fallback is active.");
            return;
        }

        LOGGER.info("[LuaRender] Standard desktop-compatible path accepted. Renderer={}", renderer);
    }
}
