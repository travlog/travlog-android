package com.travlog.android.apps.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.travlog.android.apps.R
import kotlinx.android.synthetic.main.w_state_edit_text.view.*

class StateEditText : ConstraintLayout {

    private var state: State = State.NORMAL

    private var boxCornerRadiusTopLeft = 0f
    private var boxCornerRadiusTopRight = 0f
    private var boxCornerRadiusBottomLeft = 0f
    private var boxCornerRadiusBottomRight = 0f

    private var helperTextEnabled = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.w_state_edit_text, this)

        context.obtainStyledAttributes(attrs, R.styleable.StateEditText).apply {
            for (i in 0 until indexCount) {
                getIndex(i).let {
                    when (it) {
                        R.styleable.StateEditText_boxCornerRadiusTopLeft -> boxCornerRadiusTopLeft = getDimension(it, 0f)
                        R.styleable.StateEditText_boxCornerRadiusTopRight -> boxCornerRadiusTopRight = getDimension(it, 0f)
                        R.styleable.StateEditText_boxCornerRadiusBottomLeft -> boxCornerRadiusBottomLeft = getDimension(it, 0f)
                        R.styleable.StateEditText_boxCornerRadiusBottomRight -> boxCornerRadiusBottomRight = getDimension(it, 0f)
                        R.styleable.StateEditText_text -> setText(getString(it))
                        R.styleable.StateEditText_textSize -> setTextSize(getDimension(it, 14f))
                        R.styleable.StateEditText_textColor -> {
                            val textColor = getColorStateList(it)
                            when (textColor) {
                                null -> setTextColor(ColorStateList.valueOf(0xFF000000.toInt()))
                                else -> setTextColor(textColor)
                            }
                        }
                        R.styleable.StateEditText_textColorHint -> getColorStateList(it).let {
                            if (it != null) {
                                setHintTextColor(it)
                            }
                        }
                        R.styleable.StateEditText_hint -> setHint(getString(it))
                        R.styleable.StateEditText_inputType -> text_input_edit_text.inputType = getInt(it, EditorInfo.TYPE_NULL)
                        R.styleable.StateEditText_helperTextEnabled -> helperTextEnabled = getBoolean(it, false)
                        R.styleable.StateEditText_helperText -> setHelperText(getString(it))
                    }
                }
            }

            recycle()
        }


        setState(state)
        setBoxCornerRadii(boxCornerRadiusTopLeft, boxCornerRadiusTopRight, boxCornerRadiusBottomLeft, boxCornerRadiusBottomRight)
    }

    fun getEditText(): EditText {
        return text_input_edit_text
    }

    fun setHelperTextEnabled(enabled: Boolean) {
        this.helperTextEnabled = enabled

        when (enabled) {
            true -> helper.visibility = View.VISIBLE
            false -> helper.visibility = View.GONE
        }
    }

    fun isHelperTextEnabled(): Boolean {
        return this.helperTextEnabled
    }

    fun setHelperText(helperText: CharSequence) {
        helper.text = helperText
    }

    fun getHelperText(): CharSequence {
        return helper.text
    }

    fun setText(text: CharSequence) = text_input_edit_text.setText(text)

    fun getText(): Editable? {
        return text_input_edit_text.text
    }

    fun setHintTextColor(color: Int) = text_input_edit_text.setHintTextColor(color)

    fun setHintTextColor(colors: ColorStateList) = text_input_edit_text.setHintTextColor(colors)

    fun setState(state: State) {
        this.state = state
    }

    fun setBoxCornerRadii(boxCornerRadiusTopLeft: Float, boxCornerRadiusTopRight: Float,
                          boxCornerRadiusBottomLeft: Float, boxCornerRadiusBottomRight: Float) {

        text_input_layout.setBoxCornerRadii(boxCornerRadiusTopLeft, boxCornerRadiusTopRight, boxCornerRadiusBottomLeft, boxCornerRadiusBottomRight)
    }

    fun setTextSize(textSize: Float) {
        text_input_edit_text.textSize = textSize
    }

    fun setTextColor(color: Int) = text_input_edit_text.setTextColor(color)

    fun setTextColor(colors: ColorStateList) = text_input_edit_text.setTextColor(colors)

    fun setHint(hint: CharSequence?) {
        text_input_edit_text.hint = hint
    }

    fun getHint(): CharSequence {
        return text_input_edit_text.hint
    }

    enum class State {
        DISABLED, NORMAL, FOCUS, SUCCESS, WARNING, ERROR
    }
}