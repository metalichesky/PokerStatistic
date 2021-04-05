package com.metalichesky.poker.repo

import com.metalichesky.poker.model.Card
import com.metalichesky.poker.model.CardName
import com.metalichesky.poker.model.CardSuit

object CardsRepo {

    fun getAllCards(): List<Card> {
        return listOf(
            Card(CardName.ACE, CardSuit.SPADES),
            Card(CardName.KING, CardSuit.SPADES),
            Card(CardName.QUEEN, CardSuit.SPADES),
            Card(CardName.JACK, CardSuit.SPADES),
            Card(CardName.TEN, CardSuit.SPADES),
            Card(CardName.NINE, CardSuit.SPADES),
            Card(CardName.EIGHT, CardSuit.SPADES),
            Card(CardName.SEVEN, CardSuit.SPADES),
            Card(CardName.SIX, CardSuit.SPADES),
            Card(CardName.FIFE, CardSuit.SPADES),
            Card(CardName.FOURTH, CardSuit.SPADES),
            Card(CardName.THREE, CardSuit.SPADES),
            Card(CardName.TWO, CardSuit.SPADES),
            Card(CardName.ACE, CardSuit.HEARTS),
            Card(CardName.KING, CardSuit.HEARTS),
            Card(CardName.QUEEN, CardSuit.HEARTS),
            Card(CardName.JACK, CardSuit.HEARTS),
            Card(CardName.TEN, CardSuit.HEARTS),
            Card(CardName.NINE, CardSuit.HEARTS),
            Card(CardName.EIGHT, CardSuit.HEARTS),
            Card(CardName.SEVEN, CardSuit.HEARTS),
            Card(CardName.SIX, CardSuit.HEARTS),
            Card(CardName.FIFE, CardSuit.HEARTS),
            Card(CardName.FOURTH, CardSuit.HEARTS),
            Card(CardName.THREE, CardSuit.HEARTS),
            Card(CardName.TWO, CardSuit.HEARTS),
            Card(CardName.ACE, CardSuit.CLUBS),
            Card(CardName.KING, CardSuit.CLUBS),
            Card(CardName.QUEEN, CardSuit.CLUBS),
            Card(CardName.JACK, CardSuit.CLUBS),
            Card(CardName.TEN, CardSuit.CLUBS),
            Card(CardName.NINE, CardSuit.CLUBS),
            Card(CardName.EIGHT, CardSuit.CLUBS),
            Card(CardName.SEVEN, CardSuit.CLUBS),
            Card(CardName.SIX, CardSuit.CLUBS),
            Card(CardName.FIFE, CardSuit.CLUBS),
            Card(CardName.FOURTH, CardSuit.CLUBS),
            Card(CardName.THREE, CardSuit.CLUBS),
            Card(CardName.TWO, CardSuit.CLUBS),
            Card(CardName.ACE, CardSuit.DIAMONDS),
            Card(CardName.KING, CardSuit.DIAMONDS),
            Card(CardName.QUEEN, CardSuit.DIAMONDS),
            Card(CardName.JACK, CardSuit.DIAMONDS),
            Card(CardName.TEN, CardSuit.DIAMONDS),
            Card(CardName.NINE, CardSuit.DIAMONDS),
            Card(CardName.EIGHT, CardSuit.DIAMONDS),
            Card(CardName.SEVEN, CardSuit.DIAMONDS),
            Card(CardName.SIX, CardSuit.DIAMONDS),
            Card(CardName.FIFE, CardSuit.DIAMONDS),
            Card(CardName.FOURTH, CardSuit.DIAMONDS),
            Card(CardName.THREE, CardSuit.DIAMONDS),
            Card(CardName.TWO, CardSuit.DIAMONDS)
        )
    }

    fun getAllCardNames(): List<CardName> {
        return listOf(
            CardName.TWO,
            CardName.THREE,
            CardName.FOURTH,
            CardName.FIFE,
            CardName.SIX,
            CardName.SEVEN,
            CardName.EIGHT,
            CardName.NINE,
            CardName.TEN,
            CardName.JACK,
            CardName.QUEEN,
            CardName.KING,
            CardName.ACE
        )
    }

    fun getAllCardSuits(): List<CardSuit> {
        return listOf(
            CardSuit.DIAMONDS,
            CardSuit.CLUBS,
            CardSuit.HEARTS,
            CardSuit.SPADES
        )
    }

}