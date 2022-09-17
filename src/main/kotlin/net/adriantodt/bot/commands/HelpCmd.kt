package net.adriantodt.bot.commands

import net.adriantodt.bot.utils.extensions.psi.onHelp
import net.adriantodt.bot.utils.styling
import net.adriantodt.bot.utils.text.ERROR
import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICategory
import pw.aru.psi.commands.ICommand
import pw.aru.psi.commands.context.CommandContext
import pw.aru.psi.commands.help.HelpEmbed
import pw.aru.psi.commands.help.nodes.CommandUsage
import pw.aru.psi.commands.help.nodes.description
import pw.aru.psi.commands.help.nodes.usage
import pw.aru.psi.commands.manager.CommandRegistry
import pw.aru.psi.executor.RunEvery
import pw.aru.utils.extensions.lib.description
import java.util.concurrent.TimeUnit

@Command("help")
class HelpCmd(private val registry: CommandRegistry) : ICommand {

    override fun CommandContext.call() {
        if (args.isEmpty()) {
            botHelp()
        } else {
            findHelp(args.takeRemaining().trim().toLowerCase())
        }
    }

    private fun CommandContext.botHelp() {
        sendEmbed {
            styling(message).author("DragonDice | Help").applyAll()

            description(
                registry.commandNames().joinToString(prefix = "`", separator = "` `", postfix = "`"),
                "\nTo check the command usage, type `${def.prefixes.first()}help <command>`."
            )

            setFooter(
                "${registry.commandCount()} commands | Requested by ${author.name}",
                author.effectiveAvatarUrl
            )
        }
    }

    private fun CommandContext.findHelp(args: String) {
        registry.command(args)?.let {
            onHelp(it, message, def)
            return
        }

        send("$ERROR There's no command with that name!")
    }

    private val jokes = listOf(
        "You helped yourself.",
        "Congrats, you managed to use the help command.",
        "Yo damn I heard you like help, because you just issued the help command to get the help about the help command.",
        "Helps you to help yourself.",
        "Help Inception.",
        "A help helping helping helping help.",
        "I wonder if this is what you are looking for..."
    )

    private val helps = jokes.map { joke ->
        HelpEmbed.command(listOf("help", "h"), "Help Command")
            .description("**$joke**")
            .usage(
                CommandUsage("help", "Lists all commands."),
                CommandUsage("help <command>", "Displays a command's help."),
                CommandUsage("help <category>", "Displays a category's help.")
            )
    }

    override val help get() = helps.random()
}

