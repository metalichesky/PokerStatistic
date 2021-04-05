package com.metalichesky.poker.model

import java.lang.StringBuilder

class PokerStatistic {
    var playersWins: MutableMap<Player, Int> = mutableMapOf()
    var playersLoses: MutableMap<Player, Int> = mutableMapOf()
    var startCardsWinsProbability: MutableMap<String, Double> = mutableMapOf()
    var startCardsLosesProbability: MutableMap<String, Double> = mutableMapOf()
    var handsFrequency: MutableMap<String, Int> = mutableMapOf()
    var gamesNumber: Int = 0

    fun addResult(result: PokerResult) {
        result.winners.forEach {
            playersWins[it] = playersWins.getOrDefault(it, 0) + 1
            it.pokerHand?.let { pokerHand ->
                handsFrequency[pokerHand.name] = handsFrequency.getOrDefault(pokerHand.name, 0) + 1
            }
        }
        result.losers.forEach {
            playersLoses[it] = playersLoses.getOrDefault(it, 0) + 1
            it.pokerHand?.let { pokerHand ->
                handsFrequency[pokerHand.name] = handsFrequency.getOrDefault(pokerHand.name, 0) + 1
            }
        }
        gamesNumber++
    }

    fun clearResults() {
        playersLoses.clear()
        playersWins.clear()
        handsFrequency.clear()
        gamesNumber = 0
    }

    fun getWins(player: Player): Int {
        return playersWins.getOrDefault(player, 0)
    }

    fun getLoses(player: Player): Int {
        return playersLoses.getOrDefault(player, 0)
    }

    fun getWinProbability(player: Player): Double {
        val wins = playersWins.getOrDefault(player, 0)
        val loses = playersLoses.getOrDefault(player, 0)
        return wins.toDouble() / (wins + loses)
    }

    fun getLoseProbability(player: Player): Double {
        val wins = playersWins.getOrDefault(player, 0)
        val loses = playersLoses.getOrDefault(player, 0)
        return loses.toDouble() / (wins + loses)
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        playersWins.forEach {
            stringBuilder.append("${it.key.name} wins ${it.value}\n")
        }
        playersLoses.forEach {
            stringBuilder.append("${it.key.name} loses ${it.value}\n")
        }
        handsFrequency.forEach {
            stringBuilder.append("combination ${it.key} frequency ${it.value}\n")
        }
        return stringBuilder.toString()
    }
}