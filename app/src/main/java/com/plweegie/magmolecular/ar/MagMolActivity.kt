package com.plweegie.magmolecular.ar

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.plweegie.magmolecular.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch


class MagMolActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var sphereRenderable: ModelRenderable? = null

    private val renderableJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + renderableJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mag_mol)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        uiScope.launch {
            sphereRenderable = getModelRenderable(this@MagMolActivity)
        }

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->

            sphereRenderable?.let {
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)

                Node().apply {
                    setParent(anchorNode)
                    renderable = it
                }

                TransformableNode(arFragment.transformationSystem).apply {
                    setParent(anchorNode)
                }

                arFragment.arSceneView.scene.addChild(anchorNode)
            }
        }
    }

    private suspend fun getModelRenderable(context: Context): ModelRenderable {
        val material = MaterialFactory.makeOpaqueWithColor(context, Color(android.graphics.Color.RED)).await()
        return ShapeFactory.makeSphere(0.1f, Vector3(0.0f, 0.15f, 0.0f), material)
    }

    override fun onDestroy() {
        super.onDestroy()
        renderableJob.cancel()
    }
}