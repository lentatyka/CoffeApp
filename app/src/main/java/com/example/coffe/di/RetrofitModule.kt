package com.example.coffe.di

import com.example.coffe.model.CoffeService
import com.example.coffe.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    fun getUrl():String{
        return Constants.BASE_URL
    }

    @Provides
    @Singleton
    fun provideRetrofit(url: String): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(url)
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