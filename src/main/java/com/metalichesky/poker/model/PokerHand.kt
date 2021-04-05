package com.metalichesky.poker.model

import com.metalichesky.poker.util.PokerHandCombinator
import kotlin.math.absoluteValue

abstract class PokerHand(
    val cards: List<Card>,
    val name: String,
    val rank: Int
) : Comparable<PokerHand> {
    companion object {
        fun findAllPossible(cards: List<Card>): List<PokerHand> {
            val combinator = PokerHandCombinator(cards)
            val pokerHands = mutableListOf<PokerHand>()
            var combination = combinator.nextCombination()
            while (combination != null) {
                findPokerHand(combination)?.let{ pokerHand ->
                    pokerHands.add(pokerHand)
                }
                combination = combinator.nextCombination()
            }
            return pokerHands
        }

        fun findPokerHand(combination: List<Card>): PokerHand? {
            val royalFlush = RoyalFlush(combination)
            if (royalFlush.isValid()) {
                return royalFlush
            }
            val straightFlush = StraightFlush(combination)
            if (straightFlush.isValid()) {
                return straightFlush
            }
            val fourOfKind = FourOfAKind(combination)
            if (fourOfKind.isValid()) {
                return fourOfKind
            }
            val fullHouse = FullHouse(combination)
            if (fullHouse.isValid()) {
                return fullHouse
            }
            val flush = Flush(combination)
            if (flush.isValid()) {
                return flush
            }
            val straight = Straight(combination)
            if (straight.isValid()) {
                return straight
            }
            val threeOfAKind = ThreeOfAKind(combination)
            if (threeOfAKind.isValid()) {
                return threeOfAKind
            }
            val twoPair = TwoPair(combination)
            if (twoPair.isValid()) {
                return twoPair
            }
            val pair = Pair(combination)
            if (pair.isValid()) {
                return pair
            }
            val highCard = HighCard(combination)
            if (highCard.isValid()) {
                return highCard
            }
            return null
        }
    }

    abstract val usedCards: List<Card>
    val notUsedCards: List<Card>
        get() {
            return cards.filterNot { usedCards.contains(it) }
        }

    fun getHighestUsedCard(): Card? {
        return usedCards.maxBy {
            it.name.cardPoints
        }
    }

    fun getHighestNotUsedCard(): Card? {
        return notUsedCards.maxBy {
            it.name.cardPoints
        }
    }

    fun getHighestAllCard(): Card? {
        return cards.maxBy {
            it.name.cardPoints
        }
    }

    fun getUsedCardsPoints(): Int {
        return usedCards.sumBy {
            it.name.cardPoints
        }
    }

    fun getNonUsedCardsPoints(): Int {
        return notUsedCards.sumBy {
            it.name.cardPoints
        }
    }

    fun getAllCardsPoints(): Int {
        return cards.sumBy {
            it.name.cardPoints
        }
    }

    open override fun compareTo(other: PokerHand): Int {
        return if (other.rank != this.rank) {
            other.rank - this.rank
        } else {
            // equals rank, select winner by high
            val highCardPoints = this.getHighestUsedCard()?.name?.cardPoints ?: 0
            val otherHighCardPoints = other.getHighestUsedCard()?.name?.cardPoints ?: 0
            return if (highCardPoints != otherHighCardPoints) {
                highCardPoints - otherHighCardPoints
            } else {
                0
            }
        }
    }

    abstract fun findUsedCards(): List<Card>

    open fun isValid(): Boolean {
        return cards.size == 5
    }

    override fun toString(): String {
        return "$name: ${cards.joinToString(" ")}"
    }
}

// The best hand possible, a royal flush consists of A, K, Q, J and 10, all of the same suit.
class RoyalFlush(cards: List<Card>) : PokerHand(cards, "Royal Flush", 1) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        if (cards.find { it.name == CardName.ACE } == null) {
            return emptyList()
        }
        for (cardIdx in 0 until (cards.size - 1)) {
            val nextCardIdx = cardIdx + 1
            val numberDiff = (cards[cardIdx].name.cardNumber - cards[nextCardIdx].name.cardNumber).absoluteValue
            if (numberDiff > 1) {
                return emptyList()
            }
        }
        val suit = cards.first().suit
        for (card in cards) {
            if (card.suit != suit) {
                return emptyList()
            }
        }
        return cards
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }
}

// Also very rare, a straight flush consists of any straight that is all the same suit.
class StraightFlush(cards: List<Card>) : PokerHand(cards, "Straight Flush", 2) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        for (cardIdx in 0 until (cards.size - 1)) {
            val nextCardIdx = cardIdx + 1
            val numberDiff = (cards[cardIdx].name.cardNumber - cards[nextCardIdx].name.cardNumber).absoluteValue
            if (numberDiff != 1) {
                return emptyList()
            }
        }
        val suit = cards.first().suit
        for (card in cards) {
            if (card.suit != suit) {
                return emptyList()
            }
        }
        return cards
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }
}

