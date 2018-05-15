package com.travlog.android.apps.ui.data

import android.content.Intent

class ActivityResult {

    var requestCode: Int = 0
    var resultCode: Int = 0
    var intent: Intent? = null

    override fun toString(): String {
        return "ActivityResult{" +
                "requestCode=" + requestCode +
                ", resultCode=" + resultCode +
                ", intent=" + intent +
                '}'.toString()
    }

    companion object {

        fun create(requestCode: Int, resultCode: Int, intent: Intent?): ActivityResult {
            val activityResult = ActivityResult()
            activityResult.requestCode = requestCode
            activityResult.resultCode = resultCode
            if (intent == null) {
                activityResult.intent = Intent()
            } else {
                activityResult.intent = intent
            }

            return activityResult
        }
    }
}
