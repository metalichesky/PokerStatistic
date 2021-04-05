package com.metalichesky.poker.util

import com.metalichesky.poker.model.Card
import com.metalichesky.poker.model.CardName
import com.metalichesky.poker.model.CardSuit

class PokerHandCombinator(
    private val allCards: List<Card>,
    private val combinationSize: Int = 5
) {
    private val cardsIndices: Array<Int> = allCards.indices.toList().toTypedArray()
    var combinationsReceived: Long = 0
    private set
    val combinationsCount: Long
    get(): Long {
        return MathUtils.getCombinationCount(cardsIndices.size, combinationSize)
    }

// размещения
//    fun nextCombination(): List<Card>? {
//        var j = 0
//        do {
//            j = cardsIndices.size - 2
//            while (j != -1 && cardsIndices[j] >= cardsIndices[j + 1]) {
//                j--
//            }
//            if (j == -1) {
//                return null
//            } else if (combinationsReceived == 0L && j == cardsIndices.size - 2) {
//                return prepareSet()
//            }
//            var k = cardsIndices.size - 1
//            while (cardsIndices[j] >= cardsIndices[k]) {
//                k--
//            }
//            cardsIndices.swap(j, k)
//            var l = j + 1
//            var r = cardsIndices.size - 1
//            while (l < r) {
//                cardsIndices.swap(l++, r--)
//            }
//        } while (j > combinationSize - 1)
//        return prepareSet()
//    }

// сочетания
    fun nextCombination(): List<Card>? {
        val k = combinationSize
        if (combinationsReceived == 0L) {
            return prepareSet()
        }
        for (i in (k - 1) downTo 0) {
            if (cardsIndices[i] < (cardsIndices.size - k + i)) {
                ++cardsIndices[i]
                for (j in (i + 1) until k) {
                    cardsIndices[j] = cardsIndices[j - 1] + 1
                }
                return prepareSet()
            }
        }
        return null
    }

    private fun prepareSet(): List<Card> {
        val set = cardsIndices.map {
            allCards.getOrNull(it)
        }.filterNotNull()
        combinationsReceived++
        return if (set.size > combinationSize) {
            set.subList(0, combinationSize)
        } else {
            set
        }.sortedWith(Card.nameComparator)
    }
}

fun <T> Array<T>.swap(position1: Int, position2: Int) {
    val value1 = this[position1]
    try {
        this.set(position1, this[position2])
        this.set(position2, value1)
    } catch (ex: IndexOutOfBoundsException) {
        ex.printStackTrace()
    }
}

fun main() {
    val cards = listOf(
        Card(CardName.ACE, CardSuit.SPADES),
        Card(CardName.ACE, CardSuit.HEARTS),
        Card(CardName.ACE, CardSuit.CLUBS),
        Card(CardName.ACE, CardSuit.DIAMONDS),

        Card(CardName.KING, CardSuit.SPADES),
        Card(CardName.KING, CardSuit.HEARTS),
        Card(CardName.KING, CardSuit.CLUBS),
        Card(CardName.KING, CardSuit.DIAMONDS)
    )

    val combinator = PokerHandCombinator(cards, 4)
    var combination = combinator.nextCombination()
    var idx = 0
    val combinations = mutableListOf<List<Card>>()
    while(combination != null) {
//        idx++
//        System.out.println("$idx) ${combination.joinToString(" ")}")
        if (combination[0].name == CardName.ACE && combination[1].name == CardName.ACE && combination[2].name == CardName.KING && combination[3].name == CardName.KING) {
            idx++
            combinations.add(combination)
            System.out.println("$idx) ${combination.joinToString(" ")}")
        }
        combination = combinator.nextCombination()
    }
    System.out.println("combinations = ${combinator.combinationsReceived} of ${combinator.combinationsCount}")
}