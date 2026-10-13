package com.lurarender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GLProfileGuard {
    private static final Logger LOGGER = LogManager.getLogger("LuaRender/GLProfileGuard");

    private GLProfileGuard() {
    }

    public static boolean shouldRejectUnsafeProfile(int majorVersion, int minorVersion, boolean isOpenGLES, String rendererString, String versionString) {
        if (rendererString == null) {
            rendererString = "unknown";
        }
        if (versionString == null) {
            versionString = "unknown";
        }

        if (isOpenGLES) {
            // The LWJGL OpenGL ES profile is only valid for properly exposed ES contexts.
            // We intentionally avoid forcing ES profile creation when the backend is older or hidden behind a legacy compatibility layer.
            if (majorVersion <= 1 || (majorVersion == 2 && minorVersion < 0)) {
                LOGGER.warn("[LuaRender] OpenGL ES path is unavailable or unusable: version={} renderer={} vendorInfo={}",
                    majorVersion + "." + minorVersion, rendererString, versionString);
                return true;
            }
        }

        if (!isOpenGLES && majorVersion < 2) {
            LOGGER.warn("[LuaRender] Legacy GL backend below minimum support threshold: version={} renderer={} vendorInfo={}",
                majorVersion + "." + minorVersion, rendererString, versionString);
            return true;
        }

        return false;
    }

    public static boolean isLegacyMobilePath(int majorVersion, int minorVersion, boolean isOpenGLES, String rendererString) {
        if (rendererString == null) {
            rendererString = "unknown";
        }

        final boolean lowerThanDesktopCore = majorVersion < 3;
        final boolean likelyHolyGL4ES = rendererString.toLowerCase().contains("gl4es") || rendererString.toLowerCase().contains("angrylizard");

        return (isOpenGLES && majorVersion >= 2) || (lowerThanDesktopCore && likelyHolyGL4ES) || (majorVersion == 2 && !isOpenGLES);
    }

    public static void logBackendSummary() {
        LOGGER.info("[LuaRender] GL compatibility guard active. The renderer is expected to avoid invalid ES/Core profile requests on mobile launchers.");
        LOGGER.info("[LuaRender] Safe policy: only create the requested profile after confirming both GL version and backend support.");
    }
}
