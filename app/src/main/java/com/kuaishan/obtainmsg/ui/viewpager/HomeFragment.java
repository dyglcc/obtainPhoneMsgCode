package com.kuaishan.obtainmsg.ui.viewpager;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.banner.BannerLayout;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.ui.adapter.WebBannerAdapter;
import com.kuaishan.obtainmsg.ui.home.HomeViewModel;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar.Gravity;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends LazyFragment {

    private HomeViewModel homeViewModel;
    private Button logout;
    private String[] tabNames = new String[]{"我的获取", "我的共享"};

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    public static final String INTENT_STRING_TABNAME = "intent_String_tabname";
    public static final String INTENT_INT_INDEX = "intent_int_index";
    private int index;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_home);

        //----------init homeFragment
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
//        final TextView textView = findViewById(R.id.text_home);
        final BannerLayout recyclerBanner = findViewById(R.id.recycler);
        List<String> list = new ArrayList<>();
        list.add("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=3967183915,4078698000&fm=27&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=3184221534,2238244948&fm=27&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1794621527,1964098559&fm=27&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg");
        WebBannerAdapter webBannerAdapter = new WebBannerAdapter(getActivity(), list);
        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "点击了第  " + position + "  项", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerBanner.setAdapter(webBannerAdapter);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//            }
//        });
        //----------init homeFragment
        Resources res = getResources();

//		Bundle bundle = getArguments();
        index = 0;
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragment_tabmain_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.fragment_tabmain_indicator);

        switch (index) {
            case 0:
                indicator.setScrollBar(new ColorBar(getApplicationContext(), Color.RED, 5));
                break;
            case 1:
                indicator.setScrollBar(new ColorBar(getApplicationContext(), Color.RED, 0,
                        Gravity.CENTENT_BACKGROUND));
                break;
            case 2:
                indicator.setScrollBar(new ColorBar(getApplicationContext(), Color.RED, 5,
                        Gravity.TOP));
                break;
            case 3:
                indicator.setScrollBar(new LayoutBar(getApplicationContext(),
                        R.layout.layout_slidebar, Gravity.CENTENT_BACKGROUND));
                break;
        }
        float unSelectSize = 16;
        float selectSize = unSelectSize * 1.2f;

        int selectColor = res.getColor(R.color.tab_top_text_2);
        int unSelectColor = res.getColor(R.color.tab_top_text_1);
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor,
                unSelectColor).setSize(selectSize, unSelectSize));

        viewPager.setOffscreenPageLimit(4);

        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());

        // 注意这里 的FragmentManager 是 getChildFragmentManager(); 因为是在Fragment里面
        // 而在activity里面用FragmentManager 是 getSupportFragmentManager()
        indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        Log.d("cccc", "Fragment 将要创建View " + this);

    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        Log.d("cccc", "Fragment所在的Activity onResume, onResumeLazy " + this);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        Log.d("cccc", "Fragment 显示 " + this);
    }

    @Override
    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        Log.d("cccc", "Fragment 掩藏 " + this);
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        Log.d("cccc", "Fragment所在的Activity onPause, onPauseLazy " + this);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        Log.d("cccc", "Fragment View将被销毁 " + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("cccc", "Fragment 所在的Activity onDestroy " + this);
    }

    private class MyAdapter extends IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new MyObtainFragment();
            } else if (position == 1) {
                fragment  = new MyObtainFragment();
            }
            Bundle bundle = new Bundle();
            bundle.putString(MyObtainFragment.INTENT_STRING_TABNAME, tabNames[position]);
            bundle.putInt(MyObtainFragment.INTENT_INT_POSITION, position);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

}