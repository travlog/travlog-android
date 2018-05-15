package com.travlog.android.apps.libs.qualifiers

import com.travlog.android.apps.FragmentViewModel

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresFragmentViewModel(val value: KClass<out FragmentViewModel<*>>)
