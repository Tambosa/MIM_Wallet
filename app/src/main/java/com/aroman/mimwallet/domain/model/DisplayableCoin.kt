package com.aroman.mimwallet.domain.model

data class DisplayableCoin(
    val id: Int,
    val name: String,
    val symbol: String,
    var count: Double = 0.0,
    val price: Double = 0.0,
    val percentChange1h: Double = 0.0,
    val percentChange24h: Double = 0.0,
    val percentChange7d: Double = 0.0,
    val percentChange30d: Double = 0.0,
    val percentChange60d: Double = 0.0,
    val percentChange90d: Double = 0.0,
) : DisplayableItem {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            symbol,
            "$id"
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}