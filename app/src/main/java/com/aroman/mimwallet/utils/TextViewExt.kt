package com.aroman.mimwallet.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import java.text.DecimalFormat

fun TextView.animateNumbers(duration: Long, number: Double, formatter: DecimalFormat) {
    ValueAnimator().apply {
        setObjectValues(number*0.9, number)
        setDuration(duration)
        addUpdateListener {
            this@animateNumbers.text = String.format("$%.2f", it.animatedValue)
        }
        setEvaluator(TypeEvaluator<Double> { fraction, startValue, endValue ->
            startValue + (endValue - startValue) * fraction
        })
        addListener(object: AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                this@animateNumbers.text = formatter.format(number)
            }

            override fun onAnimationStart(p0: Animator?) {}

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {}
        })
    }.start()
}