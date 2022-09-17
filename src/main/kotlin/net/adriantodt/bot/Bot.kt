package net.adriantodt.bot

import net.adriantodt.bot.exported.bot_version
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import pw.aru.psi.BotDef
import pw.aru.psi.PsiApplication
import pw.aru.utils.extensions.lang.Environment
import pw.aru.utils.extensions.lang.toSmartString
import java.awt.Color
import java.lang.management.ManagementFactory
import java.util.*

object Bot : BotDef {
    override val botName = "DragonDice"
    override val prefixes = listOf("d!")
    override val basePackage = "net.adriantodt.bot"
    override val version = bot_version

    override val mainColor = Color(0xff0000)
    override val splashes = listOf("Roll'it!")

    override val builder = DefaultShardManagerBuilder.createLight(
        EnvVars.BOT_TOKEN, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES
    )

    object EnvVars {
        val BOT_TOKEN by Environment
    }

    @JvmStatic
    fun main(args: Array<String>) {
        PsiApplication(this).init()
    }

    val uptime get() = humanizedTime(rawUptime)
    private val rawUptime get() = ManagementFactory.getRuntimeMXBean().uptime

    private fun humanizedTime(millis: Long): String {
        val days = millis / 86400000
        val hours = millis / 3600000 % 24
        val minutes = millis / 60000 % 60
        val seconds = millis / 1000 % 60

        val parts = LinkedList<String>()

        if (days > 0) parts += "$days ${if (days == 1L) "day" else "days"}"
        if (hours > 0) parts += "$hours ${if (hours == 1L) "hour" else "hours"}"
        if (minutes > 0) parts += "$minutes ${if (minutes == 1L) "minute" else "minutes"}"
        if (seconds > 0) parts += "$seconds ${if (seconds == 1L) "second" else "seconds"}"

        return if (parts.isEmpty()) "0 seconds (about now...)" else parts.toSmartString()
    }

}