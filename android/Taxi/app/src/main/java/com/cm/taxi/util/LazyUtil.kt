package com.cm.taxi.util

import android.app.Activity
import android.os.Parcelable
import androidx.fragment.app.Fragment

/*
Lazy로 선언될 변수를 명시적으로 생성해주기 위한 함수

ex)
val viewModel: ViewModel = by lazy { createViewModel(); }

override fun onCreate() {
    viewModel.initLazy()
}
*/
fun Any.initLazy() {
    // no-op
}

inline fun <reified T : Parcelable> Activity.getParcelableExtra(key: String) =
    lazy { intent.getParcelableExtra<T>(key) }

inline fun <reified T : Parcelable> Fragment.getParcelableExtras(key: String) =
    lazy { this.requireArguments().getParcelable<T>(key) }