// Four of a kind, or 'quads', consists of four cards of equal value along with another card known as a side card.
class FourOfAKind(cards: List<Card>) : PokerHand(cards, "Four of a Kind", 3) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        val names = cards
            .map { it.name }
            .distinct()
        if (names.size > 2) {
            return emptyList()
        }
        val cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()
        for (card in cards) {
            cardsPerNames[card.name] = cardsPerNames.getOrDefault(card.name, 0) + 1
        }
        val maxCards = cardsPerNames.maxBy { it.value }
        val maxCardsPerNames = maxCards?.value ?: 0
        val maxCardsName = maxCards?.key
        return if (maxCardsPerNames == 4 && maxCardsName != null) {
            cards.filter {
                it.name == maxCardsName
            }
        } else {
            emptyList()
        }
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }

    override fun compareTo(other: PokerHand): Int {
        var result = super.compareTo(other)
        return if (result != 0) {
            result
        } else {
            // equals rank, equals high, select winner by kicker
            val highCardPoints = this.getHighestNotUsedCard()?.name?.cardPoints ?: 0
            val otherHighCardPoints = other.getHighestNotUsedCard()?.name?.cardPoints ?: 0
            return highCardPoints - otherHighCardPoints
        }
    }
}

// 	A full house consists of three cards of one value and two cards of another.
class FullHouse(cards: List<Card>) : PokerHand(cards, "Full House", 4) {
    private var cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        val names = cards.map { it.name }.distinct()
        if (names.size != 2) {
            return emptyList()
        }
        val cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()
        for (card in cards) {
            cardsPerNames[card.name] = cardsPerNames.getOrDefault(card.name, 0) + 1
        }
        this.cardsPerNames = cardsPerNames
        return if (cardsPerNames[names[0]] == 2 || cardsPerNames[names[0]] == 3) {
            cards
        } else {
            emptyList()
        }
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }

    override fun compareTo(other: PokerHand): Int {
        var result = super.compareTo(other)
        return if (result != 0) {
            result
        } else {
            // equals rank, equals high, select winner by pair high
            val pairCardName = cardsPerNames.filter { it.value == 2 }.keys.firstOrNull()
            val otherPairCardName = (other as? FullHouse)?.cardsPerNames?.filter { it.value == 2 }?.keys?.firstOrNull()
            result = if (pairCardName != null && otherPairCardName != null) {
                pairCardName.cardNumber - otherPairCardName.cardNumber
            } else {
                0
            }
            if (result != 0) {
                result
            } else {
                // equals rank, equals high, equals pair, select winner by three high
                val threeCardName = cardsPerNames.filter { it.value == 3 }.keys.firstOrNull()
                val otherThreeCardName = (other as? FullHouse)?.cardsPerNames?.filter { it.value == 3 }?.keys?.firstOrNull()
                result = if (threeCardName != null && otherThreeCardName != null) {
                    threeCardName.cardNumber - otherThreeCardName.cardNumber
                } else {
                    0
                }
                result
            }
        }
    }
}

// A flush is a hand which has all cards of the same suit.
class Flush(cards: List<Card>) : PokerHand(cards, "Flush", 5) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        val suit = cards.first().suit
        for (card in cards) {
            if (card.suit != suit) {
                return emptyList()
            }
        }
        return cards
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }
}

// A straight has 5 cards of consecutive value that are not all the same suit.
class Straight(cards: List<Card>) : PokerHand(cards, "Straight", 6) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        for (cardIdx in 0 until (cards.size - 1)) {
            val nextCardIdx = cardIdx + 1
            val numberDiff = (cards[cardIdx].name.cardNumber - cards[nextCardIdx].name.cardNumber).absoluteValue
            if (numberDiff != 1) {
                return emptyList()
            }
        }
        return cards
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }
}

// Also known as 'trips', three of a kind is 3 cards of the same value and 2 side cards of different values.
class ThreeOfAKind(cards: List<Card>) : PokerHand(cards, "Three Of A Kind", 7) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        val names = cards.map { it.name }.distinct()
        if (names.size > 3) {
            return emptyList()
        }
        val cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()
        for (card in cards) {
            cardsPerNames[card.name] = cardsPerNames.getOrDefault(card.name, 0) + 1
        }
        val cardNames = cardsPerNames.filter {
            it.value == 3
        }.keys
        return if (cardNames.size == 1) {
            cards.filter { cardNames.contains(it.name) }
        } else {
            emptyList()
        }
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }

    override fun compareTo(other: PokerHand): Int {
        var result = super.compareTo(other)
        return if (result != 0) {
            result
        } else {
            // equals rank, equals high, select winner by kicker
            val highCardPoints = this.getHighestNotUsedCard()?.name?.cardPoints ?: 0
            val otherHighCardPoints = other.getHighestNotUsedCard()?.name?.cardPoints ?: 0
            return highCardPoints - otherHighCardPoints
        }
    }
}

