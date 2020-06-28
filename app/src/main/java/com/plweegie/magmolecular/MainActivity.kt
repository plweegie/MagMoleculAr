package com.plweegie.magmolecular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.plweegie.magmolecular.ar.MagMolActivity
import com.plweegie.magmolecular.ocr.TextRecognitionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_atoms_btn?.setOnClickListener {
            viewModel.getSmilesForName(smiles_field?.text.toString())
        }

        get_atoms_from_camera_btn?.setOnClickListener {
            Intent(this, TextRecognitionActivity::class.java).also { startActivity(it) }
        }

        viewModel.smiles.observe(this, Observer { smiles ->
            if (smiles.isNotEmpty()) {
                val intent = MagMolActivity.newIntent(this, smiles)
                startActivity(intent)
            }
        })
    }
}
