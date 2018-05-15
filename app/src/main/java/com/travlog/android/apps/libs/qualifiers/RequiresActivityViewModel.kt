package com.travlog.android.apps.libs.qualifiers

import com.travlog.android.apps.libs.ActivityViewModel
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresActivityViewModel(val value: KClass<out ActivityViewModel<*>>)
