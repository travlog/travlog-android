package com.travlog.android.apps.libs

import android.content.Context
import android.os.Bundle
import com.travlog.android.apps.FragmentViewModel
import com.travlog.android.apps.TravlogApplication
import com.travlog.android.apps.libs.utils.maybeGetBundle
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class FragmentViewModelManager {
    private val viewModels = HashMap<String, FragmentViewModel<*>>()

    fun <T : FragmentViewModel<*>> fetch(context: Context, viewModelClass: KClass<out FragmentViewModel<*>>,
                                         savedInstanceState: Bundle?): T? {
        val id = fetchId(savedInstanceState)
        var viewModel: FragmentViewModel<*>? = this.viewModels[id]

        if (viewModel == null) {
            viewModel = create(context, viewModelClass, savedInstanceState, id!!)
        }

        return viewModel as T?
    }

    fun destroy(viewModel: FragmentViewModel<*>) {
        viewModel.onDestroy()

        val iterator = this.viewModels.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (viewModel == entry.value) {
                iterator.remove()
            }
        }
    }

    fun save(viewModel: FragmentViewModel<*>, envelope: Bundle) {
        envelope.putString(VIEW_MODEL_ID_KEY, findIdForViewModel(viewModel))

        val state = Bundle()
        envelope.putBundle(VIEW_MODEL_STATE_KEY, state)
    }

    private fun <T : FragmentViewModel<*>> create(context: Context, viewModelClass: KClass<T>,
                                                  savedInstanceState: Bundle?, id: String): FragmentViewModel<*> {

        val application = context.applicationContext as TravlogApplication
        val environment = application.component()!!.environment()
        val viewModel: FragmentViewModel<*>

        try {
            val constructor = viewModelClass.primaryConstructor
            viewModel = constructor!!.call(environment)
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

        this.viewModels[id] = viewModel
        viewModel.onCreate(context, maybeGetBundle(savedInstanceState, VIEW_MODEL_STATE_KEY))

        return viewModel
    }

    private fun fetchId(savedInstanceState: Bundle?): String? {
        return if (savedInstanceState != null)
            savedInstanceState.getString(VIEW_MODEL_ID_KEY)
        else
            UUID.randomUUID().toString()
    }

    private fun findIdForViewModel(viewModel: FragmentViewModel<*>): String {
        for ((key, value) in this.viewModels) {
            if (viewModel == value) {
                return key
            }
        }

        throw RuntimeException("Cannot find view model in map!")
    }

    companion object {

        private val VIEW_MODEL_ID_KEY = "fragment_view_model_id"
        private val VIEW_MODEL_STATE_KEY = "fragment_view_model_state"

        val instance = FragmentViewModelManager()
    }
}
