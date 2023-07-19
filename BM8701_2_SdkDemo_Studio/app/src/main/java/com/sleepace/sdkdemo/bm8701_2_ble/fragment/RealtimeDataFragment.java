package com.sleepace.sdkdemo.bm8701_2_ble.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sleepace.sdk.bm8701_2_ble.domain.HistoryData;
import com.sleepace.sdk.bm8701_2_ble.domain.RealtimeData;
import com.sleepace.sdk.bm8701_2_ble.domain.SensorState;
import com.sleepace.sdk.bm8701_2_ble.interfs.HistoryDataListener;
import com.sleepace.sdk.bm8701_2_ble.interfs.RealtimeDataListener;
import com.sleepace.sdk.bm8701_2_ble.interfs.SensorStateListener;
import com.sleepace.sdk.interfs.IConnectionStateCallback;
import com.sleepace.sdk.interfs.IDeviceManager;
import com.sleepace.sdk.interfs.IResultCallback;
import com.sleepace.sdk.manager.CONNECTION_STATE;
import com.sleepace.sdk.manager.CallbackData;
import com.sleepace.sdk.manager.DeviceType;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdkdemo.bm8701_2_ble.MainActivity;
import com.sleepace.sdkdemo.bm8701_2_ble.R;
import com.sleepace.sdkdemo.bm8701_2_ble.bean.DeviceInfo;
import com.sleepace.sdkdemo.bm8701_2_ble.constants.SleepState;
import com.sleepace.sdkdemo.bm8701_2_ble.constants.SleepStatus;

import java.util.Calendar;
import java.util.List;

