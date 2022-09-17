package net.adriantodt.bot.utils

import net.adriantodt.bot.Bot
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import pw.aru.utils.extensions.lang.applyOn

class EmbedStyler(private val builder: EmbedBuilder, private val message: Message) {
    fun botColor() = applyOn(builder) {
        setColor(Bot.mainColor)
    }

    fun autoFooter() = applyOn(builder) {
        setFooter(
            "Requested by ${message.author.name}",
            message.author.effectiveAvatarUrl
        )
    }

    fun author(
        name: String,
        url: String? = null,
        image: String? = message.jda.selfUser.effectiveAvatarUrl
    ) = applyOn(builder) {
        setAuthor(name, url, image)
    }


    fun applyAll() = this
        .botColor()
        .autoFooter()
}

fun EmbedBuilder.styling(message: Message) = EmbedStyler(this, message)