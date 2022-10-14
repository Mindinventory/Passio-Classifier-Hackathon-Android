package com.mindinventory.dermatai.common.extensions

import java.util.*

/**
 * Uses for make first letter capital in string
 */
fun String?.capitalized(): String {
    return this?.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    } ?: ""
}