package com.lurarender;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MobileFallbackRenderer {
    private static final Logger LOGGER = LogManager.getLogger("LuaRender/MobileFallbackRenderer");
    private static final Pattern VERSION_PATTERN = Pattern.compile("(?:OpenGL(?:\\s+ES)?\\s+)?(\\d+)(?:\\.(\\d+))?");

    private MobileFallbackRenderer() {
    }

    public static void activateIfNeeded() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) {
            return;
        }

        final String renderer = mc.getGLVersionString();
        final String versionString = mc.getVersion();
        final boolean isOpenGLES = renderer != null && renderer.toLowerCase(Locale.ROOT).contains("opengl es");

        final int[] parsed = parseVersion(renderer);
        final int major = parsed[0];
        final int minor = parsed[1];

        final boolean unsafe = GLProfileGuard.shouldRejectUnsafeProfile(major, minor, isOpenGLES, renderer, versionString);
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

    private static int[] parseVersion(String glVersion) {
        int major = 0;
        int minor = 0;

        if (glVersion == null || glVersion.trim().isEmpty()) {
            return new int[] { major, minor };
        }

        final Matcher matcher = VERSION_PATTERN.matcher(glVersion);
        if (matcher.find()) {
            try {
                major = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ignored) {
                major = 0;
            }

            try {
                minor = Integer.parseInt(matcher.group(2) == null ? "0" : matcher.group(2));
            } catch (NumberFormatException ignored) {
                minor = 0;
            }
        }

        return new int[] { major, minor };
    }
}
