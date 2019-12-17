package com.plweegie.magmolecular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.plweegie.magmolecular.rendering.AndroidModelBuilder3D
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.openscience.cdk.DefaultChemObjectBuilder
import org.openscience.cdk.exception.InvalidSmilesException
import org.openscience.cdk.interfaces.IAtom
import org.openscience.cdk.interfaces.IAtomContainer
import org.openscience.cdk.smiles.SmilesParser
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var smilesParser: SmilesParser
    private lateinit var modelBuilder: AndroidModelBuilder3D
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        val builder = DefaultChemObjectBuilder.getInstance()
        smilesParser = SmilesParser(builder)
        modelBuilder = AndroidModelBuilder3D.getInstance(builder)

        get_atoms_btn?.setOnClickListener {
            parseSmiles()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun parseSmiles() {
        val smiles = smiles_field?.text.toString()
        try {
            val atomContainer = smilesParser.parseSmiles(smiles)

            launch {
                val atoms3d = get3DCoordinates(atomContainer)
                atoms3d.forEach {
                    Log.d("COORDS", "${it.point3d.x}, ${it.point3d.y}, ${it.point3d.z}")
                }
            }
        } catch (e: InvalidSmilesException) {
            Toast.makeText(this, "Invalid SMILES", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun get3DCoordinates(atomContainer: IAtomContainer): List<IAtom> {
        val atomContainer3D = withContext(Dispatchers.Default) {
            modelBuilder.generate3DCoordinates(atomContainer, false)
        }
        return atomContainer3D.atoms().toList()
    }
}
