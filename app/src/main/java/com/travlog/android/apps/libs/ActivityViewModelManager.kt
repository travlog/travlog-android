package com.travlog.android.apps.libs

import android.content.Context
import android.os.Bundle
import com.travlog.android.apps.TravlogApplication
import com.travlog.android.apps.libs.utils.estimatedDeliveryOn
import com.travlog.android.apps.libs.utils.maybeGetBundle
import timber.log.Timber
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ActivityViewModelManager {
    private val viewModels = HashMap<String, ActivityViewModel<*>>()

    fun <T : ActivityViewModel<*>> fetch(context: Context, viewModelClass: KClass<out ActivityViewModel<*>>,
                                         savedInstanceState: Bundle?): T? {
        val id = fetchId(savedInstanceState)
        var activityViewModel: ActivityViewModel<*>? = this.viewModels[id]

        if (activityViewModel == null) {
            activityViewModel = create(context, viewModelClass, savedInstanceState, id!!)
        }

        return activityViewModel as T?
    }

    fun destroy(activityViewModel: ActivityViewModel<*>) {
        activityViewModel.onDestroy()

        val iterator = this.viewModels.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (activityViewModel == entry.value) {
                iterator.remove()
            }
        }
    }

    fun save(activityViewModel: ActivityViewModel<*>, envelope: Bundle) {
        envelope.putString(VIEW_MODEL_ID_KEY, findIdForViewModel(activityViewModel))

        val state = Bundle()
        envelope.putBundle(VIEW_MODEL_STATE_KEY, state)
    }

    private fun <T : ActivityViewModel<*>> create(context: Context, viewModelClass: KClass<T>,
                                                  savedInstanceState: Bundle?, id: String): ActivityViewModel<*> {

        val application = context.applicationContext as TravlogApplication
        val environment = application.component()?.environment()
        val activityViewModel: ActivityViewModel<*>

        try {
            val constructor = viewModelClass.primaryConstructor
            activityViewModel = constructor!!.call(environment)

            // Need to catch these exceptions separately, otherwise the compiler turns them into `ReflectiveOperationException`.
            // That exception is only available in API19+
        } catch (exception: IllegalAccessException) {
            throw RuntimeException(exception)
        } catch (exception: InvocationTargetException) {
            throw RuntimeException(exception)
        } catch (exception: InstantiationException) {
            throw RuntimeException(exception)
        } catch (exception: NoSuchMethodException) {
            throw RuntimeException(exception)
        }

        this.viewModels[id] = activityViewModel
        activityViewModel.onCreate(context, maybeGetBundle(savedInstanceState, VIEW_MODEL_STATE_KEY))

        return activityViewModel
    }

    private fun fetchId(savedInstanceState: Bundle?): String? {
        return if (savedInstanceState != null)
            savedInstanceState.getString(VIEW_MODEL_ID_KEY)
        else
            UUID.randomUUID().toString()
    }

    private fun findIdForViewModel(activityViewModel: ActivityViewModel<*>): String {
        for ((key, value) in this.viewModels) {
            if (activityViewModel == value) {
                return key
            }
        }

        throw RuntimeException("Cannot find view model in map!")
    }

    companion object {

        private val VIEW_MODEL_ID_KEY = "view_model_id"
        private val VIEW_MODEL_STATE_KEY = "view_model_state"

        val instance = ActivityViewModelManager()
    }
}
