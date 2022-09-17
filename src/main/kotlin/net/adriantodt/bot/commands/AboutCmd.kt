package net.adriantodt.bot.commands

import net.adriantodt.bot.utils.styling
import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICommand
import pw.aru.psi.commands.context.CommandContext
import pw.aru.psi.commands.help.HelpEmbed
import pw.aru.psi.commands.help.nodes.CommandUsage
import pw.aru.psi.commands.help.nodes.description
import pw.aru.psi.commands.help.nodes.usage
import pw.aru.utils.extensions.lib.description
import pw.aru.utils.extensions.lib.field

@Command("about")
class AboutCmd : ICommand {

    override fun CommandContext.call() {
        when (args.takeString()) {
            "credits", "credit" -> credits()
            "me", "bot", "" -> about()
            else -> showHelp()
        }
    }

    private fun CommandContext.credits() {
        sendEmbed {
            styling(message).author("DragonDice | Credits").applyAll()
            setThumbnail(author.effectiveAvatarUrl)
            field("Developer", "AdrianTodt#0722")
        }
    }

    private fun CommandContext.about() {
        sendEmbed {
            styling(message).author("DragonDice | About").applyAll()
            description(
                "I'm a bot that rolls dice. That's it.",
                "Send `${def.prefixes.first()}${"d20"}` to roll a 20-sized die.",
                "Send `${def.prefixes.first()}${"help"}` to check my command list!",
                "Send `${def.prefixes.first()}${"about credits"}` to see the credits."
            )
        }
    }

    override val help = HelpEmbed.command(listOf("about"), "About Command")
        .description("Learn more about me!")
        .usage(
            CommandUsage("about [me/bot]", "Let me introduce myself."),
            CommandUsage("about credits", "A bit about the people that make me alive.")
        )
}