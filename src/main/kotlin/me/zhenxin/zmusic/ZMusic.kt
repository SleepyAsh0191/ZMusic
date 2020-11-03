package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.Player
import me.zhenxin.zmusic.module.Sender
import me.zhenxin.zmusic.module.Tasker
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager

object ZMusic {
    var isBC = false
    var plugin: Any? = null
    var logger: Logger? = null
    var tasker: Tasker? = null
    var sender: Sender? = null
    var player: Player? = null

    var dataFolder: File? = null
    var thisVer: String? = null
    var thisVerCode = 202009190
    var bilibiliIsVIP = false
    var isViaVer = true
    var isEnable: Boolean = true
    var isEnabled: Boolean = false

    fun enable() {
        val logo = ("\n" +
                "  ______  __  __                 _        \n" +
                " |___  / |  \\/  |               (_)       \n" +
                "    / /  | \\  / |  _   _   ___   _    ___ \n" +
                "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
                "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
                " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n" +
                "                                          \n").split("\n")
        logo.forEach {
            logger?.log("§b$it")
        }
        logger?.log("\t§6v$thisVer($thisVerCode)\tby ZhenXin")
        logger?.log("")
        logger?.log(Lang.Loading.loading)
        val manager = CookieManager()
        CookieHandler.setDefault(manager)
        Config.load()
        logger?.log(Lang.Loading.loaded + "\n\n")
    }
}
