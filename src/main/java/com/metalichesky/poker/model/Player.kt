package com.metalichesky.poker.model

open class Player (
    val name: String
) {
    var playerCards: List<Card> = emptyList()
    var pokerHand: PokerHand? = null

    fun getPokerHands(sharedCards: List<Card>): List<PokerHand> {
        val allCards = sharedCards + playerCards
        return PokerHand.findAllPossible(allCards)
    }

    fun findBestHand(sharedCards: List<Card>): PokerHand {
        val pokerHands = getPokerHands(sharedCards)
        val bestPokerHand = pokerHands.max()
        return bestPokerHand ?: pokerHands.first()
    }

    fun preparePokerHand(sharedCards: List<Card>) {
        pokerHand = findBestHand(sharedCards)
    }

}