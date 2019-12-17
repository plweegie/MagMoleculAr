package com.plweegie.magmolecular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.plweegie.magmolecular.ar.MagMolActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_atoms_btn?.setOnClickListener {
            val intent = MagMolActivity.newIntent(this, smiles_field?.text.toString())
            startActivity(intent)
        }
    }
}
