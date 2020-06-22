package com.plweegie.magmolecular.nameresolver

import retrofit2.http.GET
import retrofit2.http.Path


interface ResolverApi {

    @GET("structure/{name}/smiles")
    suspend fun getSmiles(@Path("name") name: String): String
}