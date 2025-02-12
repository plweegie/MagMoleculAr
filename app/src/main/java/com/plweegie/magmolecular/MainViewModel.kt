package com.plweegie.magmolecular

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.plweegie.magmolecular.nameresolver.ChemicalNameResolver
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val nameResolver: ChemicalNameResolver,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val smiles: LiveData<String>
        get() = _smiles

    private val _smiles = MutableLiveData<String>()

    fun getSmilesForName(name: String) {
        viewModelScope.launch {
            try {
                val result = nameResolver.resolveName(name)
                _smiles.postValue(result)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Wrong substance name", e)
            }
        }
    }
}