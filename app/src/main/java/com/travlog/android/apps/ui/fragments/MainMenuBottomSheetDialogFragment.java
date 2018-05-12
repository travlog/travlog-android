package com.travlog.android.apps.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travlog.android.apps.R;
import com.travlog.android.apps.ui.activities.MyPageActivity;
import com.travlog.android.apps.ui.activities.SignInActivity;
import com.travlog.android.apps.ui.activities.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.sign_in)
    View signInButton;
    @BindView(R.id.sign_up)
    View signUpButton;
    @BindView(R.id.my_page)
    View myPageButton;

    public static MainMenuBottomSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        MainMenuBottomSheetDialogFragment fragment = new MainMenuBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.f_main_menu_bottom_sheet, null);
        ButterKnife.bind(this, view);

        signInButton.setOnClickListener(v -> {
            final Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);
            dismiss();
        });

        signUpButton.setOnClickListener(v -> {
            final Intent intent = new Intent(getContext(), SignUpActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);
            dismiss();
        });

        myPageButton.setOnClickListener(v -> {
            final Intent intent = new Intent(getContext(), MyPageActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);
            dismiss();
        });

        return view;
    }
}
