package com.travlog.android.apps.libs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.travlog.android.apps.FragmentViewModel
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

open class BaseFragment<ViewModelType : FragmentViewModel<*>> : RxFragment(), FragmentLifeCycleType {
    private val disposables = CompositeDisposable()
    private val VIEW_MODEL_KEY = "viewModel"
    protected var viewModel: ViewModelType? = null

    fun viewModel(): ViewModelType? {
        return this.viewModel
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach %s", this.toString())
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate %s", this.toString())
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView %s", this.toString())

        viewModel?.onCreateView(inflater, container, savedInstanceState, this)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        Timber.d("onStart %s", this.toString())
        viewModel?.onStart()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Timber.d("onResume %s", this.toString())
        viewModel?.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Timber.d("onPause %s", this.toString())
        viewModel?.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        Timber.d("onStop %s", this.toString())
        viewModel?.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView %s", this.toString())
        viewModel?.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy %s", this.toString())
        viewModel?.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        Timber.d("onDetach %s", this.toString())
        super.onDetach()

        activity?.apply {
            if (isFinishing) {
                viewModel?.onDetach()
            }
        }
    }
}
