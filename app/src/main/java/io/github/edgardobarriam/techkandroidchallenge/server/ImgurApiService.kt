package io.github.edgardobarriam.techkandroidchallenge.server

import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ImgurApiService {


    @Headers("Authorization: Client-ID 4fc5fe45f24ef4a")
    @GET("tags") fun getDefaultTags(): Observable<TagsListResult>

    companion object {
        fun getInstance(): ImgurApiService {

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/3/")
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .build()

            return retrofit.create(ImgurApiService::class.java)
        }
    }
}