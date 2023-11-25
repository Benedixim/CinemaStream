package com.example.cinemastream.model

data class Film(
    val filmId: Int,
    val nameRu: String,
    val nameEn: String?,
    val year: String,
    val filmLength: String,
    val countries: List<Country>,
    val genres: List<Genre>,
    val rating: String,
    val ratingVoteCount: Int,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingChange: String?,
    val isRatingUp: String?,
    val isAfisha: Int
)

data class Country(
    val country: String
)

data class Genre(val genre: String) {
    override fun toString(): String {
        return genre
    }
}

