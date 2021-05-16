package dev.xyze.namecolor.util

object ColorUtil {
    fun validateColor(color: String): Boolean {
        val colorMatcher = Regex("^#([a-fA-F0-9]{6})$")
        return color matches colorMatcher
    }
}