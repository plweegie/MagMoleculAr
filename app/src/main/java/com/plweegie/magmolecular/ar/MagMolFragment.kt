package com.plweegie.magmolecular.ar

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.*


class MagMolFragment : ArFragment() {

    companion object {
        private val MIN_OPENGL_VERSION = 3.1
    }

    private val displayMetrics: DisplayMetrics by lazy {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        metrics
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //TODO add OpenGL check
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false

        return view
    }

    override fun makeTransformationSystem(): TransformationSystem {

        val transformationSystem = TransformationSystem(displayMetrics, null)
        val gesturePointersUtility = GesturePointersUtility(displayMetrics)

        val dragGestureRecognizer = DragGestureRecognizer(gesturePointersUtility)

        dragGestureRecognizer.addOnGestureStartedListener {
            val startPosition = it.position
            var endPosition = Vector3.zero()

            it.setGestureEventListener(object : BaseGesture.OnGestureEventListener<DragGesture> {
                override fun onUpdated(gesture: DragGesture?) {
                    endPosition = gesture?.position
                    val angle = getRotationAngle(startPosition, endPosition)
                    val rotationAxis = gesture?.targetNode?.parent?.worldPosition
                    val adjustedRotationAxis = Vector3(rotationAxis!!.x, rotationAxis.z, rotationAxis.y)

                    gesture.targetNode?.parent?.localRotation = Quaternion.multiply(
                        gesture.targetNode?.parent?.localRotation,
                        Quaternion.axisAngle(rotationAxis, angle)
                    )
                }

                override fun onFinished(gesture: DragGesture?) {}
            })
        }
        transformationSystem.apply {
            addGestureRecognizer(dragGestureRecognizer)
            selectionVisualizer = NullVisualizer()
        }
        return transformationSystem
    }

    private fun getRotationAngle(startPosition: Vector3, endPosition: Vector3): Float {
        val diff = endPosition.x - startPosition.x
        return  (10 * diff) / displayMetrics.widthPixels
    }

    class NullVisualizer : SelectionVisualizer {
        override fun applySelectionVisual(node: BaseTransformableNode?) {}
        override fun removeSelectionVisual(node: BaseTransformableNode?) {}
    }
}