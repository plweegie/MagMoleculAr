package com.plweegie.magmolecular.utils

import android.content.Context
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.MaterialFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.future.asDeferred
import org.openscience.cdk.interfaces.IAtom


class ArMolecule(atoms: List<IAtom>, private val context: Context) {

    val centerCoordX: Float = (atoms.sumByDouble { it.point3d.x } / atoms.size).toFloat()
    val centerCoordY: Float = (atoms.sumByDouble { it.point3d.y } / atoms.size).toFloat()
    val centerCoordZ: Float = (atoms.sumByDouble { it.point3d.z } / atoms.size).toFloat()

    private val arAtoms: List<ArAtom> = atoms.map { atom ->
        with(atom.point3d) {
            ArAtom(x.toFloat(), y.toFloat(), z.toFloat(), atom.symbol)
        }
    }

    private val materials: List<Deferred<Material>> = arAtoms.map {
        getMaterialAsync(it.color)
    }

    val renderableAtoms: List<Pair<ArAtom, Deferred<Material>>> = arAtoms zip materials

    private fun getMaterialAsync(color: Int): Deferred<Material> =
        MaterialFactory.makeOpaqueWithColor(context, com.google.ar.sceneform.rendering.Color(color)).asDeferred()
}