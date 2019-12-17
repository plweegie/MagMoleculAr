package com.plweegie.magmolecular.ar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.plweegie.magmolecular.R
import com.plweegie.magmolecular.rendering.AndroidModelBuilder3D
import kotlinx.android.synthetic.main.activity_mag_mol.*
import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import org.openscience.cdk.DefaultChemObjectBuilder
import org.openscience.cdk.exception.InvalidSmilesException
import org.openscience.cdk.interfaces.IAtom
import org.openscience.cdk.interfaces.IAtomContainer
import org.openscience.cdk.smiles.SmilesParser


class MagMolActivity : AppCompatActivity() {

    companion object {
        private const val SMILES_EXTRA = "smiles_extra"

        fun newIntent(context: Context, smiles: String): Intent =
            Intent(context, MagMolActivity::class.java).apply {
                putExtra(SMILES_EXTRA, smiles)
            }
    }

    private lateinit var smilesParser: SmilesParser
    private lateinit var modelBuilder: AndroidModelBuilder3D
    private lateinit var arFragment: ArFragment

    private var smiles: String? = null
    private var sphereRenderable: ModelRenderable? = null

    private val renderableJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + renderableJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mag_mol)

        val builder = DefaultChemObjectBuilder.getInstance()
        smilesParser = SmilesParser(builder)
        modelBuilder = AndroidModelBuilder3D.getInstance(builder)

        smiles = intent.getStringExtra(SMILES_EXTRA)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        uiScope.launch {
            parseSmiles(smiles)
        }
    }

    private suspend fun parseSmiles(smiles: String?) {
        loading_pb?.visibility = View.VISIBLE

        sphereRenderable = getModelRenderable(this@MagMolActivity)

        sphereRenderable?.let {
            try {
                val atomContainer = smilesParser.parseSmiles(smiles)

                val atoms3d = get3DCoordinates(atomContainer)
                atoms3d.forEach { atom ->
                    with(atom.point3d) {
                        val coords = Vector3(x.toFloat(), y.toFloat(), z.toFloat())
                        addRenderable(coords)
                    }
                }

            } catch (e: InvalidSmilesException) {
                Toast.makeText(this, "Invalid SMILES", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addRenderable(position: Vector3) {
        loading_pb?.visibility = View.GONE

        val node = Node().apply {
            worldPosition = Vector3.subtract(position.scaled(0.05f), Vector3(0.0f, 0.2f, 0.5f))
            renderable = sphereRenderable
        }

        arFragment.arSceneView.scene.addChild(node)
    }

    private suspend fun get3DCoordinates(atomContainer: IAtomContainer): List<IAtom> {
        val atomContainer3D = withContext(Dispatchers.Default) {
            modelBuilder.generate3DCoordinates(atomContainer, false)
        }
        return atomContainer3D.atoms().toList()
    }

    private suspend fun getModelRenderable(context: Context): ModelRenderable {
        val material = MaterialFactory.makeOpaqueWithColor(context, Color(android.graphics.Color.BLACK)).await()
        return ShapeFactory.makeSphere(0.05f, Vector3.zero(), material)
    }

    override fun onDestroy() {
        super.onDestroy()
        renderableJob.cancel()
    }
}