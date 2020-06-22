package com.plweegie.magmolecular.di

import com.plweegie.magmolecular.nameresolver.ChemicalNameResolver
import com.plweegie.magmolecular.nameresolver.ResolverApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object CoreModule {

    private const val BASE_URL = "https://cactus.nci.nih.gov/chemical/"

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    @Provides
    fun provideAPI(retrofit: Retrofit): ResolverApi =
        retrofit.create(ResolverApi::class.java)

    @Provides
    fun provideChemicalNameResolver(resolverApi: ResolverApi) =
        ChemicalNameResolver(resolverApi)
}