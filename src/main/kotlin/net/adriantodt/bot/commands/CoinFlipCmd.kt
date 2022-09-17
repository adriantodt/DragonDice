package net.adriantodt.bot.commands

import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICommand
import pw.aru.psi.commands.context.CommandContext
import pw.aru.psi.commands.help.HelpEmbed
import pw.aru.psi.commands.help.nodes.CommandUsage
import pw.aru.psi.commands.help.nodes.description
import pw.aru.psi.commands.help.nodes.usage
import pw.aru.utils.extensions.discordapp.stripFormatting
import pw.aru.utils.extensions.lang.randomOf

@Command("coinflip", "flip", "coin")
class CoinFlipCmd : ICommand, ICommand.Discrete {
    override fun CommandContext.call() {
        send(randomOf(heads, tails))
    }

    override fun CommandContext.discreteCall(outer: String) {
        val toSend = outer.replace('\n', ' ').stripFormatting()

        send("**$toSend**\n${randomOf(heads, tails)}")
    }

    override val help = HelpEmbed.command(listOf("coinflip", "flip", "coin"), "CoinFlip Command")
        .description("Flip a coin. Do it now.")
        .usage(
            CommandUsage("coinflip", "Flips a coin.")
        )

    companion object {
        private const val heads = "https://tenor.com/view/champagne-heads-tails-heads-or-tails-coin-gif-13943298"
        private const val tails = "https://tenor.com/view/champagne-heads-tails-heads-or-tails-coin-gif-13943297"
    }
}