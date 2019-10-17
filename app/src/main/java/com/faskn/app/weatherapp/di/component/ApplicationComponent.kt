package com.faskn.app.weatherapp.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.faskn.app.weatherapp.WeatherApp
import com.faskn.app.weatherapp.di.module.*
import com.faskn.app.weatherapp.ui.main.MainActivityViewModel

import dagger.BindsInstance

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            ApplicationModule::class,
            NetModule::class,
            DatabaseModule::class,
            ActivityModule::class,
            ViewModelModule::class
        ]
)

interface ApplicationComponent : AndroidInjector<WeatherApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationBind(application: Application): Builder

        fun build(): ApplicationComponent
    }
}
