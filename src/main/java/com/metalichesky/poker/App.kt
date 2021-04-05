package com.metalichesky.poker

import com.metalichesky.poker.model.*
import com.metalichesky.poker.repo.CardsRepo
import com.metalichesky.poker.util.PokerHandCombinator
import com.metalichesky.poker.util.PokerStartCardsTableReader
import com.metalichesky.poker.util.PokerStartCardsTableWriter
import com.metalichesky.poker.util.PokerStatisticTableWriter
import java.io.File
import kotlin.random.Random

class App {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            App().startApp()
        }
    }

    val statistic = PokerStatistic()

    private val player1 = Player("Player1")
    private val player2 = Player("Player2")
    private val players = listOf(player1, player2)
    private val random = Random(System.currentTimeMillis())
    private val gamesNumber = 50_000

    fun startApp() {
        playGame1()

//        readStatistic()
    }

    private fun playGame1() {
        val allCards = CardsRepo.getAllCards()
        val allCardNames = CardsRepo.getAllCardNames()
        statistic.clearResults()
        val fileName = "table"
//                "${card1.name.shortCardName}_${card1.suit.suitName}_${card2.name.shortCardName}_${card2.suit.suitName}"
        val filePath = "C:\\Users\\Dmitriy\\Desktop\\Poker\\${fileName}.csv"
        val pokerStatisticWriter = PokerStatisticTableWriter()
        pokerStatisticWriter.prepare(filePath)

        val listener = object : TexasHoldemPokerGame.Listener {
            override fun onDistributeCards(player: Player, count: Int, freeCards: MutableList<Card>): List<Card> {
                return if (player == player1) {
                    val twos = mutableListOf<Card>()
                    freeCards.find {
                        it.name == CardName.ACE
                    }?.let {
                        twos.add(it)
                        freeCards.remove(it)
                    }
                    freeCards.find {
                        it.name == CardName.ACE
                    }?.let {
                        twos.add(it)
                        freeCards.remove(it)
                    }
                    twos
                } else {
                    val twos = mutableListOf<Card>()
//                    val randomCardName = allCardNames.random(random)
                    freeCards.find {
                        it.name == CardName.KING
                    }?.let {
                        twos.add(it)
                        freeCards.remove(it)
                    }
                    freeCards.find {
                        it.name == CardName.KING
                    }?.let {
                        twos.add(it)
                        freeCards.remove(it)
                    }
                    twos
                }
            }

            override fun onResult(result: PokerResult) {
                statistic.addResult(result)
            }
        }
        val game = TexasHoldemPokerGame(
            players,
            listener
        )

        for (i in 0..gamesNumber) {
            System.out.println("Round ${i}")
            game.playRound()
            if (i % 100 == 0) {
                pokerStatisticWriter.writeIntoFile(player1, statistic)
            }
            System.out.println("Round ${i} done")
        }
        pokerStatisticWriter.complete()
        System.out.println(statistic)
        players.forEach {
            val winProbability = statistic.getWinProbability(it)
            val loseProbability = statistic.getLoseProbability(it)
            System.out.println("${it.name} win ${winProbability} lose ${loseProbability}")
        }
    }

    private fun playGame() {
        val allCards = CardsRepo.getAllCards()
        val combinator = PokerHandCombinator(allCards, 2)
        var startCards = combinator.nextCombination()
        while (startCards != null) {
            statistic.clearResults()
            val card1 = startCards[0]
            val card2 = startCards[1]
            val fileName =
                "${card1.name.shortCardName}_${card1.suit.suitName}_${card2.name.shortCardName}_${card2.suit.suitName}"
            val filePath = "C:\\Users\\Dmitriy\\Desktop\\Poker\\${fileName}.csv"
            if (!File(filePath).exists()) {
                playGame(fileName, filePath, startCards)
            }
            startCards = combinator.nextCombination()
        }

        val filePath = "C:\\Users\\Dmitriy\\Desktop\\Poker\\total.csv"
        val startCardsTableWriter = PokerStartCardsTableWriter()
        startCardsTableWriter.prepare(filePath)
        startCardsTableWriter.writeIntoFile(statistic)
        startCardsTableWriter.complete()
        System.out.println("Done!")
    }

    private fun playGame(fileName: String, filePath: String, startCards: List<Card>) {
        val card1 = startCards[0]
        val card2 = startCards[1]
        val pokerStatisticWriter = PokerStatisticTableWriter()
        pokerStatisticWriter.prepare(filePath)
        val listener = object : TexasHoldemPokerGame.Listener {
            override fun onDistributeCards(player: Player, count: Int, freeCards: MutableList<Card>): List<Card> {
                return if (player == player1) {
                    val twos = mutableListOf<Card>()
                    freeCards.find {
                        it.isSameCard(card1)
                    }?.let {
                        twos.add(it)
                        freeCards.remove(it)
                    }
                    freeCards.find {
                        it.isSameCard(card2)
                    }?.let {
                        twos.add(it)
                        freeCards.remove(it)
                    }
                    twos
                } else {
                    val cards = freeCards.takeLast(count)
                    freeCards.removeAll(cards)
                    cards
                }
            }

            override fun onResult(result: PokerResult) {
                statistic.addResult(result)
            }
        }
        val game = TexasHoldemPokerGame(
            players,
            listener
        )
        for (i in 0..gamesNumber) {
            System.out.println("Round ${i}")
            game.playRound()
            if (i % (gamesNumber / 1000) == 0) {
                pokerStatisticWriter.writeIntoFile(player1, statistic)
            }
            System.out.println("Round ${i} done")
        }
        pokerStatisticWriter.complete()
        System.out.println(statistic)
        players.forEach {
            val winProbability = statistic.getWinProbability(it)
            val loseProbability = statistic.getLoseProbability(it)
            System.out.println("${it.name} win ${winProbability} lose ${loseProbability}")
        }
        val winProbability = statistic.getWinProbability(player1)
        val loseProbability = statistic.getLoseProbability(player1)
        statistic.startCardsWinsProbability[fileName] = winProbability
        statistic.startCardsLosesProbability[fileName] = loseProbability
    }

    private fun readStatistic() {
        val reader = PokerStartCardsTableReader()
        val filePath = "C:\\Users\\Dmitriy\\Desktop\\Poker\\total.csv"
        reader.prepare(filePath)
        val table = reader.readFromFile()
        reader.complete()
        System.out.println("Table")
        val startCards = table.map {
//            System.out.println("row ${it}")
            val cards = Card.fromLongString(it.startCards).sortedWith(Card.strongComparator).reversed()
            System.out.println("cards ${cards.joinToString(" ")} win probability ${it.winProbability} loseProbability ${it.loseProbability}")
            cards
        }.sortedWith(Card.strongListComparator)
        startCards.forEach {
            System.out.println("cards ${it.joinToString(" ")}")
        }

    }
}