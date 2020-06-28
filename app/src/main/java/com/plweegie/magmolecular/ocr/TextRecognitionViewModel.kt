package com.plweegie.magmolecular.ocr

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.plweegie.magmolecular.ocr.TextRecognitionFragment.Companion.DESIRED_HEIGHT_CROP_PERCENT
import com.plweegie.magmolecular.ocr.TextRecognitionFragment.Companion.DESIRED_WIDTH_CROP_PERCENT
import com.plweegie.magmolecular.utils.SmoothedMutableLiveData


class TextRecognitionViewModel @ViewModelInject constructor(
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
}