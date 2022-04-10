package com.example.mystoryapp.cv

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class CustomLoginButton: AppCompatButton {

    private lateinit var enabledBg: Drawable
    private lateinit var disabledBg: Drawable
    private var txtColor: Int = 0
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = if(isEnabled) enabledBg else disabledBg
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = resources.getString(R.string.login)
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBg = ContextCompat.getDrawable(context, R.drawable.bg_button_login) as Drawable
        disabledBg= ContextCompat.getDrawable(context, R.drawable.bg_button_login_disabled) as Drawable
    }
}