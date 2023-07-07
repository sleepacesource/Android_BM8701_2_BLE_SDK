package com.sleepace.sdkdemo.bm8701_2_ble.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sleepace.sdk.bm8701_2_ble.BM8701DeviceManager;
import com.sleepace.sdk.bm8701_2_ble.BM8701Helper;
import com.sleepace.sdk.bm8701_2_ble.domain.HistoryData;
import com.sleepace.sdk.bm8701_2_ble.domain.RealtimeData;
import com.sleepace.sdk.bm8701_2_ble.domain.SensorState;
import com.sleepace.sdk.bm8701_2_ble.interfs.HistoryDataListener;
import com.sleepace.sdk.bm8701_2_ble.interfs.RealtimeDataListener;
import com.sleepace.sdk.interfs.IConnectionStateCallback;
import com.sleepace.sdk.interfs.IDeviceManager;
import com.sleepace.sdk.interfs.IResultCallback;
import com.sleepace.sdk.manager.CONNECTION_STATE;
import com.sleepace.sdk.manager.CallbackData;
import com.sleepace.sdk.manager.DeviceType;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdk.util.StringUtil;
import com.sleepace.sdkdemo.bm8701_2_ble.MainActivity;
import com.sleepace.sdkdemo.bm8701_2_ble.R;
import com.sleepace.sdkdemo.bm8701_2_ble.bean.DeviceInfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RealtimeDataFragment extends BaseFragment {

	TextView connect_status_1,monitorConnect1,inBedStatus1,sleepStatus1,heart1,breath1 ,connect_status_2,monitorConnect2,inBedStatus2,sleepStatus2,heart2,breath2;
	Button btn_sync_2,btn_sync_1;

	TextView device1Tips,device2Tips;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_realtime, null);
		findView(view);
		initListener();
		initUI();
		return view;
	}

	protected void findView(View root) {
		super.findView(root);
		monitorConnect1 = root.findViewById(R.id.tv_monitor_connect_1);
		inBedStatus1 = root.findViewById(R.id.tv_inbedstatus_1);
		sleepStatus1 = root.findViewById(R.id.tv_sleepStatus_1);

		monitorConnect2 = root.findViewById(R.id.tv_monitor_connect_2);
		inBedStatus2 = root.findViewById(R.id.tv_inbedstatus_2);
		sleepStatus2 = root.findViewById(R.id.tv_sleepStatus_2);

		heart1 = root.findViewById(R.id.tv_heart_1);
		heart2 = root.findViewById(R.id.tv_heart_2);
		breath1 = root.findViewById(R.id.tv_breath_1);
		breath2 = root.findViewById(R.id.tv_breath_2);
		connect_status_1 = root.findViewById(R.id.connect_status_1);
		connect_status_2 = root.findViewById(R.id.connect_status_2);
		btn_sync_1 = root.findViewById(R.id.btn_sync_1);
		btn_sync_2 = root.findViewById(R.id.btn_sync_2);
		device1Tips = root.findViewById(R.id.device1_tip);
		device2Tips = root.findViewById(R.id.device2_tip);


	}






	protected void initListener() {
		super.initListener();
		DeviceInfo device1 = MainActivity.deviceInfos.get(0);
		DeviceInfo device2 = MainActivity.deviceInfos.get(1);
		getDeviceHelper().addConnectionStateCallback(stateCallback);
		getDeviceHelper().addRealtimeDataListener(realtimeDataListener);
		if(getDeviceHelper().isConnected(device1.getAddress())){
			getDeviceHelper().realtimeDataStart(device1.getAddress(),device1.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte)0,(manager,rd)->{
				SdkLog.log("realtimeDataStart:"+rd.getStatus());
			});
		}
		getDeviceHelper().addHistoryDataListener(historyDataListener);
		if(getDeviceHelper().isConnected(device2.getAddress())){
			getDeviceHelper().realtimeDataStart(device2.getAddress(),device2.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte)0,(manager,rd)->{
				SdkLog.log("realtimeDataStart:"+rd.getStatus());
			});
		}






		btn_sync_1.setOnClickListener(view->{
			SdkLog.log("同步设备1历史报告");
		});

	}


	private void refreshTextView(TextView textView,String text){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(text);
			}
		});
	}

	protected void initUI() {
		DeviceInfo device1 = MainActivity.deviceInfos.get(0);
		if(getDeviceHelper().isConnected(device1.getAddress())){
			connect_status_1.setText(device1.getDeviceName());
			if(device1.getCommunityMode() != 1){ //通信模式不为BLE
				device1Tips.setVisibility(View.VISIBLE);
				device1Tips.setText("请先将设备通信模式修改为BLE");
			}
			getDeviceHelper().outOfBedSensorStatesGet(MainActivity.deviceInfos.get(0).getAddress(),device1.getDeviceId(),DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(),(byte)0,(manager,cb)->{
				if(cb.isSuccess()){
					SensorState state =  cb.getResult();
					String stateStr = state.getState()==0?"未连接":state.getState()==1?"已连接":"未知";
					this.monitorConnect1.setText(stateStr);
				} else{
					SdkLog.log("outOfBedSensorStatesGet fail:"+cb.getStatus());
					this.monitorConnect1.setText("状态获取失败："+cb.getStatus());
				}
			});
		} else{
			this.monitorConnect1.setText("--");
		}
		heart1.setText("--");
		breath1.setText("--");
		this.sleepStatus1.setText("--");
		this.inBedStatus1.setText("--");


		DeviceInfo device2 = MainActivity.deviceInfos.get(1);
		if(getDeviceHelper().isConnected(device2.getAddress())){
			connect_status_2.setText(device2.getDeviceName());
			if(device1.getCommunityMode() != 1){ //通信模式不为BLE
				device1Tips.setVisibility(View.VISIBLE);
				device2Tips.setText("请先将设备通信模式修改为BLE");
			}
			getDeviceHelper().outOfBedSensorStatesGet(device2.getAddress(),device2.getDeviceId(),DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(),(byte)0,(manager,cb)->{
				if(cb.isSuccess()){
					SensorState state =  cb.getResult();
					String stateStr = state.getState()==0?"未连接":state.getState()==1?"已连接":"未知";
					this.monitorConnect2.setText(stateStr);
				} else{
					SdkLog.log("outOfBedSensorStatesGet fail:"+cb.getStatus());
					this.monitorConnect2.setText("状态获取失败："+cb.getStatus());
				}
			});
		} else{
			this.monitorConnect2.setText("--");
		}
		heart2.setText("--");
		breath2.setText("--");
		this.sleepStatus2.setText("--");
		this.inBedStatus2.setText("--");
	}



	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SdkLog.log(TAG + " onDestroyView----------------");
		getDeviceHelper().removeConnectionStateCallback(stateCallback);
		getDeviceHelper().removeRealtimeDataListener(realtimeDataListener);
		getDeviceHelper().removeHistoryDataListener(historyDataListener);
	}

	private HistoryDataListener historyDataListener = new HistoryDataListener(){
		@Override
		public void onHistoryData(IDeviceManager manager, HistoryData historyData) {
			SdkLog.log("History upload:"+historyData.getDeviceId()+","+historyData);
		}
	};

	private RealtimeDataListener realtimeDataListener = new RealtimeDataListener(){

		@Override
		public void onRealtimeData(IDeviceManager manager, RealtimeData realTimeData) {
			{
				DeviceInfo device1 = MainActivity.deviceInfos.get(0);
				DeviceInfo device2 = MainActivity.deviceInfos.get(1);
				if(manager.getAddress().equals(device1.getAddress())){
					refreshTextView(heart1,realTimeData.getHeartRate()+"");
					refreshTextView(breath1,realTimeData.getBreathRate()+"");
					byte status = realTimeData.getStatus();
					String statusStr = status==0XFF?"初始化":status==0x00?"离床":"在床";
					refreshTextView(RealtimeDataFragment.this.inBedStatus1,statusStr);
					byte sleepState = realTimeData.getSleepState();
					String sleepStr = sleepState==0XFF?"初始化":sleepState==0xE0?"深睡":sleepState==0xE2?"浅睡":"清醒";
					refreshTextView(RealtimeDataFragment.this.sleepStatus1,sleepStr);
				}
				if(manager.getAddress().equals(device2.getAddress())){
					refreshTextView(heart2,realTimeData.getHeartRate()+"");
					refreshTextView(breath2,realTimeData.getBreathRate()+"");

					byte status = realTimeData.getStatus();
					String statusStr = status==0XFF?"初始化":status==0x00?"离床":"在床";
					refreshTextView(RealtimeDataFragment.this.inBedStatus2,statusStr);
					byte sleepState = realTimeData.getSleepState();
					String sleepStr = sleepState==0XFF?"初始化":sleepState==0xE0?"深睡":sleepState==0xE2?"浅睡":"清醒";
					refreshTextView(RealtimeDataFragment.this.sleepStatus2,sleepStr);
				}
			}
		}
	};
	private IConnectionStateCallback stateCallback = new IConnectionStateCallback() {
		@Override
		public void onStateChanged(IDeviceManager manager, final CONNECTION_STATE state) {
			DeviceInfo device1 = MainActivity.deviceInfos.get(0);
			DeviceInfo device2 = MainActivity.deviceInfos.get(1);
			if(state.getValue() == CONNECTION_STATE.CONNECTED.getValue()){
					if(manager.getAddress().equals(device1.getAddress())){
						refreshTextView(connect_status_1,device1.getDeviceName());
					}
					if(manager.getAddress().equals(device2.getAddress())){
						refreshTextView(connect_status_2,device2.getDeviceName());
					}
				} else{
					connect_status_1.setText("未连接");
				}
		}
	};
	
	private static HistoryData historyData;

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
}











