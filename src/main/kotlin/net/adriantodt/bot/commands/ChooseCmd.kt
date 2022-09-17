package net.adriantodt.bot.commands

import net.adriantodt.bot.utils.text.ARROW
import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICommand
import pw.aru.psi.commands.context.CommandContext
import pw.aru.psi.commands.help.HelpEmbed
import pw.aru.psi.commands.help.nodes.CommandUsage
import pw.aru.psi.commands.help.nodes.description
import pw.aru.psi.commands.help.nodes.usage
import pw.aru.utils.extensions.discordapp.safeUserInput
import pw.aru.utils.extensions.discordapp.stripFormatting

@Command("choose")
class ChooseCmd : ICommand {

    override fun CommandContext.call() {
        val options = args.takeRemaining()
            .splitToSequence(',')
            .map(String::trim)
            .filterNot(String::isEmpty)
            .toList()

        if (options.isEmpty()) return showHelp()

        send("$ARROW ${options.random().stripFormatting().safeUserInput()}")
    }

    override val help = HelpEmbed.command(listOf("choose"), "Choose Command")
        .description("I'll choose between multiple options for you.")
        .usage(
            CommandUsage(
                "choose <option 1>, <option 2>, [other options separated by comma...]",
                "Choose one of the options."
            )
        )
}