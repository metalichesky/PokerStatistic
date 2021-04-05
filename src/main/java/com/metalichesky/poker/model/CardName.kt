package com.metalichesky.poker.model

enum class CardName(val cardName: String, val shortCardName: String, val cardPoints: Int, val cardNumber: Int) {
    JOKER("Joker", "Joker", 17, 14),
    ACE("Ace", "A",16, 13),
    KING("King", "K", 14, 12),
    QUEEN("Queen", "Q", 13, 11),
    JACK("Jack", "J", 12, 10),
    TEN("10", "10", 11, 9),
    NINE("9", "9", 9, 8),
    EIGHT("8", "8", 8, 7),
    SEVEN("7", "7",7, 6),
    SIX("6","6", 6, 5),
    FIFE("5", "5", 5, 4),
    FOURTH("4", "4", 4, 3),
    THREE("3", "3", 3, 2),
    TWO("2", "2", 2, 1)
}