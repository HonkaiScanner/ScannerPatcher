package xyz.hellocraft.scannerpatcher

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import kotlin.Throws
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import android.app.Activity
import de.robv.android.xposed.XC_MethodHook
import android.os.Bundle
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

class TencentHook(lpparam: LoadPackageParam) {
    companion object {
        private val digits = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )

        @Throws(NoSuchAlgorithmException::class)
        private fun a(): Array<String?> {
            val messageDigest = MessageDigest.getInstance("MD5")
            var signature: String? = ""
            val sb2 = (System.currentTimeMillis() / 1000).toString()
            val strArr = arrayOf<String?>("", "", "")
            val rawSignature = "6203756aea8a1b7d35bb0222a2444ec3"
            val packageName = "com.tencent.tmgp.bh3"
            try {
//            messageDigest.reset();
                messageDigest.update((packageName + "_" + rawSignature + "_" + sb2).toByteArray())
                signature = bytes2HexStr(messageDigest.digest())
            } catch (e3: Exception) {
                XposedBridge.log(e3)
            }
            strArr[0] = rawSignature
            strArr[1] = signature
            strArr[2] = sb2
            return strArr
        }

        private fun bytes2HexStr(var0: ByteArray?): String? {
            return if (var0 != null && var0.isNotEmpty()) {
                val var1 = CharArray(var0.size * 2)
                for (var2 in var0.indices) {
                    val var3 = var0[var2]
                    var var4: Int
                    val var5 = var2 * 2.also { var4 = it } + 1
                    val var6: CharArray = digits
                    var1[var5] = var6[(var3 and 15).toInt()]
                    val var10002 = (var3.toInt().ushr(4)).toByte()
                    val var7 = var4 + 0
                    var1[var7] = var6[(var10002 and 15).toInt()]
                }
                String(var1)
            } else {
                null
            }
        }
    }

    init {
        XposedBridge.log("Hook " + lpparam.packageName)
        val clazz = XposedHelpers.findClass(
            "com.tencent.open.virtual.OpenSdkVirtualUtil",
            lpparam.classLoader
        )
        val clazz2 = XposedHelpers.findClass(
            "com.tencent.open.agent.strategy.SSOLoginAction",
            lpparam.classLoader
        )
        val clazz3 = XposedHelpers.findClass(
            "com.tencent.open.agent.util.AuthorityUtil",
            lpparam.classLoader
        )
        try {
            XposedHelpers.findAndHookMethod(
                clazz3,
                "c",
                Activity::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val packageName = param.result.toString()
                        XposedBridge.log("hooked AuthorityUtil.c,package_name:$packageName")
                        if (packageName.contains("com.github.haocen2004.bh3_login_simulation")) {
                            param.result = "com.tencent.tmgp.bh3"
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        try {
            XposedHelpers.findAndHookMethod(
                clazz2,
                "d",
                Bundle::class.java,
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val packageName = param.result.toString()
                        XposedBridge.log("hooked SSOLoginAction.d,package_name:$packageName")
                        if (packageName.contains("com.github.haocen2004.bh3_login_simulation")) {
                            param.result = "com.tencent.tmgp.bh3"
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        XposedHelpers.findAndHookMethod(clazz, "a", String::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                val packageName = param.args[0].toString()
                XposedBridge.log("hooked OpenSdkVirtualUtil.a,package_name:$packageName")
                val strings = param.result as Array<*>
                for (string in strings) {
                    XposedBridge.log("old:$string")
                }
                if (strings[0] == "87870b21663deebd4f0ad96e6ac5414d" || packageName.contains("com.github.haocen2004.bh3_login_simulation") || packageName.contains(
                        "com.tencent.tmgp.bh3"
                    )
                ) {
                    XposedBridge.log("replace need,param: $packageName")
                    val arrayOfStrings = a()
                    for (string in arrayOfStrings) {
                        XposedBridge.log("new:$string")
                    }
                    param.result = arrayOfStrings
                    XposedBridge.log("replaced.")
                }
                //
//ret:87870b21663deebd4f0ad96e6ac5414d
//ret:27AC59E4DF2A6446FD2E3619DF7BB46F
//ret:1650184540
                //C72B17A4ED30C124D45204A58D358BDF, timestr=1650184588
                //21D1144D6FED4D26C303DF19C9F8A778
            }
        })
    }
}