package net.adriantodt.bot.commands

import net.adriantodt.bot.utils.styling
import net.adriantodt.bot.Bot
import net.adriantodt.bot.exported.bot_version
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.sharding.ShardManager
import org.kodein.di.direct
import org.kodein.di.generic.instance
import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICommand
import pw.aru.psi.commands.context.CommandContext
import pw.aru.utils.extensions.lang.format
import pw.aru.utils.extensions.lib.field
import pw.aru.utils.extensions.lib.inlineField

@Command("stats", "stat")
class StatsCmd : ICommand {

    override fun CommandContext.call() {
        sendEmbed {
            Thread.getAllStackTraces()
            styling(message).author("DragonDice | Discord Stats").applyAll()
            field("Uptime:", Bot.uptime)
            inlineField(
                "Bot Stats:",
                "\u25AB **Aru Version**: $bot_version",
                "\u25AB **JDA Version**: ${JDAInfo.VERSION}",
                "\u25AB **Shards**: ${direct.instance<ShardManager>().shardsTotal.format("%,d")}"
                //"\u25AB **Commands**: ${processor.count.format("%,d")} executed"
            )
        }
    }
}