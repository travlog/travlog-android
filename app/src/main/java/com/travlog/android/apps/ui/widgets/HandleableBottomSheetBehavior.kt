package com.travlog.android.apps.ui.widgets

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class HandleableBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {


    private var handle: View? = null
    private var handleable = true

    constructor() : super()

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    companion object {
        fun <V : View> from(view: V): HandleableBottomSheetBehavior<V> {
            val params = view.layoutParams as? CoordinatorLayout.LayoutParams
                    ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            val behavior = params.behavior as? BottomSheetBehavior<*>
                    ?: throw IllegalArgumentException("The view is not associated with BottomSheetBehavior")
            return behavior as HandleableBottomSheetBehavior<V>
        }
    }

    fun setHandleView(handleView: View?) {
        this.handle = handleView
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        handle?.apply {
            val handle = Rect(left, top, right, bottom)

            handleable = handle.contains(event.x.toInt(), event.y.toInt())
            return handleable
        }
        return onInterceptTouchEvent(parent, child, event)
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return when {
            handleable -> super.onTouchEvent(parent, child, event)
            else -> false
        }
    }
}