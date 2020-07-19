package com.kuaishan.obtainmsg.ui.dashboard;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.account.LoginActivity;
import com.kuaishan.obtainmsg.core.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private View logout, about, contract, privacy;
    private TextView mainAccount, loginTv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//            }
//        });
        mainAccount = root.findViewById(R.id.main_account);
        loginTv = root.findViewById(R.id.tv_login_reg);
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(getActivity());
            }
        });

        logout = root.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.logOut(getActivity());
                getActivity().finish();
            }
        });
        about = root.findViewById(R.id.btn_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(getActivity(), "about");
            }
        });
        privacy = root.findViewById(R.id.btn_privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPrivicyDialog(getActivity(),false);
            }
        });
        contract = root.findViewById(R.id.btn_contract);
        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(getActivity(), "联系我们");
                FeedbackAPI.openFeedbackActivity();
            }
        });
        return root;
    }


    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = getActivity().getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
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
