package com.plweegie.magmolecular.ocr

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.plweegie.magmolecular.R
import com.plweegie.magmolecular.ar.MagMolActivity


class TextRecognitionLoadingFragment : Fragment() {

    companion object {
        private const val SMILES_ARG = "smiles_arg"

        fun newInstance(smiles: String) = TextRecognitionLoadingFragment().apply {
            Bundle().apply { putString(SMILES_ARG, smiles) }.also {
                arguments = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_text_recognition_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (arguments?.get(SMILES_ARG) as String).let { smiles ->
            Handler().postDelayed({
                val intent = MagMolActivity.newIntent(activity as Context, smiles)
                startActivity(intent)
                activity?.finish()
            }, 2000)
        }
    }
}