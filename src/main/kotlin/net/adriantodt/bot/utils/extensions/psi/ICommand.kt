@file:Suppress("NOTHING_TO_INLINE")

package net.adriantodt.bot.utils.extensions.psi

import net.adriantodt.bot.utils.text.ERROR
import net.dv8tion.jda.api.entities.Message
import pw.aru.psi.BotDef
import pw.aru.psi.commands.ICommand

fun onHelp(command: ICommand, message: Message, def: BotDef) {
    command.help?.let {
        message.channel.sendMessage(it.onHelp(def, message)).queue()
        return
    }

    message.channel.sendMessage("$ERROR Heh. Sorry, but the command doesn't provide any help.").queue()
}
