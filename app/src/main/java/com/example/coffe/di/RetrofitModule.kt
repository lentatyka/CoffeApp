package com.example.coffe.di

import android.app.Application
import com.example.coffe.model.CoffeService
import com.example.coffe.model.TokenInterceptor
import com.example.coffe.util.Constants
import com.example.coffe.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideSessionManager(app: Application):SessionManager{
        return SessionManager(app)
    }

    @Provides
    @Singleton
    fun provideInterceptor(sessionManager: SessionManager): Interceptor {
        return TokenInterceptor(sessionManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor):OkHttpClient{
        return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    }

    @Provides
    fun getUrl():String{
        return Constants.BASE_URL
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, url: String): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    @Singleton
    fun provideCoffeService(retrofit: Retrofit.Builder): CoffeService {
        return retrofit
            .build()
            .create(CoffeService::class.java)
    }
}