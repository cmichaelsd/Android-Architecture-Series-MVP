package com.colemichaels.mvp_demo

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}