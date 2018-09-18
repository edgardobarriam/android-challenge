package io.github.edgardobarriam.techkandroidchallenge.server

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurApiService {

    @GET("tags") fun getDefaultTags(): Observable<TagsListResponse>

    @GET("gallery/t/{tag}/") fun getTagGalleries(@Path("tag") tag: String ): Observable<GallerySearchResponse>

    @GET("gallery/{imageId}/comments/") fun getGalleryComments(@Path("imageId") imageId: String ): Observable<GalleryCommentsResponse>

    // Singleton pattern
    companion object {
        private const val IMGUR_ACCESS_TOKEN = "Client-ID 4fc5fe45f24ef4a"

        fun getInstance(): ImgurApiService {

            // Add Authorization header to all requests
            val httpClient = OkHttpClient.Builder()
                    .addInterceptor {
                        val builder = it.request().newBuilder()
                        builder.addHeader("Authorization", IMGUR_ACCESS_TOKEN)
                        it.proceed(builder.build())
                    }
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/3/")
                    .client(httpClient)
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .build()

            return retrofit.create(ImgurApiService::class.java)
        }
    }
}