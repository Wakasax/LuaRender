package com.lurarender;

import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GLProfileGuard {
    private static final Logger LOGGER = LogManager.getLogger("LuaRender/GLProfileGuard");

    private GLProfileGuard() {
    }

    public static boolean shouldRejectUnsafeProfile(int majorVersion, int minorVersion, boolean isOpenGLES, String rendererString, String versionString) {
        final String renderer = rendererString == null ? "unknown" : rendererString;
        final String version = versionString == null ? "unknown" : versionString;

        if (isOpenGLES) {
            if (majorVersion <= 1 || (majorVersion == 2 && minorVersion < 0)) {
                LOGGER.warn("[LuaRender] OpenGL ES path is unavailable or unusable: version={} renderer={} vendorInfo={}",
                    majorVersion + "." + minorVersion, renderer, version);
                return true;
            }
        }

        if (!isOpenGLES && majorVersion < 2) {
            LOGGER.warn("[LuaRender] Legacy GL backend below minimum support threshold: version={} renderer={} vendorInfo={}",
                majorVersion + "." + minorVersion, renderer, version);
            return true;
        }

        return false;
    }

    public static boolean isLegacyMobilePath(int majorVersion, int minorVersion, boolean isOpenGLES, String rendererString) {
        if (rendererString == null) {
            return false;
        }

        final String normalized = rendererString.toLowerCase(Locale.ROOT);
        final boolean lowerThanDesktopCore = majorVersion < 3;
        final boolean likelyHolyGL4ES = normalized.contains("gl4es") || normalized.contains("angrylizard") || normalized.contains("mesa llvmpipe");

        return (isOpenGLES && majorVersion >= 2) || (lowerThanDesktopCore && likelyHolyGL4ES) || (majorVersion == 2 && !isOpenGLES);
    }

    public static void logBackendSummary() {
        LOGGER.info("[LuaRender] GL compatibility guard active. The renderer is expected to avoid invalid ES/Core profile requests on mobile launchers.");
        LOGGER.info("[LuaRender] Safe policy: only create the requested profile after confirming both GL version and backend support.");
    }
}
