package com.plweegie.magmolecular.ocr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.plweegie.magmolecular.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TextRecognitionActivity : AppCompatActivity(), TextRecognitionFragment.FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TextRecognitionFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResultReceived(smiles: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TextRecognitionLoadingFragment.newInstance(smiles))
            .commitNow()
    }
}