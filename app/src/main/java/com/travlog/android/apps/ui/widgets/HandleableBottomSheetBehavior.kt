/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travlog.android.apps.ui.widgets

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

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
        if (parent.getChildAt(0) is Toolbar) {
            parent.getChildAt(0).apply {
                Rect(left, top, right, bottom).let {
                    if (it.contains(event.x.toInt(), event.y.toInt())) {
                        handleable = true
                        return super.onInterceptTouchEvent(parent, child, event)
                    }
                }
            }
        }

        handle?.apply {
            Rect(left, top, right, bottom).let {
                it.contains(event.x.toInt(), event.y.toInt()).let {
                    handleable = it
                    return handleable
                }
            }
        }
        return super.onInterceptTouchEvent(parent, child, event)
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return when {
            handleable -> super.onTouchEvent(parent, child, event)
            else -> false
        }
    }
}