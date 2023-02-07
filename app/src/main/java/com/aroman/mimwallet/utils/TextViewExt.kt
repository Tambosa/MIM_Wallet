package com.aroman.mimwallet.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.widget.TextView

fun TextView.animatePriceNumbers(duration: Long, number: Double) {
    ValueAnimator().apply {
        setObjectValues(number*0.9, number)
        setDuration(duration)
        addUpdateListener {
            this@animatePriceNumbers.text = String.format("$%.2f", it.animatedValue)
        }
        setEvaluator(TypeEvaluator<Double> { fraction, startValue, endValue ->
            startValue + (endValue - startValue) * fraction
        })
        addListener(object: AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                this@animatePriceNumbers.text = String.format("$%.2f", number)
            }

            override fun onAnimationStart(p0: Animator?) {}

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {}
        })
    }.start()
}