package net.adriantodt.bot.commands

import net.adriantodt.bot.utils.styling
import pw.aru.psi.commands.Command
import pw.aru.psi.commands.ICommand
import pw.aru.psi.commands.context.CommandContext
import pw.aru.psi.commands.help.HelpEmbed
import pw.aru.psi.commands.help.nodes.description
import pw.aru.utils.extensions.lib.field

@Command("invite", "links")
class InviteCmd : ICommand {
    private var inviteUrl: String? = null

    override fun CommandContext.call() {
        val url = inviteUrl ?: jda.retrieveApplicationInfo().complete().getInviteUrl().also { inviteUrl = it }
        sendEmbed {
            styling(message)
                .author("DragonDice | Invite")
                .applyAll()

            field("Add to Server", url)
            field("Support", "Contact AdrianTodt#0722")
        }
    }

    override val help = HelpEmbed.command(listOf("invite", "links"), "Add to Server Link")
        .description("Provides Add to Server link.")
}