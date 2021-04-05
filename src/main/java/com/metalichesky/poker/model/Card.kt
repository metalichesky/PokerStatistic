package com.metalichesky.poker.model

class Card(val name: CardName, val suit: CardSuit) : Comparable<Card> {
    companion object {
        var longStringDivider = "_"

        fun fromLongString(longString: String): List<Card> {
            val parts = longString.split(longStringDivider)
            var currentShortName = ""
            var currentSuit = ""
            val cards = mutableListOf<Card>()
            for (partIdx in 0 until parts.size step 2) {
                currentShortName = parts.getOrNull(partIdx) ?: ""
                currentSuit = parts.getOrNull(partIdx + 1) ?: ""
                if (currentShortName.isNotEmpty() && currentSuit.isNotEmpty()) {
                    val cardName = CardName.values().find { it.shortCardName == currentShortName }
                    val cardSuit = CardSuit.values().find { it.suitName == currentSuit }
                    if (cardName != null && cardSuit != null) {
                        cards.add(Card(cardName, cardSuit))
                    }
                }
            }
            return cards
        }

        fun toLongString(cards: List<Card>): String {
            return cards.joinToString(separator = longStringDivider) { it.toLongString() }
        }

        val nameComparator = object : Comparator<Card> {
            override fun compare(card1: Card?, card2: Card?): Int {
                return if (card1 != null && card2 != null) {
                    card1.name.cardNumber - card2.name.cardNumber
                } else {
                    0
                }
            }
        }

        val suitComparator = object: Comparator<Card> {
            override fun compare(card1: Card?, card2: Card?): Int {
                return if (card1 != null && card2 != null) {
                    card2.suit.rank - card1.suit.rank
                } else {
                    0
                }
            }
        }

        val strongComparator = object : Comparator<Card> {
            override fun compare(card1: Card?, card2: Card?): Int {
                return if (card1 != null && card2 != null) {
                    val byCardNumber = card1.name.cardNumber - card2.name.cardNumber
                    return if (byCardNumber != 0) {
                        byCardNumber
                    } else {
                        card2.suit.rank - card1.suit.rank
                    }
                } else {
                    0
                }
            }
        }
        val strongListComparator = object : Comparator<List<Card>> {
            override fun compare(list1: List<Card>?, list2: List<Card>?): Int {
                return if (list1 != null && list2 != null) {
                    if (list1.size != list2.size) {
                        list1.size - list2.size
                    } else if (list2.isEmpty() && list1.isEmpty()){
                        0
                    } else {
                        val list1Sorted = list1.sortedWith(nameComparator).reversed()
                        val list2Sorted = list2.sortedWith(nameComparator).reversed()
                        var result = 0
                        for (i in list1Sorted.indices) {
                            val compareResult = nameComparator.compare(list1Sorted.get(i), list2Sorted.get(i))
                            if (compareResult != 0) {
                                result = compareResult
                                break
                            }
                        }
                        if (result != 0) {
                            result
                        } else {
                            for (i in list1Sorted.indices) {
                                val compareResult = suitComparator.compare(list1Sorted.get(i), list2Sorted.get(i))
                                if (compareResult != 0) {
                                    result = compareResult
                                    break
                                }
                            }
                            result
                        }
                    }
                } else {
                    0
                }
            }
        }
    }

    fun toLongString(): String {
        return "${name.shortCardName}${longStringDivider}${suit.suitName}"
    }

    override fun toString(): String {
        return "${name.shortCardName}${suit.symbol}"
    }

    override fun compareTo(other: Card): Int {
        return nameComparator.compare(this, other)
    }

    fun isSameCard(card: Card): Boolean {
        return card.name == this.name && card.suit == this.suit
    }

}

