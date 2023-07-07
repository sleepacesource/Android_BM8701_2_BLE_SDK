package com.sleepace.sdkdemo.bm8701_2_ble.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdkdemo.bm8701_2_ble.R;
import com.sleepace.sdkdemo.bm8701_2_ble.bean.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class DeviceFrameFragment extends BaseFragment {


    private TabLayout tabLayout;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String>  mTitle = new ArrayList<>();

    public static int currentDevice=0;


    ViewPager viewPager = null;
    private List<DeviceInfo> list ;

    public DeviceFrameFragment(List<DeviceInfo> deviceInfos) {
        this.list = deviceInfos;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(fragmentList.size() == 0){
            fragmentList.add(new DeviceFragment(0));
            fragmentList.add(new DeviceFragment(1));
            mTitle.add("设备1");
            mTitle.add("设备2");

        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中某个tab
                currentDevice = tab.getPosition();
                SdkLog.log(TAG+" selected tips-------------"+tab.getText()+","+currentDevice);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //当tab从选择到未选择

                SdkLog.log(TAG+" unSelected tips-------------"+tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //已经选中tab后的重复点击tab

                SdkLog.log(TAG+" reSelected tips-------------"+tab.getText());

            }
        });
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view =  inflater.inflate(R.layout.fragment_device_frame, null);
        SdkLog.log(TAG+" -------onCreateView------"+this);
         _initView(view);
        initListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SdkLog.log(TAG+" -------onDestroyView------");
    }

    private void _initView(View root){
        tabLayout = root.findViewById(R.id.device_tab);

        ViewPager viewPager = root.findViewById(R.id.viewPager);
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), fragmentList, mTitle);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
        SdkLog.log(TAG+" -------_initView------");

    }


    public class TabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments;
        private final List<String> titles;
        public TabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            SdkLog.log(TAG+" -------TabAdapter------");
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            SdkLog.log(TAG+" -------getItem------"+position);
            return fragmentList.get(position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }
}