package com.appsamurai.storyly.reactnative.monetization

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.appsamurai.storyly.reactnative.STStorylyManager
import com.appsamurai.storyly.reactnative.STStorylyView
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.UIManagerModule


class STStorylyMonetization (
    private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val REACT_CLASS = "STStorylyMonetization"
    }

    override fun initialize() {
        super.initialize()
    }

    @ReactMethod
    fun setAdViewProvider(reactViewId: Int, testParam: String) {
        Handler(Looper.getMainLooper()).post {
            val uiManagerModule = reactContext.getNativeModule(UIManagerModule::class.java) ?: return@post
            val stStorylyView = uiManagerModule.resolveView(reactViewId) as? STStorylyView ?: return@post
            stStorylyView.setAdProvider(testParam)
        }
    }


    override fun getName(): String = REACT_CLASS

    private fun getActivityContext(): Context {
        return reactApplicationContext.currentActivity ?: reactApplicationContext
    }
}
