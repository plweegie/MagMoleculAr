package com.plweegie.magmolecular.ocr

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.plweegie.magmolecular.nameresolver.ChemicalNameResolver
import com.plweegie.magmolecular.ocr.TextRecognitionFragment.Companion.DESIRED_HEIGHT_CROP_PERCENT
import com.plweegie.magmolecular.ocr.TextRecognitionFragment.Companion.DESIRED_WIDTH_CROP_PERCENT
import com.plweegie.magmolecular.utils.SmoothedMutableLiveData
import kotlinx.coroutines.launch


class TextRecognitionViewModel @ViewModelInject constructor(
    private val nameResolver: ChemicalNameResolver,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        // Amount of time (in milliseconds) to wait for detected text to settle
        const val SMOOTHING_DURATION = 50L
    }

    // We set desired crop percentages to avoid having to analyze the whole image from the live
    // camera feed. However, we are not guaranteed what aspect ratio we will get from the camera, so
    // we use the first frame we get back from the camera to update these crop percentages based on
    // the actual aspect ratio of images.
    val imageCropPercentages = MutableLiveData<Pair<Int, Int>>()
        .apply { value = Pair(DESIRED_HEIGHT_CROP_PERCENT, DESIRED_WIDTH_CROP_PERCENT) }

    val sourceText = SmoothedMutableLiveData<String>(SMOOTHING_DURATION)

    val smiles: LiveData<String>
        get() = _smiles

    private val _smiles = MutableLiveData<String>()

    fun getSmilesForCameraName(name: String) {
        viewModelScope.launch {
            try {
                val result = nameResolver.resolveName(name)
                _smiles.postValue(result)
            } catch (e: Exception) {
                Log.e("TextRecognitionViewModel", "Wrong substance name", e)
            }
        }
    }
}