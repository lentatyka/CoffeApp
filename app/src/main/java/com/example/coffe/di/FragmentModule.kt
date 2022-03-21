package com.example.coffe.di

import androidx.fragment.app.Fragment
import com.example.coffe.util.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Provides
    fun provideCallback(fragment: Fragment):LocationService.Callback{
        return  fragment as LocationService.Callback
    }
}