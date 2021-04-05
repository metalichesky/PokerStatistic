package com.metalichesky.poker.model

import com.metalichesky.poker.repo.CardsRepo
import kotlin.math.absoluteValue
import kotlin.random.Random

class TexasHoldemPokerGame(
    var players: List<Player>,
    var listener: Listener
) {
    private val randoms = listOf(
        Random(System.currentTimeMillis()),
        Random((System.currentTimeMillis() - Random(System.currentTimeMillis()).nextInt()).absoluteValue),
        Random((System.currentTimeMillis() + Random(System.currentTimeMillis()).nextInt()).absoluteValue)
    )
    val allCards: List<Card> = CardsRepo.getAllCards()
    private var freeCards: MutableList<Card> = allCards.toMutableList()
    private var sharedCards: MutableList<Card> = mutableListOf()
    private var playersInGame: MutableList<Player> = mutableListOf()
    private var playerWinners: MutableList<Player> = mutableListOf()
    var pokerResult: PokerResult? = null
    private set

    fun playRound() {
        prepareGame()
        preFlop()
        flop()
        turn()
        river()
    }

    private fun prepareGame() {
        pokerResult = null
        preparePlayers()
        prepareCards()
    }

    private fun prepareCards() {
        sharedCards = mutableListOf()
        freeCards = allCards
            .shuffled(randoms.random())
            .shuffled(randoms.random())
            .shuffled(randoms.random())
            .toMutableList()
    }

    private fun preparePlayers() {
        playerWinners.clear()
        playersInGame.clear()
        playersInGame.addAll(players)
    }

    private fun preFlop() {
        distributeCards(2)

    }

    private fun distributeCards(count: Int) {
        players.forEach {
            val playerCards = listener.onDistributeCards(it, count, freeCards)
            it.playerCards = playerCards
        }
    }

    private fun addSharedCards(count: Int) {
        val newSharedCards = freeCards.takeLast(count)
        freeCards.removeAll(newSharedCards)
        sharedCards.addAll(newSharedCards)
    }

    private fun flop() {
        addSharedCards(3)

    }

    private fun turn() {
        addSharedCards(1)

    }

    private fun river() {
        addSharedCards(1)
        showdown()
    }

    private fun showdown() {
        players.forEach {
            it.preparePokerHand(sharedCards)
        }
        prepareResult()
    }

    private fun prepareResult() {
        val playersResult: MutableMap<Player, PokerHand> = mutableMapOf()
        players.forEach {
            it.preparePokerHand(sharedCards)
            it.pokerHand?.let{ pokerHand ->
                playersResult[it] = pokerHand
            }
        }
        val playersPokerHands = playersResult.map {
            it.value
        }.sorted()
        val bestHand = playersPokerHands.last()
        val winnersResult = playersResult.filter {
            it.value.compareTo(bestHand) == 0
        }
        val winners = winnersResult.map { it.key }
        playerWinners.addAll(winners)
        val losers = players.filter {
            !playerWinners.contains(it)
        }
        val pokerResult = PokerResult(playerWinners, losers)
        this.pokerResult = pokerResult
        listener.onResult(pokerResult)

        System.out.println("shared ${sharedCards.joinToString(" ")}")
        players.forEach {
            val win = winners.contains(it)
            val hand = playersResult[it]
            System.out.println("player ${it.name} ${if (win) "win" else "lose"} hand ${hand}")
        }
    }

    interface Listener {
        fun onDistributeCards(player: Player, count: Int, freeCards: MutableList<Card>): List<Card>
        fun onResult(result: PokerResult)
    }
}