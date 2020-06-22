package com.plweegie.magmolecular.nameresolver


class ChemicalNameResolver(
    private val resolverApi: ResolverApi
) {

    suspend fun resolveName(name: String) = resolverApi.getSmiles(name)
}