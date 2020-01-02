package com.cuhacking.app.admin.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.cuhacking.app.R

class ResultOverlay(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    enum class State {
        SUCCESS,
        FAILURE
    }

    private val icon: ImageView by lazy { findViewById<ImageView>(R.id.icon) }

    init {
        View.inflate(context, R.layout.result_overlay, this)

        alpha = 0f
    }

    fun updateState(state: State) {
        icon.setImageResource(if (state == State.SUCCESS) R.drawable.ic_check else R.drawable.ic_clear)
        setBackgroundColor(ContextCompat.getColor(context, if (state == State.SUCCESS) R.color.success else R.color.error))

        alpha = 0.95f
        animate().apply {
            duration = 2000
            startDelay = 500
            alpha(0f)
            start()
        }
    }
}