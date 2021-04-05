package com.metalichesky.poker.util

import java.util.stream.LongStream


object MathUtils {

    fun factorial(n: Int): Long {
        return LongStream.rangeClosed(1L, n.toLong())
            .reduce(1) { x: Long, y: Long -> x * y }
    }

    fun getCombinationCount(n: Int, m: Int): Long {
        return factorial(n) / (factorial(n - m) * factorial(m))
    }


    fun getPlacementCount(n: Int, m: Int): Long {
        return factorial(n) / factorial(n - m)
    }
}
