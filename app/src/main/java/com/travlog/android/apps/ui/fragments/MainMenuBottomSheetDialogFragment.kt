package com.travlog.android.apps.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.travlog.android.apps.R
import com.travlog.android.apps.ui.activities.MyPageActivity
import com.travlog.android.apps.ui.activities.SignInActivity
import com.travlog.android.apps.ui.activities.SignUpActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.f_main_menu_bottom_sheet.*

class MainMenuBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {

        fun newInstance(): MainMenuBottomSheetDialogFragment {
            val args = Bundle()

            MainMenuBottomSheetDialogFragment()
                    .let {
                        it.arguments = args
                        return it
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.f_main_menu_bottom_sheet, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out)
            dismiss()
        }

        sign_up.setOnClickListener {
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out)
            dismiss()
        }

        my_page.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }
}
