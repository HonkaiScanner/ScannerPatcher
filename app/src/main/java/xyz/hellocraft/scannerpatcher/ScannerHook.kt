package xyz.hellocraft.scannerpatcher

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class ScannerHook(lpparam: LoadPackageParam) {
    init {
        XposedBridge.log("Hook " + lpparam.packageName)
        val clazz3 = XposedHelpers.findClass(
            "com.github.haocen2004.login_simulation.login.Tencent",
            lpparam.classLoader
        )
        try {
            XposedHelpers.findAndHookMethod(clazz3, "getHooked", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    param.result = true
                    XposedBridge.log("Scanner Hooked Success.")
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }
}