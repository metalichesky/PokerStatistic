package com.metalichesky.poker.model

enum class CardSuit(val symbol: String, val suitName: String, val suitColor: SuitColor, val rank: Int) {
    SPADES("♠", "Spades", SuitColor.BLACK, 1),
    CLUBS("♣", "Clubs", SuitColor.BLACK, 3),
    HEARTS("♥", "Hearts", SuitColor.RED, 2),
    DIAMONDS("♦", "Diamonds", SuitColor.RED, 4)
}

enum class SuitColor(val colorName: String) {
    BLACK("Black"),
    RED("Red")
}