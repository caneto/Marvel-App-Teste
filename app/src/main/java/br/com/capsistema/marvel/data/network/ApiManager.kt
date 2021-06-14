package br.com.capsistema.marvel.data.network

import br.com.capsistema.marvel.BuildConfig
import br.com.capsistema.marvel.data.network.model.CharactersResponse
import br.com.capsistema.marvel.utils.md5
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * The class responsible to make the Marvel Api requests.
 */
class ApiManager: ApiService {

    private val retrofit by lazy {
        createRetrofit()
    }

    val marvelService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /**
     * Create the retrofit instance
     */
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getOkHttpClient().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(getGsonConverterFactory())
                .build()
    }

    /**
     * The OkHttp Client with all interceptors attached.
     */
    private fun getOkHttpClient(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()

        client.addInterceptor({ chain: Interceptor.Chain ->

            val ts = System.currentTimeMillis().toString()
            val hash = "$ts${BuildConfig.PRIVATE_API_KEY}${BuildConfig.PUBLIC_API_KEY}".md5()

            var request = chain.request()
            val url = request.url.newBuilder().addQueryParameter(Parameters.TS, ts)
                    .addQueryParameter(Parameters.API_KEY, BuildConfig.PUBLIC_API_KEY)
                    .addQueryParameter(Parameters.HASH, hash).build()

            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        })

        client.addInterceptor(getHttpLoggingInterceptor())

        return client
    }

    /**
     * Http Logging Interceptor
     */
    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * Creates the json converter class
     */
    private fun getGsonConverterFactory(): retrofit2.Converter.Factory {
        val gson = GsonBuilder()
                .create()
        return GsonConverterFactory.create(gson)
    }


    fun asRetrofitException(throwable: Throwable): RetrofitException {
        // We had non-200 http error
        if (throwable is HttpException) {
            val response = throwable.response()
            if (response != null) {
                return RetrofitException.httpError(response.raw().request.url.toString(), response, retrofit)
            }
        }
        // A network error happened
        return if (throwable is IOException) {
            RetrofitException.networkError(throwable)
        } else RetrofitException.unexpectedError(throwable)

        // We don't know what happened. We need to simply convert to an unknown error

    }

    /**
     * The parameters to be used on all Marvel Service requests
     */
    object Parameters {
        val TS = "ts"
        val HASH = "hash"
        val API_KEY = "apikey"
    }


    /**
     * Marvel Api Requests
     */
    override fun getCharacters(limit: Int, offset: Int): Single<CharactersResponse> {
        return marvelService.getCharacters(limit, offset)
    }

}