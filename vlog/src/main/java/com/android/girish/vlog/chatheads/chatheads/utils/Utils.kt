package com.android.girish.vlog.chatheads.chatheads.utils

import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.WindowManager
import android.util.TypedValue

fun getOverlayFlag(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_PHONE
    }
}

fun getScreenSize(): DisplayMetrics {
    return Resources.getSystem().displayMetrics
}

fun dpToPx(dp: Float): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun spToPx(sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().displayMetrics)
}

fun runOnMainLoop(fn: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        fn()
    }
}

fun getAvatarUrl(url: String?, fallbackUrl: String): String {
    return if (url != null) {
        url
    } else {
        fallbackUrl
    }
}

fun debug(txt: String) {
    println("asdf: $txt")
}