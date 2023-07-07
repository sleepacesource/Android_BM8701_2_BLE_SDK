package com.sleepace.sdkdemo.bm8701_2_ble.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sleepace.sdk.bm8701_2_ble.domain.HistoryData;
import com.sleepace.sdk.domain.BleDevice;
import com.sleepace.sdk.interfs.IConnectionStateCallback;
import com.sleepace.sdk.interfs.IDeviceManager;
import com.sleepace.sdk.interfs.IResultCallback;
import com.sleepace.sdk.manager.CONNECTION_STATE;
import com.sleepace.sdk.manager.CallbackData;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdkdemo.bm8701_2_ble.DemoApp;
import com.sleepace.sdkdemo.bm8701_2_ble.MainActivity;
import com.sleepace.sdkdemo.bm8701_2_ble.R;
import com.sleepace.sdkdemo.bm8701_2_ble.ReportActivity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class HistoryDataFragment extends BaseFragment {

	private Button btnDemo, btnAbortHistoryDownload, btnAbortGetLast24HourData;
	private LinearLayout layoutSync;
	private ProgressDialog progressDialog;
	
	//10 days, Including start time and end time
	private String startTime1 = "2018-11-15 00:00:00", endTime1 = "2018-11-24 23:59:59";
	
	//10 days, Including start time and end time
	private String startTime2 = "2018-11-26 00:00:00", endTime2 = "2018-12-05 23:59:59";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_data, null);
		// SdkLog.log(TAG + " onCreateView-----------");
		findView(view);
		initListener();
		initUI();
		return view;
	}

	protected void findView(View root) {
		// TODO Auto-generated method stub
		super.findView(root);
		layoutSync = root.findViewById(R.id.layout_sync);
		btnAbortHistoryDownload = (Button) root.findViewById(R.id.btn_abortHistoryDownload);
		btnAbortGetLast24HourData = (Button) root.findViewById(R.id.btn_abortGetLast24HourData);
		btnDemo = (Button) root.findViewById(R.id.btn_demo_data);
	}

	protected void initListener() {
		// TODO Auto-generated method stub
		super.initListener();
		getDeviceHelper().addConnectionStateCallback(stateCallback);
		btnAbortHistoryDownload.setOnClickListener(this);
		btnAbortGetLast24HourData.setOnClickListener(this);
		btnDemo.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		boolean enable = getDeviceHelper().isConnected();
//		setBtnEnable(enable);
	}
	
	private void setBtnEnable(boolean enable) {
		
	}

	protected void initUI() {
		// TODO Auto-generated method stub
		mActivity.setTitle(R.string.history_data);
		btnAbortHistoryDownload.setText("AbortHistoryDownload");
		btnAbortGetLast24HourData.setText("AbortGetLast24HourData");
		
		progressDialog = new ProgressDialog(mActivity);
		progressDialog.setIcon(android.R.drawable.ic_dialog_info);
		progressDialog.setTitle(R.string.history_data);
		progressDialog.setMessage(getString(R.string.syncing));
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		initBtnSync();
	}
	
	
	private void initBtnSync() {
		layoutSync.removeAllViews();
		for(int i = 0; i< MainActivity.getDeviceList().size(); i++) {
			final BleDevice device = MainActivity.getDeviceList().get(i);
			final View itemView = LayoutInflater.from(mActivity).inflate(R.layout.layout_btn_sync, null);
			TextView tv = itemView.findViewById(R.id.tv_device);
			tv.setText(getString(R.string.device) + (MainActivity.getDeviceList().size() - i) + " " + device.getDeviceName());
			final Button btn = itemView.findViewById(R.id.btn_sync);
//			if(getDeviceHelper().isConnected(device.getAddress())) {
//				btn.setEnabled(true);
//			}else {
//				btn.setEnabled(false);
//			}
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int endTime = (int) (System.currentTimeMillis() / 1000);
					downloadData(device.getAddress(), 0, endTime);
				}
			});
			layoutSync.addView(itemView, 0);
		}
	}

	private IConnectionStateCallback stateCallback = new IConnectionStateCallback() {
		@Override
		public void onStateChanged(IDeviceManager manager, final CONNECTION_STATE state) {
			// TODO Auto-generated method stub
			if (!isAdded()) {
				return;
			}

			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(state == CONNECTION_STATE.CONNECTED) {
						setBtnEnable(true);
					}else if(state == CONNECTION_STATE.DISCONNECT) {
						setBtnEnable(false);
					}
				}
			});
		}
	};

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getDeviceHelper().removeConnectionStateCallback(stateCallback);
	}
	
	private void downloadData(String address, int startTime, int endTime) {
		progressDialog.show();
		getDeviceHelper().historyDownload(address, startTime, endTime, DemoApp.USER_SEX, new IResultCallback<List<HistoryData>>() {
			@Override
			public void onResultCallback(final IDeviceManager manager, final CallbackData<List<HistoryData>> cd) {
				// TODO Auto-generated method stub
				if (!isAdded()) {
					return;
				}
				
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						if (cd.isSuccess()) {
							List<HistoryData> list = cd.getResult();
							final int size = list == null ? 0 : list.size();
							SdkLog.log(TAG + " historyDownload size:" + size+",list:" + list);
							if (size > 0) {
								HistoryData historyData = list.get(0);
//								SdkLog.log(TAG + " historyDownload first data:" + historyData);
								Intent intent = new Intent(mActivity, ReportActivity.class);
								intent.putExtra(ReportActivity.EXTRA_REPORT_TYPE, ReportActivity.REPORT_TYPE_HISOTRY);
								intent.putExtra(ReportActivity.EXTRA_DATA, historyData);
								startActivity(intent);
							}else {
//								Toast.makeText(mActivity, R.string.tips_no_report, Toast.LENGTH_SHORT).show();
								showTips(R.string.tips_no_report);
							}
						}else {
//							Toast.makeText(mActivity, R.string.sync_falied, Toast.LENGTH_SHORT).show();
							showTips(R.string.sync_falied);
						}
					}
				});
			}
		});
	}

	private void showTips(int msgRes){
		String msg = getString(msgRes);
		showTips(msg);
	}

	private void showTips(String msg){
		new AlertDialog.Builder(mActivity)
				.setMessage(msg)
				.setNegativeButton(android.R.string.ok, null)
				.create().show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v == btnAbortGetLast24HourData) {
			
		}else if(v == btnAbortHistoryDownload) {
			
		}/*else if (v == btnSync) {
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.HOUR_OF_DAY, 0);
//			cal.set(Calendar.MINUTE, 0);
//			cal.set(Calendar.SECOND, 0);
//			int endTime = (int) (cal.getTimeInMillis() / 1000);
//			cal.add(Calendar.DATE, -1);
			// cal.set(Calendar.HOUR_OF_DAY, 20);
			// cal.set(Calendar.MINUTE, 0);
			// cal.set(Calendar.SECOND, 0);
			int endTime = (int) (System.currentTimeMillis() / 1000);
			downloadData(0, endTime);
		}else if(v == btn1115) {
			try {
				Date startDate = dateFormat.parse(startTime1);
				Date endDate = dateFormat.parse(endTime1);
				int startTime = (int) (startDate.getTime() / 1000);
				int endTime = (int) (endDate.getTime() / 1000);
				downloadData(startTime, endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(v == btn1126) {
			try {
				Date sDate = dateFormat.parse(startTime2);
				Date eDate = dateFormat.parse(endTime2);
				int startTime = (int) (sDate.getTime() / 1000);
				int endTime = (int) (eDate.getTime() / 1000);
				downloadData(startTime, endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} */else if (v == btnDemo) {

		}
	}

}
