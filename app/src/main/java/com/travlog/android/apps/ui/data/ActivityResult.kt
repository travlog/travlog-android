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
