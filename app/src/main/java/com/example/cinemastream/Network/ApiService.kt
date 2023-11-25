package com.example.cinemastream.Network

import com.example.cinemastream.model.Film
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.net.SocketTimeoutException

fun getPopularMovies(apiKey: String): List<Film> {
    try {
        val client = OkHttpClient()
        val url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS&page=1"

        val request = Request.Builder()
            .url(url)
            .header("X-API-KEY", apiKey)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            val apiResponse = Gson().fromJson(responseBody, ApiResponse::class.java)
            return apiResponse.films
        } else {
            throw Exception("Failed to fetch popular movies")
        }
    } catch (e: SocketTimeoutException) {
        // Обработка ошибки при отсутствии подключения к интернету
        e.printStackTrace()
        // Вернуть пустой список фильмов
        return emptyList()
    } catch (e: Exception) {
        // Обработка других исключений
        e.printStackTrace()
        throw e
    }
}

data class ApiResponse(
    val films: List<Film>
)

fun searchMovie(apiKey: String, movieName: String): List<Film> {
    try {
        val client = OkHttpClient()
        val url = "https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword?keyword=$movieName"

        val request = Request.Builder()
            .url(url)
            .header("X-API-KEY", apiKey)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            val apiResponse = Gson().fromJson(responseBody, ApiResponse::class.java)
            return apiResponse.films
        } else {
            throw Exception("Failed to fetch movies")
        }
    } catch (e: SocketTimeoutException) {
        // Обработка ошибки при отсутствии подключения к интернету
        e.printStackTrace()
        // Вернуть пустой список фильмов
        return emptyList()
    } catch (e: Exception) {
        // Обработка других исключений
        e.printStackTrace()
        throw e
    }
}

