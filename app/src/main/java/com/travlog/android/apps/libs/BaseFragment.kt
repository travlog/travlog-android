package com.travlog.android.apps.libs

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.travlog.android.apps.FragmentViewModel
import com.travlog.android.apps.libs.qualifiers.RequiresFragmentViewModel
import com.travlog.android.apps.libs.utils.BundleUtils.maybeGetBundle
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

open class BaseFragment<ViewModelType : FragmentViewModel<*>> : RxFragment(), FragmentLifeCycleType {
    private val disposables = CompositeDisposable()
    private var unbinder: Unbinder? = null
    protected var viewModel: ViewModelType? = null

    fun viewModel(): ViewModelType? {
        return this.viewModel
    }

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.d("onAttach %s", this.toString())
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate %s", this.toString())
        assignViewModel(savedInstanceState)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView %s", this.toString())
        if (viewModel != null)
            viewModel!!.onCreateView(inflater, container, savedInstanceState, this as Nothing)


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun setContentView(@LayoutRes layoutResID: Int): View {
        val view = LayoutInflater.from(context).inflate(layoutResID, null)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        Timber.d("onStart %s", this.toString())
        if (viewModel != null)
            viewModel!!.onStart()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Timber.d("onResume %s", this.toString())
        if (viewModel != null)
            viewModel!!.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Timber.d("onPause %s", this.toString())
        if (viewModel != null)
            viewModel!!.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        Timber.d("onStop %s", this.toString())
        if (viewModel != null)
            viewModel!!.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView %s", this.toString())
        if (viewModel != null)
            viewModel!!.onDestroyView()
        unbinder!!.unbind()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy %s", this.toString())
        if (viewModel != null)
            viewModel!!.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        Timber.d("onDetach %s", this.toString())
        super.onDetach()

        if (activity!!.isFinishing) {
            if (viewModel != null) {
                viewModel!!.onDetach()
            }
        }
    }

    private fun assignViewModel(viewModelEnvelope: Bundle?) {
        if (viewModel == null) {
            val viewModelTypeClass = javaClass.getAnnotation(RequiresFragmentViewModel::class.java)?.value
            if (viewModelTypeClass != null) {
                viewModel = FragmentViewModelManager.instance.fetch(context!!,
                        viewModelTypeClass,
                        maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY))
            }
        }
    }

    companion object {

        private val VIEW_MODEL_KEY = "viewModel"
    }
}
