package com.aroman.mimwallet.utils.pie_chart_view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.aroman.mimwallet.R
import kotlin.math.cos
import kotlin.math.sin

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var data: PieData? = null

    private var pieState = PieState.MINIMIZED
    private var initialHeight: Int? = null

    private val borderPaint = Paint()
    private val linePaint = Paint()
    private val indicatorCirclePaint = Paint()
    private var indicatorCircleRadius = 0f
    private val mainTextPaint = Paint()
    private val oval = RectF()
    private val innerOval = RectF()
    private val innerOvalPaint = Paint()

    private val expandAnimator = ValueAnimator.ofInt()
    private val collapseAnimator = ValueAnimator.ofInt()
    private val textAlpha = ValueAnimator.ofInt()
    private val animateExpansion = AnimatorSet()
    private val animateCollapse = AnimatorSet()

    init {
        borderPaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = Color.WHITE
        }
        indicatorCirclePaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = Color.LTGRAY
            alpha = 0
        }
        linePaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = Color.LTGRAY
            alpha = 0
        }
        mainTextPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            alpha = 0
        }
        innerOvalPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            val typedValue = TypedValue()
            context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true)
            color = typedValue.data
        }
        setupAnimations()
    }

    fun setData(data: PieData) {
        this.data = data
        setPieSliceDimensions()
        invalidate()
    }

    private fun setPieSliceDimensions() {
        var lastAngle = 0f
        data?.pieSlices?.forEach {
            it.value.startAngle = lastAngle
            it.value.sweepAngle = (((it.value.value / data?.totalValue!!)) * 360f).toFloat()
            lastAngle += it.value.sweepAngle
            setIndicatorLocation(it.key)
        }
    }


    private fun setIndicatorLocation(key: String) {
        data?.pieSlices?.get(key)?.let {
            val middleAngle = it.sweepAngle / 2 + it.startAngle

            it.indicatorCircleLocation.x =
                (layoutParams.height.toFloat() / 1.8f - layoutParams.height / 8) *
                        cos(Math.toRadians(middleAngle.toDouble())).toFloat() + width / 2
            it.indicatorCircleLocation.y =
                (layoutParams.height.toFloat() / 1.8f - layoutParams.height / 8) *
                        sin(Math.toRadians(middleAngle.toDouble()))
                            .toFloat() + layoutParams.height / 2
        }
    }

    private fun setCircleBounds(
        top: Float = 0f, bottom: Float = layoutParams.height.toFloat(),
        left: Float = (width / 2) - (layoutParams.height / 2).toFloat(),
        right: Float = (width / 2) + (layoutParams.height / 2).toFloat()
    ) {
        oval.top = top
        oval.bottom = bottom
        oval.left = left
        oval.right = right

        innerOval.left = (width / 2) - (layoutParams.height / 3).toFloat()
        innerOval.top = layoutParams.height.toFloat() / 6
        innerOval.right = (width / 2) + (layoutParams.height / 3).toFloat()
        innerOval.bottom = layoutParams.height.toFloat() - layoutParams.height.toFloat() / 6
    }

    private fun setGraphicSizes() {
        mainTextPaint.textSize = height / 10f
        borderPaint.strokeWidth = height / 80f
        linePaint.strokeWidth = height / 120f
        indicatorCircleRadius = height / 70f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setCircleBounds()
        setGraphicSizes()
        data?.pieSlices?.forEach {
            setIndicatorLocation(it.key)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        data?.pieSlices?.let { slices ->
            slices.forEach {
                canvas?.drawArc(
                    oval,
                    it.value.startAngle,
                    it.value.sweepAngle,
                    true,
                    it.value.paint
                )
//                border if needed
//                canvas?.drawArc(oval, it.value.startAngle, it.value.sweepAngle, false, borderPaint)
                drawIndicators(canvas, it.value)
            }
        }
        canvas?.drawArc(innerOval, 0f, 360f, false, innerOvalPaint)
    }

    private fun drawIndicators(canvas: Canvas?, pieItem: PieSlice) {
        if (pieItem.indicatorCircleLocation.x < width / 2) {
            drawIndicatorLine(canvas, pieItem, IndicatorAlignment.LEFT)
            drawIndicatorText(canvas, pieItem, IndicatorAlignment.LEFT)
        } else {
            drawIndicatorLine(canvas, pieItem, IndicatorAlignment.RIGHT)
            drawIndicatorText(canvas, pieItem, IndicatorAlignment.RIGHT)
        }
        canvas?.drawCircle(
            pieItem.indicatorCircleLocation.x, pieItem.indicatorCircleLocation.y,
            indicatorCircleRadius, indicatorCirclePaint
        )
    }

    private fun drawIndicatorLine(
        canvas: Canvas?,
        pieItem: PieSlice,
        alignment: IndicatorAlignment
    ) {
        val xOffset = if (alignment == IndicatorAlignment.LEFT) width / 4 * -1 else width / 4
        canvas?.drawLine(
            pieItem.indicatorCircleLocation.x,
            pieItem.indicatorCircleLocation.y,
            pieItem.indicatorCircleLocation.x + xOffset,
            pieItem.indicatorCircleLocation.y,
            linePaint
        )
    }

    private fun drawIndicatorText(
        canvas: Canvas?,
        pieItem: PieSlice,
        alignment: IndicatorAlignment
    ) {
        val xOffset = if (alignment == IndicatorAlignment.LEFT) width / 4 * -1 else width / 4
        if (alignment == IndicatorAlignment.LEFT) mainTextPaint.textAlign = Paint.Align.LEFT
        else mainTextPaint.textAlign = Paint.Align.RIGHT
        canvas?.drawText(
            pieItem.name, pieItem.indicatorCircleLocation.x + xOffset,
            pieItem.indicatorCircleLocation.y - 10, mainTextPaint
        )
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        if (event?.action == MotionEvent.ACTION_DOWN) return true
//        if (event?.action == MotionEvent.ACTION_UP) {
//            when (pieState) {
//                PieState.MINIMIZED -> expandPieChart()
//                PieState.EXPANDED -> collapsePieChart()
//            }
//        }
//        return super.onTouchEvent(event)
//    }

    fun expandPieChart() {
        expandAnimator.setIntValues(layoutParams.height, (width / 2.5).toInt())
        textAlpha.setIntValues(0, 255)
        animateExpansion.start()
    }

    fun collapsePieChart() {
        initialHeight?.let {
            collapseAnimator.setIntValues(layoutParams.height, it)
            textAlpha.setIntValues(255, 0)
            animateCollapse.start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (initialHeight == null) initialHeight = layoutParams.height
    }

    private fun setupAnimations() {
        expandAnimator.duration = ANIMATION_DURATION
        expandAnimator.interpolator = OvershootInterpolator()
        expandAnimator.addUpdateListener {
            layoutParams.height = it.animatedValue as Int
            requestLayout()
            setCircleBounds()
            setPieSliceDimensions()
            invalidate()
        }
        expandAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                pieState = PieState.EXPANDED
            }
        })

        collapseAnimator.duration = ANIMATION_DURATION
        collapseAnimator.interpolator = DecelerateInterpolator()
        collapseAnimator.addUpdateListener {
            layoutParams.height = it.animatedValue as Int
            requestLayout()
            setCircleBounds()
            setPieSliceDimensions()
            invalidate()
        }
        collapseAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                pieState = PieState.MINIMIZED
            }
        })

        textAlpha.duration = ANIMATION_DURATION
        textAlpha.interpolator = DecelerateInterpolator()
        textAlpha.addUpdateListener {
            mainTextPaint.alpha = it.animatedValue as Int
            linePaint.alpha = it.animatedValue as Int
            indicatorCirclePaint.alpha = it.animatedValue as Int
            invalidate()
        }

        animateExpansion.play(expandAnimator).with(textAlpha)
        animateCollapse.play(collapseAnimator).with(textAlpha)
    }

    enum class IndicatorAlignment {
        LEFT, RIGHT
    }

    enum class PieState {
        MINIMIZED, EXPANDED
    }

    companion object {
        const val ANIMATION_DURATION = 800L
    }
}