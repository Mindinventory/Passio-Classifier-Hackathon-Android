package com.mindinventory.dermatai.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.safeObserve(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner) { it?.let(observer) }
}
