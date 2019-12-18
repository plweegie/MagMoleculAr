package com.plweegie.magmolecular.utils

import android.graphics.Color


data class ArAtom(val xCoord: Float,
                  val yCoord: Float,
                  val zCoord: Float,
                  private val element: String) {

    companion object {
        private val COLOR_MAPPINGS: HashMap<String, Int> = hashMapOf(
            "C" to Color.BLACK,
            "O" to Color.RED,
            "N" to Color.BLUE,
            "Cl" to Color.GREEN,
            "S" to Color.YELLOW,
            "P" to Color.parseColor("#FFA500"),
            "Br" to Color.parseColor("maroon"),
            "I" to Color.parseColor("fuchsia"),
            "F" to Color.parseColor("#BFFF00")
        )
    }

    val color: Int = COLOR_MAPPINGS[element] ?: Color.BLACK
}