// Two pair consists of two cards of equal value, another two cards of equal value, and one extra card.
class TwoPair(cards: List<Card>) : PokerHand(cards, "Two Pair", 8) {
    private var cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()
    private var pairCardNames: List<CardName> = emptyList()

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        val names = cards.map { it.name }.distinct()
        if (names.size > 3) {
            return emptyList()
        }
        val cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()
        for (card in cards) {
            cardsPerNames[card.name] = cardsPerNames.getOrDefault(card.name, 0) + 1
        }
        this.cardsPerNames = cardsPerNames
        pairCardNames = cardsPerNames.filter {
            it.value == 2
        }.map { it.key }

        return if (pairCardNames.size == 2) {
            cards.filter { pairCardNames.contains(it.name) }
        } else {
            emptyList()
        }
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }

    override fun compareTo(other: PokerHand): Int {
        var result = super.compareTo(other)
        return if (result != 0) {
            result
        } else {
            // equals rank, equals high, select winner by pairs high
            other as TwoPair
            val firstCardNumber = pairCardNames.getOrNull(0)?.cardNumber ?: 0
            val otherFirstCardNumber = other.pairCardNames.getOrNull(0)?.cardNumber ?: 0

            val secondCardNumber = pairCardNames.getOrNull(1)?.cardNumber ?: 0
            val otherSecondCardNumber = other.pairCardNames.getOrNull(1)?.cardNumber ?: 0

            if (firstCardNumber - otherFirstCardNumber != 0) {
                firstCardNumber - otherFirstCardNumber
            } else if (secondCardNumber - otherSecondCardNumber != 0) {
                secondCardNumber - otherSecondCardNumber
            } else {
                // equals rank, equals pairs, select winner by kicker
                val highCardPoints = this.getHighestNotUsedCard()?.name?.cardPoints ?: 0
                val otherHighCardPoints = other.getHighestNotUsedCard()?.name?.cardPoints ?: 0
                highCardPoints - otherHighCardPoints
            }
        }
    }
}

// One pair consists of two cards of the same value, and three extra cards.
class Pair(cards: List<Card>) : PokerHand(cards, "Pair", 9) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        if (!super.isValid()) return emptyList()
        val cardsPerNames: MutableMap<CardName, Int> = mutableMapOf()
        for (card in cards) {
            cardsPerNames[card.name] = cardsPerNames.getOrDefault(card.name, 0) + 1
        }
        val cardNames = cardsPerNames.filter {
            it.value == 2
        }.map { it.key }
        return if (cardNames.size == 1) {
            cards.filter { cardNames.contains(it.name) }
        } else {
            emptyList()
        }
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }

    override fun compareTo(other: PokerHand): Int {
        var result = super.compareTo(other)
        return if (result != 0) {
            result
        } else {
            // equals rank, equals high, select winner by kicker
            val highCardPoints = this.getHighestNotUsedCard()?.name?.cardPoints ?: 0
            val otherHighCardPoints = other.getHighestNotUsedCard()?.name?.cardPoints ?: 0
            return highCardPoints - otherHighCardPoints
        }
    }
}

// High card is when you have five cards that do not interact with each other to make any of the above hands.
class HighCard(cards: List<Card>) : PokerHand(cards, "High Card", 10) {

    override val usedCards: List<Card> = findUsedCards()

    override fun findUsedCards(): List<Card> {
        return if (cards.size >= 5) {
            cards.sortedBy { it.name.cardNumber }.takeLast(5)
        } else {
            cards
        }
    }

    override fun isValid(): Boolean {
        return super.isValid() && usedCards.isNotEmpty()
    }

    override fun compareTo(other: PokerHand): Int {
        var result = super.compareTo(other)
        return if (result != 0) {
            result
        } else {
            val usedCards = usedCards.sortedByDescending{ it.name }
            val otherUsedCards = other.usedCards.sortedByDescending { it.name }
            for (cardIdx in usedCards.indices) {
                val usedCardNumber = usedCards.getOrNull(cardIdx)?.name?.cardNumber ?: 0
                val otherUsedCardNumber = otherUsedCards.getOrNull(cardIdx)?.name?.cardNumber ?: 0
                if (usedCardNumber != otherUsedCardNumber) {
                    result = usedCardNumber - otherUsedCardNumber
                    break
                }
            }
            result
        }
    }
}