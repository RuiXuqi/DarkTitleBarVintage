package cn.ksmcbrigade.darktitlebar;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
        clientSideOnly = true, acceptableRemoteVersions = "*", dependencies = "required-after:cleanroom")
public class DarkTitleBar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reference.MOD_ID);

    public static void setDarkTitlebar() {
        // check if we are running on windows
        if (Platform.getOSType() != Platform.WINDOWS) {
            LOGGER.warn("DarkTitleBar only works on windows!");
            return;
        }

        // check for supported windows versions
        WinNT.OSVERSIONINFO osversioninfo = new WinNT.OSVERSIONINFO();

        Kernel32.INSTANCE.GetVersionEx(osversioninfo);

        if (osversioninfo.dwMajorVersion.longValue() < 10 || osversioninfo.dwBuildNumber.longValue() < 17763) { // 1809
            LOGGER.warn("Dark Title Bar requires Windows 10 version 1809 or newer!");
            return;
        }

        // get hwnd
        long glfwWindow = Display.getWindow();
        long hwndLong = GLFWNativeWin32.glfwGetWin32Window(glfwWindow);
        WinDef.HWND hwnd = new WinDef.HWND(Pointer.createConstant(hwndLong));

        // set dark mode
        Memory mem = new Memory(Native.POINTER_SIZE);
        mem.setInt(0, 1);
        DwmApi.INSTANCE.DwmSetWindowAttribute(
                hwnd,
                DwmApi.DWMWA_USE_IMMERSIVE_DARK_MODE,
                new WinDef.LPVOID(mem),
                new WinDef.DWORD(WinDef.DWORD.SIZE)
        );
        mem.close();
    }

    private interface DwmApi extends StdCallLibrary {

        DwmApi INSTANCE = Native.load("dwmapi", DwmApi.class, W32APIOptions.DEFAULT_OPTIONS);

        WinDef.DWORD DWMWA_USE_IMMERSIVE_DARK_MODE = new WinDef.DWORD(20);

        @SuppressWarnings("UnusedReturnValue")
        WinNT.HRESULT DwmSetWindowAttribute(
                WinDef.HWND hwnd,
                WinDef.DWORD dwAttribute,
                WinDef.LPVOID pvAttribute,
                WinDef.DWORD cbAttribute
        );

    }
}
