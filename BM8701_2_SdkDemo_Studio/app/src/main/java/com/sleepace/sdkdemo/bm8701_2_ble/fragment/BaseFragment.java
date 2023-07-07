package com.sleepace.sdkdemo.bm8701_2_ble.fragment;

import com.sleepace.sdk.bm8701_2_ble.BM8701Helper;
import com.sleepace.sdkdemo.bm8701_2_ble.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements OnClickListener{
	
	protected String TAG = getClass().getSimpleName();
	protected MainActivity mActivity;
	private BM8701Helper mHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivity = (MainActivity) getActivity();
		mHelper = BM8701Helper.getInstance(mActivity);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public BM8701Helper getDeviceHelper() {
		return mHelper;
	}

	protected void findView(View root) {
		// TODO Auto-generated method stub
	}


	protected void initListener() {
		// TODO Auto-generated method stub
	}

	protected void initUI() {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}



