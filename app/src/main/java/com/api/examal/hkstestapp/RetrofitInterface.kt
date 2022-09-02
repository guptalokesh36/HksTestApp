package com.api.examal.hkstestapp

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface {

    @get:GET("sort")
    val sort: Call<List<PostModel?>?>

    companion object {
        const val Base_url = ""
    }

}