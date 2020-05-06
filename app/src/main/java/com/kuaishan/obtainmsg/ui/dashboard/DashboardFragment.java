package com.kuaishan.obtainmsg.ui.dashboard;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.account.LoginActivity;
import com.kuaishan.obtainmsg.core.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Button logout, login, about, contract, privacy;
    private TextView mainAccount,loginTv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        mainAccount = root.findViewById(R.id.main_account);
        loginTv = root.findViewById(R.id.tv_login_reg);
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(getActivity());
            }
        });

        login = root.findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    LoginActivity.start(getActivity());
                }
            }
        });

        logout = root.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.logOut(getActivity());
            }
        });
        about = root.findViewById(R.id.btn_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginActivity.logOut(getActivity());
                Utils.toast(getActivity(),"about");
            }
        });
        privacy = root.findViewById(R.id.btn_privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.toast(getActivity(),"about privacy");
//                LoginActivity.logOut(getActivity());
            }
        });
        contract = root.findViewById(R.id.btn_contract);
        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginActivity.logOut(getActivity());
                Utils.toast(getActivity(),"联系我们");
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        String mainAccountStr = Utils.getPhone(getActivity());
        if (TextUtils.isEmpty(mainAccountStr)) {
            mainAccount.setVisibility(View.GONE);
            loginTv.setVisibility(View.VISIBLE);

        } else {
            loginTv.setVisibility(View.GONE);
            mainAccount.setText(mainAccountStr);
        }
    }
}
