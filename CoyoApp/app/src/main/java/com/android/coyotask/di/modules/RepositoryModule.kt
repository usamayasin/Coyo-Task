package com.android.coyotask.di.modules

import android.app.Application
import com.android.coyotask.data.remote.ApiService
import com.android.coyotask.data.repository.Repository
import com.android.coyotask.data.repository.RepositoryImpl
import com.android.coyotask.utils.StringUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The Dagger Module for providing repository instances.
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideStringUtils(app: Application): StringUtils {
        return StringUtils(app)
    }

    @Singleton
    @Provides
    fun provideImagineRepository(stringUtils: StringUtils, apiService: ApiService): Repository {
        return RepositoryImpl(stringUtils, apiService)
    }
}
