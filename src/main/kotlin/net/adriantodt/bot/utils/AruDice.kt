package net.adriantodt.bot.utils

import pw.aru.libs.dicenotation.evaluator.DiceEvaluatorBuilder
import pw.aru.libs.dicenotation.evaluator.DiceSolver
import pw.aru.libs.dicenotation.lexer.DiceLexer
import pw.aru.libs.dicenotation.parser.DiceParser
import pw.aru.utils.extensions.lang.randomOf
import java.util.*
import kotlin.math.*

class AruDice {
    private val r = Random()

    private fun internalRoll(sides: Int) = r.run {
        ((nextDouble() * 0.9 + nextDouble() * nextDouble() * nextDouble() * 0.9) * sides)
            .toInt().coerceIn(0, sides - 1).plus(1)
    }

    fun reseed() {
        r.setSeed(3447679086515839964L xor System.nanoTime())
    }

    private val evaluator = DiceEvaluatorBuilder()
        .value("pi", Math.PI)
        .value("e", Math.E)
        .value("r", Math::random)
        .valueAlias("r", "rand", "rdn", "random")
        .function("log10") { log10(it[0].toDouble()) }
        .function("log2") { log2(it[0].toDouble()) }
        .function("ln") { ln(it[0].toDouble()) }
        .function("sin") { sin(it[0].toDouble()) }
        .function("cos") { cos(it[0].toDouble()) }
        .function("tan") { tan(it[0].toDouble()) }
        .function("random") { internalRoll(it[0].toInt()) }
        .function("average") { sequenceOf(*it).map(Number::toDouble).average() }
        .function("any") { randomOf(*it) }
        .function("int") { it[0].toInt() }
        .function("double") { it[0].toDouble() }
        .function("abs") { it[0].let { n -> if (n is Int) abs(n.toInt()) else abs(n.toDouble()) } }
        .functionAlias("random", "rand", "rdn", "r", "roll")
        .functionAlias("average", "avg")
        .functionAlias("sin", "sen")
        .functionAlias("int", "integer", "long", "round")
        .functionAlias("double", "float", "decimal")
        .build()

    private val solver = DiceSolver(::internalRoll)

    fun roll(text: String, simple: Boolean): String {
        val diceExpr = DiceParser(DiceLexer(text)).parse().accept(solver)
        val solvedValue = diceExpr.accept(evaluator)

        val diceText by lazy(diceExpr::toString)
        val valueText by lazy(solvedValue::toString)

        if (simple)
            return valueText
        val fullText = "$valueText ⟵ $diceText"
        return if (fullText.length > 1900) "$valueText (Output simplified for being too long)" else fullText
    }
}
