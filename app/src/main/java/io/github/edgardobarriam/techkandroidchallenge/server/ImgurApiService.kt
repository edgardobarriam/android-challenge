package io.github.edgardobarriam.techkandroidchallenge.server

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ImgurApiService {

    @GET("tags") fun getDefaultTags(): Observable<TagsListResponse>

    @GET("gallery/t/{tag}/") fun getTagGalleries(@Path("tag") tag: String ): Observable<GallerySearchResponse>

    @GET("gallery/{imageId}/comments/") fun getGalleryComments(@Path("imageId") imageId: String ): Observable<GalleryCommentsResponse>

    @Multipart
    @POST("image") fun postImage(@Part image: MultipartBody.Part, @Part("title") title: RequestBody) : Observable<UploadResponse>

    // Singleton
    companion object {
        private const val IMGUR_ACCESS_TOKEN = "Client-ID 4fc5fe45f24ef4a"
        private const val API_URL = "https://api.imgur.com/3/"
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
                    .baseUrl(API_URL)
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