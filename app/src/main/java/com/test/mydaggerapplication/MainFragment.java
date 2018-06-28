package com.test.mydaggerapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.test.dagger.android.MainFragmentComponent;
import com.test.dagger.android.ToastUtil;
import com.test.mytestapplication.R;

import javax.inject.Inject;

public class MainFragment extends Fragment implements MainFragmentContract.View {

    @Inject
    MainFragmentContract.Presenter mainPresenter;
    @Inject
    ToastUtil toastUtil;

    private MainFragmentComponent mainFragmentComponent;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            mainFragmentComponent = ((MainActivity) getActivity()).getMainComponent().mainFragmentComponent();
            mainFragmentComponent.inject(this);

        }
        mainPresenter.setView(this);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.dagger_fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnToast = (Button) view.findViewById(R.id.btn_toast);
        btnToast.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                mainPresenter.toastButtonClick();
            }
        });

        Button btnUserData = (Button) view.findViewById(R.id.btn_user_info);
        btnUserData.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                mainPresenter.userInfoButtonClick();
            }
        });
    }

    @Override
    public void setUserName(String name) {
        ((TextView) getView().findViewById(R.id.et_user)).setText(name);
    }

    @Override
    public void showToast(String msg) {
        toastUtil.showToast(msg);
    }
}