package xyz.hellocraft.scannerpatcher

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        when (lpparam?.packageName) {
            "com.tencent.mobileqq" -> {
                XposedBridge.log("Hook QQ")
            }
            "com.github.haocen2004.bh3_login_simulation" -> {
                XposedBridge.log("Hook Scanner")
                ScannerHook(lpparam)
            }
            "com.github.haocen2004.bh3_login_simulation.dev" -> {
                XposedBridge.log("Hook Dev Scanner")
                ScannerHook(lpparam)
            }
            else -> {
                XposedBridge.log("Hook Unnecessary Package: ${lpparam?.processName}")

            }
        }

    }
}