public class RealtimeDataFragment extends BaseFragment {
	private TextView connect_status_1,monitorConnect1,inBedStatus1,sleepStatus1,heart1,breath1 ,connect_status_2,monitorConnect2,inBedStatus2,sleepStatus2,heart2,breath2;
	private Button btn_sync_2,btn_sync_1;
	private TextView device1Tips,device2Tips;
	private DeviceInfo device1 = null, device2 = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(MainActivity.deviceInfos.size() == 1){
			device1 = MainActivity.deviceInfos.get(0);
		}else if(MainActivity.deviceInfos.size() == 2){
			device1 = MainActivity.deviceInfos.get(0);
			device2 = MainActivity.deviceInfos.get(1);
		}
		SdkLog.log(TAG+" onCreateView------device1:" + device1+",device2:" + device2);
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
		getDeviceHelper().addConnectionStateCallback(stateCallback);
		getDeviceHelper().addRealtimeDataListener(realtimeDataListener);
		getDeviceHelper().addHistoryDataListener(historyDataListener);
		getDeviceHelper().addOutOfBedSensorStateListener(outOfBedSensorStateListener);
		btn_sync_1.setOnClickListener(view->{
			SdkLog.log("同步设备1历史报告");
			if(device1 != null){
				Calendar calendar = Calendar.getInstance();
				int endTime = (int) (calendar.getTimeInMillis() / 1000);
				calendar.add(Calendar.DATE, -1);
				int startTime = (int) (calendar.getTimeInMillis() / 1000);
				SdkLog.log(TAG+ " historyDownload1 startTime:" + startTime+",endTime:" + endTime);
				getDeviceHelper().historyDownload(device1.getAddress(), device1.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte) 0,
						startTime, endTime, (byte) 0, new IResultCallback<List<HistoryData>>() {
							@Override
							public void onResultCallback(IDeviceManager manager, CallbackData<List<HistoryData>> cd) {
								SdkLog.log("historyDownload1 cd:" + cd);
							}
						});
			}
		});
		btn_sync_2.setOnClickListener(view->{
			SdkLog.log("同步设备2历史报告");
			if(device2 != null){
				Calendar calendar = Calendar.getInstance();
				int endTime = (int) (calendar.getTimeInMillis() / 1000);
				calendar.add(Calendar.DATE, -1);
				int startTime = (int) (calendar.getTimeInMillis() / 1000);
				SdkLog.log(TAG+ " historyDownload2 startTime:" + startTime+",endTime:" + endTime);
				getDeviceHelper().historyDownload(device2.getAddress(), device2.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte) 0,
						startTime, endTime, (byte) 0, new IResultCallback<List<HistoryData>>() {
							@Override
							public void onResultCallback(IDeviceManager manager, CallbackData<List<HistoryData>> cd) {
								SdkLog.log("historyDownload2 cd:" + cd);
							}
						});
			}
		});
	}

	private void realtimeDataStart(DeviceInfo device){
		SdkLog.log("realtimeDataStart device:"+device);
		if(device != null){
			getDeviceHelper().realtimeDataStart(device.getAddress(),device.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte)0,(manager,rd)->{
				SdkLog.log("realtimeDataStart:"+rd.getStatus());
			});
		}
	}

	private SensorStateListener outOfBedSensorStateListener = new SensorStateListener() {
		@Override
		public void onSensorStateChanged(final IDeviceManager manager, final SensorState state) {
			if(!isAdded()){
				return;
			}
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String stateStr = state.getState()==0?"未连接":state.getState()==1?"已连接":"未知";
					if(device1!=null && state.getDeviceId().equals(device1.getDeviceId())){
						monitorConnect1.setText(stateStr);
					}
					if(device2!=null && state.getDeviceId().equals(device2.getDeviceId())){
						monitorConnect2.setText(stateStr);
					}
				}
			});
		}
	};


	private void refreshTextView(TextView textView,String text){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(text);
			}
		});
	}

	protected void initUI() {
		if(device1!=null){
			boolean device1Connect = getDeviceHelper().isConnected(device1.getAddress());
			SdkLog.log(TAG+" initUI device1Connect:" + device1Connect);
			if(device1Connect){
				connect_status_1.setText(device1.getDeviceName());
				if(device1.getCommunityMode() != 1){ //通信模式不为BLE
					device1Tips.setVisibility(View.VISIBLE);
					device1Tips.setText("请先将设备通信模式修改为BLE");
				}
			}else{
				connect_status_1.setText("未连接");
				this.monitorConnect1.setText("--");
			}
			getDeviceHelper().outOfBedSensorStatesGet(device1.getAddress(),device1.getDeviceId(),DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(),(byte)0,(manager,cb)->{
				if(cb.isSuccess()){
					SensorState state =  cb.getResult();
					String stateStr = state.getState()==0?"未连接":state.getState()==1?"已连接":"未知";
					this.monitorConnect1.setText(stateStr);
				} else{
					SdkLog.log("outOfBedSensorStatesGet fail:"+cb.getStatus());
					this.monitorConnect1.setText("状态获取失败："+cb.getStatus());
				}
				realtimeDataStart(device1);
			});
		} else{
			connect_status_1.setText("未连接");
			this.monitorConnect1.setText("--");
		}

		heart1.setText("--");
		breath1.setText("--");
		this.sleepStatus1.setText("--");
		this.inBedStatus1.setText("--");

		if(device2!=null){
			boolean device2Connect = getDeviceHelper().isConnected(device2.getAddress());
			SdkLog.log(TAG+" initUI device2Connect:" + device2Connect);
			if(device2Connect){
				connect_status_2.setText(device2.getDeviceName());
				if(device1.getCommunityMode() != 1){ //通信模式不为BLE
					device1Tips.setVisibility(View.VISIBLE);
					device2Tips.setText("请先将设备通信模式修改为BLE");
				}
			}else{
				connect_status_2.setText("未连接");
				this.monitorConnect2.setText("--");
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
				realtimeDataStart(device2);
			});
		} else{
			connect_status_2.setText("未连接");
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
		getDeviceHelper().removeOutOfBedSensorStateListener(outOfBedSensorStateListener);
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
			if(device1!=null && manager.getAddress().equals(device1.getAddress())){
				initRealtimeDataView(realTimeData, heart1, breath1, inBedStatus1, sleepStatus1);
			}

			if(device2!=null && manager.getAddress().equals(device2.getAddress())){
				initRealtimeDataView(realTimeData, heart2, breath2, inBedStatus2, sleepStatus2);
			}
		}
	};


	private void initRealtimeDataView(RealtimeData realTimeData, TextView tvHeart, TextView tvBreath, TextView tvStatus, TextView tvState){
		String heartRate = "--";
		String breathRate = "--";
		String sleepState = "--";
		byte status = realTimeData.getStatus();
		if(status != SleepStatus.OUT_OF_BED){
			heartRate = String.valueOf(realTimeData.getHeartRate());
			breathRate = String.valueOf(realTimeData.getBreathRate());
			if(realTimeData.getSleepState()== SleepState.INIT){
				sleepState = "初始化";
			}else if(realTimeData.getSleepState()== SleepState.DEEP){
				sleepState = "深睡";
			}else if(realTimeData.getSleepState()== SleepState.LIGHT){
				sleepState = "浅睡";
			}else if(realTimeData.getSleepState()== SleepState.WAKE){
				sleepState = "清醒";
			}
		}

		String statusStr = status== SleepStatus.INIT ? "初始化" : status==SleepStatus.OUT_OF_BED ? "离床" : "在床";
		refreshTextView(tvStatus, statusStr);
		refreshTextView(tvState, sleepState);
		refreshTextView(tvHeart,heartRate);
		refreshTextView(tvBreath,breathRate);
	}

	private IConnectionStateCallback stateCallback = new IConnectionStateCallback() {
		@Override
		public void onStateChanged(IDeviceManager manager, final CONNECTION_STATE state) {
			if (state == CONNECTION_STATE.CONNECTED) {
				if (device1!=null && manager.getAddress().equals(device1.getAddress())) {
					refreshTextView(connect_status_1, device1.getDeviceName());
				}
				if (device2!=null && manager.getAddress().equals(device2.getAddress())) {
					refreshTextView(connect_status_2, device2.getDeviceName());
				}
				if(device1!=null){
					getDeviceHelper().realtimeDataStart(device1.getAddress(),device1.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte)0,(mgr,rd)->{
						SdkLog.log("state cb d1 realtimeDataStart:"+rd.getStatus());
					});
				}
				if(device2!=null){
					getDeviceHelper().realtimeDataStart(device2.getAddress(),device2.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte)0,(mgr,rd)->{
						SdkLog.log("state cb d2 realtimeDataStart:"+rd.getStatus());
					});
				}
			} else if(state == CONNECTION_STATE.DISCONNECT){
				if (device1!=null && manager.getAddress().equals(device1.getAddress())) {
					refreshTextView(connect_status_1, "未连接");
				}
				if (device2!=null && manager.getAddress().equals(device2.getAddress())) {
					refreshTextView(connect_status_2, "未连接");
				}
			}
		}
	};
	
	private static HistoryData historyData;

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
}











