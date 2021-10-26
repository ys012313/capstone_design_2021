package com.cm.taxi.util

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cm.taxi.R
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import java.util.concurrent.TimeUnit

@BindingAdapter("dismissKeyboardOnClick")
fun View.setOnClickListenerForDismissSoftInput(dismiss: Boolean) {
    if (dismiss) {
        setOnClickListener {
            val imm = context.getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(rootView.windowToken, 0)
            rootView.findFocus()?.clearFocus()
        }
    }
}

@BindingAdapter(value = ["fullColor", "openColor"],requireAll = false)
fun TextView.changePeopleColor(currentPeople : Int , totalPeople : Int) {
    if(currentPeople == totalPeople){
        setTextColor(Color.parseColor("#ff0000"))
    }else{
        setTextColor(Color.parseColor("#3d3d3d"))
    }
}

@SuppressLint("CheckResult")
@BindingAdapter(value = ["onShortBlockClick"])
fun View.setOnShortBlockClick(listener: View.OnClickListener) {
    clicks().throttleFirst(250L, TimeUnit.MILLISECONDS)
        .subscribe {
            listener.onClick(this)
        }
}

@BindingAdapter("visibilityWithAnim", "duration", requireAll = false)
fun View.setVisibilityWithAnim(visible: Boolean, _duration: Long) {
    val duration = if (_duration == 0L) 200L else _duration
    if (visible) {
        animate().alpha(1f).setDuration(duration).withStartAction {
            alpha = 0f
            isVisible = true
        }.start()
    } else {
        animate().alpha(0f).setDuration(duration).withEndAction {
            isVisible = false
        }.start()
    }
}



@CheckResult
@ExperimentalCoroutinesApi
fun SwipeRefreshLayout.refresh(): Flow<Unit> = callbackFlow {

    val listener = SwipeRefreshLayout.OnRefreshListener {
        trySend(Unit)
    }
    setOnRefreshListener(listener)
    awaitClose {
        setOnRefreshListener(null)
    }
}.conflate()





