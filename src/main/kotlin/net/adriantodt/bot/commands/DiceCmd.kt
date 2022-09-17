package net.adriantodt.bot.commands

import net.adriantodt.bot.utils.AruDice
import net.adriantodt.bot.utils.text.GAME_DIE
import pw.aru.libs.dicenotation.exceptions.EvaluationException
import pw.aru.libs.dicenotation.exceptions.SyntaxException
import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICommand.CustomHandler.Result
import pw.aru.psi.commands.context.CommandContext
import pw.aru.psi.commands.help.HelpEmbed
import pw.aru.psi.commands.help.nodes.description
import pw.aru.psi.commands.help.nodes.example
import pw.aru.psi.commands.help.nodes.note
import pw.aru.psi.executor.Executable
import pw.aru.psi.executor.RunEvery
import pw.aru.utils.extensions.discordapp.safeUserInput
import pw.aru.utils.extensions.discordapp.stripFormatting
import java.util.concurrent.TimeUnit
import pw.aru.psi.commands.ICommand.CustomDiscreteHandler as DiscreteCustomCommand
import pw.aru.psi.commands.ICommand.CustomHandler as CustomCommand
import pw.aru.psi.commands.ICommand.Discrete as DiscreteCommand

@Command("roll", "dice")
@RunEvery(0, 65536, TimeUnit.MILLISECONDS)
class DiceCmd : DiscreteCommand, CustomCommand, DiscreteCustomCommand, Executable {
    private val dicePattern = Regex("^\\d*d\\d+", RegexOption.IGNORE_CASE)
    private val dice = AruDice()

    override fun run() {
        dice.reseed()
    }

    private fun resolveRoll(args: String, simple: Boolean = false): String {
        when {
            args.startsWith("-simple") -> return resolveRoll(args.substring(7), true)
            args.endsWith("-simple") -> return resolveRoll(args.substring(0, args.length - 7), true)
            args.isBlank() -> return resolveRoll("d20", simple)
        }

        return try {
            dice.roll(args, simple)
        } catch (e: Exception) {
            when (e) {
                is SyntaxException,
                is EvaluationException,
                is IllegalArgumentException,
                is IllegalStateException -> "Error: ${e.message}"
                else -> throw e
            }
        }
    }

    override fun CommandContext.call() {
        send("$GAME_DIE **${author.name.safeUserInput()}**, ${resolveRoll(args.takeRemaining())}")
    }

    override fun CommandContext.discreteCall(outer: String) {
        val toSend = outer.replace('\n', ' ').stripFormatting().trim()

        if (toSend.isEmpty()) return call()

        send("**$toSend**\n$GAME_DIE ${resolveRoll(args.takeRemaining())}")
    }

    override fun CommandContext.customCall(command: String): Result {
        if (!dicePattern.containsMatchIn(command)) return Result.IGNORE

        try {
            send(
                "$GAME_DIE **${
                    author.name.safeUserInput()
                }**, ${resolveRoll("$command ${args.takeRemaining()}")}".trim()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Result.HANDLED
    }

    override fun CommandContext.customCall(command: String, outer: String): Result {
        if (!dicePattern.containsMatchIn(command)) return Result.IGNORE

        val toSend = outer.replace('\n', ' ').stripFormatting().trim()

        if (toSend.isEmpty()) {
            send(
                "$GAME_DIE **${
                    author.name.safeUserInput()
                }**, ${resolveRoll("$command ${args.takeRemaining()}".trim())}"
            )
        } else {
            send("**$toSend**\n$GAME_DIE ${resolveRoll(args.takeRemaining())}")
        }
        return Result.HANDLED
    }

    override val help = HelpEmbed.command(listOf("roll", "dice"), "Dice Command")
        .description("Rolls a dice, which needs to be written in dice notation.")
        .example(
            "dice d20",
            "dice 2d10",
            "dice 1d5 - 2",
            "dice 3d4 + 1d20",
            "dice d360 * pi"
        )
        .note("Dice notations are considered commands too.")
}