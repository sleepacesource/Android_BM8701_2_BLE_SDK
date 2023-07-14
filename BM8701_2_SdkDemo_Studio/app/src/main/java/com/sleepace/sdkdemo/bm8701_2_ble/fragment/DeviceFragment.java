package com.sleepace.sdkdemo.bm8701_2_ble.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.binatonesdk.demo.util.BleUtil;
import com.sleepace.sdk.bm8701_2_ble.BM8701DeviceManager;
import com.sleepace.sdk.bm8701_2_ble.BM8701Helper;
import com.sleepace.sdk.bm8701_2_ble.domain.DataUploadConfig;
import com.sleepace.sdk.bm8701_2_ble.domain.RealtimeDataInterval;
import com.sleepace.sdk.bm8701_2_ble.domain.SleepConfig;
import com.sleepace.sdk.domain.BleDevice;
import com.sleepace.sdk.interfs.IConnectionStateCallback;
import com.sleepace.sdk.interfs.IDeviceManager;
import com.sleepace.sdk.interfs.IResultCallback;
import com.sleepace.sdk.manager.CONNECTION_STATE;
import com.sleepace.sdk.manager.CallbackData;
import com.sleepace.sdk.manager.DeviceType;
import com.sleepace.sdk.manager.ble.BleConfig;
import com.sleepace.sdk.manager.ble.BleHelperX;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdkdemo.bm8701_2_ble.AlarmSettingActivity;
import com.sleepace.sdkdemo.bm8701_2_ble.DemoApp;
import com.sleepace.sdkdemo.bm8701_2_ble.MainActivity;
import com.sleepace.sdkdemo.bm8701_2_ble.R;
import com.sleepace.sdkdemo.bm8701_2_ble.SearchBleDeviceActivity;
import com.sleepace.sdkdemo.bm8701_2_ble.bean.DeviceInfo;
import com.sleepace.sdkdemo.bm8701_2_ble.view.wheelview.KVWhellAdapter;
import com.sleepace.sdkdemo.bm8701_2_ble.view.wheelview.OnItemSelectedListener;
import com.sleepace.sdkdemo.bm8701_2_ble.view.wheelview.StringWhellAdapter;
import com.sleepace.sdkdemo.bm8701_2_ble.view.wheelview.WheelView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.telecom.Call;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class DeviceFragment extends BaseFragment {
	private LayoutInflater inflater;

	private  DeviceInfo device;
	private EditText etUserId;
	private View vUserDeviceTips, set_trans_model, set_realtime_interval,set_report_time,set_bed_config;

	private Button btnConnectDevice, btnMac, btnUpgrade;
	private TextView tvDeviceName, tvDeviceId,  tvVersion;

	private TextView tv_trans_model, tv_realtime_interval,tv_report_time,tv_bed_config,tv_bed_material;

	private Map<Integer,String> material = new HashMap<Integer,String>(){
		{
			this.put(0,"默认");
			this.put(1,"海绵");
			this.put(2,"弹簧");
			this.put(3,"乳胶");
			this.put(4,"气垫");
			this.put(5,"其他");
		}
	};
	private Map<Integer,String> thickness = new HashMap<Integer,String>(){
		{
			this.put(0,"默认");
			this.put(1,"5-10cm");
			this.put(2,"11-20cm");
			this.put(3,"21-30cm");
			this.put(4,"13.5cm");
		}
	};

	private Map<Integer,String> modes = new HashMap<Integer,String>(){{
		this.put(0,"Wi-Fi");
		this.put(1,"BLE");
	}
	};

	private boolean upgrading = false;
	//private LinearLayout deviceListLayout;
	private int deviceIndex;

	public DeviceFragment (int deviceIndex){
		this.deviceIndex = deviceIndex;
		device = MainActivity.deviceInfos.get(deviceIndex);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View root = inflater.inflate(R.layout.fragment_device, null);
		 SdkLog.log(TAG+" onCreateView-----------");
		findView(root);
		initListener();
		initUI();
		return root;
	}

	protected void findView(View root) {
		// TODO Auto-generated method stub
		super.findView(root);
		inflater = getLayoutInflater();

		//deviceListLayout = root.findViewById(R.id.layout_device_list);
		etUserId = root.findViewById(R.id.et_userid);
		tvDeviceName =  root.findViewById(R.id.tv_device_name);
		tvDeviceId =  root.findViewById(R.id.tv_device_id);
		tvVersion =  root.findViewById(R.id.tv_device_version);
		btnConnectDevice =  root.findViewById(R.id.btn_connect_device);
//		btnDeviceId = (Button) root.findViewById(R.id.btn_get_device_id);
		btnMac =  root.findViewById(R.id.btn_get_mac);
		//btnVersion = (Button) root.findViewById(R.id.btn_device_version);
		btnUpgrade = root.findViewById(R.id.btn_upgrade_fireware);
		tv_trans_model = root.findViewById(R.id.tv_trans_model);
		tv_realtime_interval = root.findViewById(R.id.tv_realtime_interval);
		tv_report_time =  root.findViewById(R.id.tv_report_time);
		tv_bed_config =  root.findViewById(R.id.tv_bed_config);
		set_trans_model =  root.findViewById(R.id.set_trans_model);
		set_realtime_interval =  root.findViewById(R.id.set_realtime_interval);
		set_report_time = root.findViewById(R.id.set_report_time);
		set_bed_config = root.findViewById(R.id.set_bed_config);
		//set_bed_material =  root.findViewById(R.id.set_bed_material);


	}

	protected void initListener() {
		super.initListener();
		getDeviceHelper().addConnectionStateCallback(stateCallback);
		btnConnectDevice.setOnClickListener(this);
		btnUpgrade.setOnClickListener(this);
		this.set_trans_model.setOnClickListener(this);
		this.set_realtime_interval.setOnClickListener(this);
		this.set_report_time.setOnClickListener(this);
		this.set_bed_config.setOnClickListener(this);
	}

	protected void initUI() {
		mActivity.setTitle(R.string.device);
		etUserId.setText("1");
		initDeviceName();
		initDeviceId();
		initVersion();
		this.initCommunityMode();
		this.initBedInfo();
		this.initRealtimeInterval();
		this.initReportTime();
	}

	@Override
	public void onResume() {
		super.onResume();
		/*if(deviceIndex != DeviceFrameFragment.currentDevice){//页面可见再查设备信息
			return;
		}*/
		boolean connected = getDeviceHelper().isConnected(device.getAddress());
		SdkLog.log(TAG + " onResume------------:"+deviceIndex+",visible:"+isVisible()+",connected:" + connected);
		if(connected){//已连接设备
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 14);
			calendar.set(Calendar.MINUTE, 58);
			int timestamp = (int) (calendar.getTimeInMillis() / 1000);
			getDeviceHelper().syncTime(device.getAddress(), timestamp,28800, (byte)0, (byte)0, resultCallback);
			initBtnState(true);
		}
	}

	private void initBtnState(final boolean connected){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(connected){
					btnConnectDevice.setText(R.string.disconnect);
				}else{
					btnConnectDevice.setText(R.string.connect_device);
				}
			}
		});
	}

	private IResultCallback resultCallback = new IResultCallback() {
		@Override
		public void onResultCallback(IDeviceManager manager, final CallbackData cd) {
			if(cd.getCallbackType() == BM8701DeviceManager.CallbackType.SYNC_TIME){
				if(cd.isSuccess()){
					SdkLog.log("syncTime success");
				}else {
					toast("同步时间失败："+cd.getStatus());
					SdkLog.log("syncTime fail:"+cd.getStatus());
				}

				getDeviceHelper().getDeviceInfo(device.getAddress(), this);
			}else if(cd.getCallbackType() == BM8701DeviceManager.CallbackType.DEVICE_INFO_GET){
				if(cd.isSuccess()){
					SdkLog.log("Get device info suc", cd);
					com.sleepace.sdk.bm8701_2_ble.domain.DeviceInfo deviceInfo = (com.sleepace.sdk.bm8701_2_ble.domain.DeviceInfo) cd.getResult();
					device.setDeviceId(deviceInfo.getDeviceId());
					device.setVersion(deviceInfo.getFirmwareVersion());
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initDeviceName();
							initDeviceId();
							initVersion();
						}
					});
				} else{
					SdkLog.log("Get device info fail:",cd.getStatus());
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							toast("获取设备信息失败："+cd.getStatus());
						}
					});
				}

				getDeviceHelper().bleWiFiModeGet(device.getAddress(),this);
			}else if(cd.getCallbackType() == BM8701DeviceManager.CallbackType.BLE_WIFI_MODE_GET){
//				SdkLog.log("bleWiFiModeGet--------", cd);
				if(cd.isSuccess()){
					Byte b = (Byte) cd.getResult();
					device.setCommunityMode(b);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initCommunityMode();
						}
					});
				} else{
					SdkLog.log("bleWiFiModeGet fail:",cd.getStatus());
					toast("获取通信模式失败："+cd.getStatus());
				}

				getDeviceHelper().realtimeDataIntervalGet(device.getAddress(), this);
			}else if(cd.getCallbackType() == BM8701DeviceManager.CallbackType.REALTIME_DATA_INTERVAL_GET){
//				SdkLog.log("realtimeDataIntervalGet--------", cd);
				if(cd.isSuccess()){
					RealtimeDataInterval b = (RealtimeDataInterval) cd.getResult();
					device.setRealtimeInterval(b.getInterval());
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initRealtimeInterval();
						}
					});
				} else{
					SdkLog.log("realtimeDataIntervalGet fail:",cd.getStatus());
					toast("获取实时数据上报间隔失败："+cd.getStatus());
				}

				getDeviceHelper().sleepConfigGet(device.getAddress(), this);
			}else if(cd.getCallbackType() == BM8701DeviceManager.CallbackType.SLEEP_CONFIG_GET){
				if(cd.isSuccess()){
					SleepConfig config = (SleepConfig) cd.getResult();
					int materialV = config.getMattressMaterial();
					int thicknessV = config.getMattressThickness();
					device.setMaterial(materialV);
					device.setBedThickness(thicknessV);
					SdkLog.log("sleepConfigGet suc:", cd);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initBedInfo();
						}
					});
				} else{
					SdkLog.log("sleepConfigGet fail:",cd.getStatus());
					toast("获取床垫信息失败："+cd.getStatus());
				}

				BM8701Helper.getInstance(DeviceFragment.this.getContext()).dataUploadConfigGet(device.getAddress(), this);
			}else if(cd.getCallbackType() == BM8701DeviceManager.CallbackType.DATA_UPLOAD_CONFIG_GET){
//				SdkLog.log("dataUploadConfigGet--------", cd);
				if(cd.isSuccess()){
					DataUploadConfig config = (DataUploadConfig) cd.getResult();
					device.setReportTime(config.getHour());
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initReportTime();
						}
					});
				} else{
					SdkLog.log("dataUploadConfigGet fail:",cd.getStatus());
					toast("获取报告上传时间失败："+cd.getStatus());
				}
			}
		}
	};

	private void toast(String msg){

		if(!isAdded()){
			return;
		}
		mActivity.runOnUiThread(()->{
			SdkLog.log("runOnUiThread:"+this);
			Toast toast = Toast.makeText(this.getContext(),msg,Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		});

	}


	private void initDeviceName() {
		if (device.getDeviceName() != null) {
			tvDeviceName.setText(device.getDeviceName());
		} else {
			tvDeviceName.setText("");
		}
	}

	private void initDeviceId() {
		if (device.getDeviceId() != null) {
			tvDeviceId.setText(device.getDeviceId());
		} else {
			tvDeviceId.setText("");
		}
	}
	private void initVersion() {
		if (device.getVersion() != null) {
			tvVersion.setText(device.getVersion());
		} else {
			tvVersion.setText("");
		}
	}
	private void initCommunityMode() {
		if (device.getCommunityMode() != -1) {
			this.tv_trans_model.setText(this.modes.get(device.getCommunityMode()));
		} else {
			tv_trans_model.setText("");
		}
	}
	private void initRealtimeInterval() {
		if (device.getRealtimeInterval()!= 0) {
			this.tv_realtime_interval.setText(device.getRealtimeInterval()+"s");
		} else {
			tv_realtime_interval.setText("");
		}
	}
	private void initReportTime() {
		if (device.getReportTime() != -1) {
			this.tv_report_time.setText(device.getReportTime()+":00");
		} else {
			tv_report_time.setText("");
		}
	}
	private void initBedInfo() {
		String str = "";
		if (device.getBedThickness() != -1) {
			str += this.thickness.get(device.getBedThickness());
		}
		if(device.getMaterial() != -1){
			str += " "+this.material.get(device.getMaterial());
		}
		this.tv_bed_config.setText(str);
	}




	private void initPageState(boolean isConnected) {
		setPageEnable(isConnected);
		if (!isConnected) {
			clearDeviceInfo();
		}else {

		}
	}

	private void setPageEnable(boolean enable) {
		btnMac.setEnabled(enable);
		btnUpgrade.setEnabled(enable);
	//	this.tv_bed_thickness.setEnabled(enable);
		this.tv_trans_model.setEnabled(enable);
		this.tv_realtime_interval.setEnabled(enable);
		this.tv_report_time.setEnabled(enable);
	}

	private void clearDeviceInfo() {
		/*tvDeviceName.setText("");
		tvDeviceId.setText("");
		tvMac.setText("");
		tvVersion.setText("");*/

	}


	private IConnectionStateCallback stateCallback = new IConnectionStateCallback() {
		@Override
		public void onStateChanged(final IDeviceManager manager, final CONNECTION_STATE state) {
			if (!isAdded()) {
				return;
			}

			if(state == CONNECTION_STATE.CONNECTED){
				initBtnState(true);
				if(manager.getAddress().equals(device.getAddress())){
					toast(device.getDeviceName()+"已连接");
				}
			}else if(state == CONNECTION_STATE.DISCONNECT){
				initBtnState(false);
				if(manager.getAddress().equals(device.getAddress())){
					toast(device.getDeviceName()+"断开连接");
				}
			}

		/*	mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (MainActivity.getCurDevice() != null && MainActivity.getCurDevice().getAddress().equals(manager.getAddress())) {
						initPageState(state == CONNECTION_STATE.CONNECTED);
						SdkLog.log(TAG + " onStateChanged state:" + state);
						if (state == CONNECTION_STATE.DISCONNECT) {
							if (upgrading) {
								upgrading = false;
								mActivity.hideUpgradeDialog();
								mActivity.setUpgradeProgress(0);
							}
						} else if (state == CONNECTION_STATE.CONNECTED) {
							if (upgrading) {
								upgrading = false;
								btnUpgrade.setEnabled(true);
								mActivity.hideUpgradeDialog();
							}
						}
					}
				}
			});*/
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getDeviceHelper().removeConnectionStateCallback(stateCallback);
	}



	
	private void upgradeDevice(FirmwareBean bean) {
		SdkLog.log(TAG+" upgradeDevice------" + bean);
		btnUpgrade.setEnabled(false);
		mActivity.showUpgradeDialog();
		mActivity.setUpgradeProgress(0);
		upgrading = true;
		// InputStream is = getResources().getAssets().open("xxx.des");
		getDeviceHelper().upgradeDevice(device.getAddress(), bean.crcDes, bean.crcBin, "6.08", bean.is, new IResultCallback<Integer>() {
			@Override
			public void onResultCallback(IDeviceManager manager, final CallbackData<Integer> cd) {
				// TODO Auto-generated method stub
				SdkLog.log(TAG+" upgradeDevice " + cd);
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (cd.isSuccess()) {
							int progress = cd.getResult();
							mActivity.setUpgradeProgress(progress);
							if (progress == 100) {
								upgrading = false;
								btnUpgrade.setEnabled(true);
								mActivity.hideUpgradeDialog();
								Toast.makeText(mActivity, R.string.up_success, Toast.LENGTH_SHORT).show();
							}
						} else {
							upgrading = false;
							mActivity.hideUpgradeDialog();
							Toast.makeText(mActivity, R.string.up_failed, Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v == btnConnectDevice) {// 233 - 10000
			if (!BleUtil.isBluetoothOpen(mActivity)) {
				Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enabler, BleConfig.REQCODE_OPEN_BT);
			} else {
//				String strId = etUserId.getText().toString().trim();
//				if (TextUtils.isEmpty(strId)) {
//					Toast.makeText(mActivity, R.string.userid_not_empty, Toast.LENGTH_SHORT).show();
//					return;
//				}
//				int uid = Integer.parseInt(strId);
//				if (uid <= 0 || strId.startsWith("0")) {
//					Toast.makeText(mActivity, R.string.userid_error, Toast.LENGTH_SHORT).show();
//					return;
//				}

				if(getDeviceHelper().isConnected(device.getAddress())) {//已连接设备
					getDeviceHelper().disconnect(device.getAddress());
				}else{
					Intent intent = new Intent(mActivity, SearchBleDeviceActivity.class);
					SdkLog.log("deviceIndex:"+deviceIndex+","+this);
					intent.putExtra("deviceIndex",deviceIndex);
					startActivity(intent);
				}
			}
		} else if (v == btnUpgrade) {
			final FirmwareBean firmwareBean = getFirmwareBean();
			SdkLog.log(TAG+" upgradeDevice------firmwareBean:" + firmwareBean);
			if (firmwareBean == null) {
				return;
			}

			upgradeDevice(firmwareBean);

		}  else if(v == set_trans_model){

			LayoutInflater inflater = LayoutInflater.from(getContext());
			View view = inflater.inflate(R.layout.dialog_common, null);
			LinearLayout layout =  view.findViewById(R.id.dialog_content);
			View items = inflater.inflate(R.layout.common_item, null);
			layout.addView(items);

			AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.Dialog_No_border).create();
			dialog.show();

			Window window = dialog.getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			int margin = getResources().getDimensionPixelSize(R.dimen.dp30);
			params.width = getResources().getDisplayMetrics().widthPixels - 2*margin;
			window.setAttributes(params);
			window.setContentView(view);

			final WheelView wheelView =	items.findViewById(R.id.s_item);
			wheelView.setAdapter(new KVWhellAdapter(modes.values(),modes.keySet()));
			wheelView.setCyclic(false);
			wheelView.setCurrentItem(0);
			wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(int index) {
					SdkLog.log("onItemSelected:"+index);
				}
			});
			View confirm = view.findViewById(R.id.confirm_button);
			confirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					KVWhellAdapter adapter =(KVWhellAdapter)wheelView.getAdapter();
					int index = wheelView.getCurrentItem();
					int modelV = adapter.getValue(index);
					SdkLog.log("mode:"+modelV);
					deviceConfig(v,modelV);
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});

		} else if(v == this.set_realtime_interval){
			Dialog dialog = new Dialog(getContext(), R.style.Dialog_No_border);
			dialog.show();

			LayoutInflater inflater = LayoutInflater.from(getContext());
			View view = inflater.inflate(R.layout.dialog_common, null);
			LinearLayout layout =  view.findViewById(R.id.dialog_content);
			View items = inflater.inflate(R.layout.realtime_interval, null);
			final EditText et = items.findViewById(R.id.interval);
			et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_CLASS_NUMBER);
			layout.addView(items);

			Window window = dialog.getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			int margin = getResources().getDimensionPixelSize(R.dimen.dp30);
			params.width = getResources().getDisplayMetrics().widthPixels - 2*margin;
			window.setAttributes(params);
			window.setContentView(view);

			View confirm = view.findViewById(R.id.confirm_button);
			confirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					String val = et.getText().toString();
					if(val == null||val.trim().equals("")){
						dialog.dismiss();
						return;
					}

					deviceConfig(v,Integer.parseInt(val));
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});

		} else if (v == this.set_report_time){
			List<String> itemStrs = new ArrayList<>();
			for(int i =0;i<24;i++){
				itemStrs.add(i+":00");
			}
			LayoutInflater inflater = LayoutInflater.from(DeviceFragment.this.getContext());
			View view = inflater.inflate(R.layout.dialog_common, null);
			AlertDialog.Builder builder=new AlertDialog.Builder(DeviceFragment.this.getContext());
			AlertDialog dialog=builder.create();
			dialog.setView(view,0,0,0,0);
			dialog.show();
			LinearLayout layout =  view.findViewById(R.id.dialog_content);
			View items = inflater.inflate(R.layout.common_item, null);
			layout.addView(items);
			final WheelView wheelView =	items.findViewById(R.id.s_item);
			wheelView.setAdapter(new StringWhellAdapter(itemStrs));
			wheelView.setCyclic(false);
			wheelView.setCurrentItem(0);
			wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(int index) {
					SdkLog.log("onItemSelected:"+index);
				}
			});
			View confirm = view.findViewById(R.id.confirm_button);
			confirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int index = wheelView.getCurrentItem();
					deviceConfig(v,index);
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.cancel_button).setOnClickListener((view1)-> dialog.dismiss());
		} else if(v == this.set_bed_config){


			LayoutInflater inflater = LayoutInflater.from(DeviceFragment.this.getContext());
			View view = inflater.inflate(R.layout.dialog_common, null);
			AlertDialog.Builder builder=new AlertDialog.Builder(DeviceFragment.this.getContext());
			//builder.setView(view);
			AlertDialog dialog=builder.create();
			dialog.setView(view,0,0,0,0);
			dialog.show();
			LinearLayout layout =  view.findViewById(R.id.dialog_content);
			View items = inflater.inflate(R.layout.common_item, null);
			layout.addView(items);

			((TextView)items.findViewById(R.id.id_param_1)).setText("床垫厚度");
			final WheelView wheelView =	items.findViewById(R.id.s_item);

			wheelView.setAdapter(new KVWhellAdapter(thickness.values(),thickness.keySet()));
			wheelView.setCyclic(false);
			wheelView.setCurrentItem(0);
			wheelView.setOnItemSelectedListener(index -> SdkLog.log("onItemSelected:"+index));
			((TextView)items.findViewById(R.id.id_param_2)).setText("床垫材质");
			final WheelView wheelView2 =	items.findViewById(R.id.s_item_2);
			wheelView2.setAdapter(new KVWhellAdapter(material.values(),material.keySet()));
			wheelView2.setCyclic(false);
			wheelView2.setCurrentItem(0);
			wheelView2.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(int index) {
					SdkLog.log("onItemSelected:"+index);
				}
			});


			View confirm = view.findViewById(R.id.confirm_button);
			confirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int index = wheelView.getCurrentItem();
					int index2 = wheelView2.getCurrentItem();
					int thicknessV = ((KVWhellAdapter) wheelView.getAdapter()).getValue(index);
					int materialV = ((KVWhellAdapter) wheelView2.getAdapter()).getValue(index2);
					deviceConfig(v,thicknessV,materialV);
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.cancel_button).setOnClickListener((view1 ->dialog.dismiss()));
		}
	}

	class FirmwareBean {
		InputStream is;
		long crcBin;
		long crcDes;
	}

	private FirmwareBean getFirmwareBean() {
		try {
			InputStream is = mActivity.getResources().getAssets().open("BM8701-2(LaiBang)-v6.10(v02.04.50)-ug-20230714.bin");
			FirmwareBean bean = new FirmwareBean();
			bean.is = is;
			bean.crcBin = 0L;
			bean.crcDes = 0L;
			return bean;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	private void deviceConfig(View v,Object... values){
		if(v == set_trans_model){
			int modeV = ((int)values[0]);
			String mode = modes.get(modeV);
			SdkLog.log("mode:"+mode);
			//DeviceFragment.this.tv_trans_model.setText(mode);
			BM8701Helper.getInstance(this.getContext()).bleWiFiModeSet(device.getAddress(),(byte)modeV,(manager,cb)->{
				if(cb.isSuccess()){
					device.setCommunityMode(modeV);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initCommunityMode();
						}
					});
				}else {
					toast("传输模式设置失败："+cb.getStatus());
					SdkLog.log("bleWiFiModeSet fail:"+cb.getStatus());
				}
			});

		} else if(v == this.set_realtime_interval){
			int val = (int)values[0];
			BM8701Helper.getInstance(this.getContext()).realtimeDataIntervalSet(device.getAddress(),(byte)0,(byte)val,(manager,cb)->{
				if(cb.isSuccess()){
					device.setRealtimeInterval(val);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initRealtimeInterval();
						}
					});
				}else {
					toast("实时数据上传时间设置失败："+cb.getStatus());
					SdkLog.log("realtimeDataIntervalSet fail:"+cb.getStatus());
				}
			});


		} else if(v == set_report_time){
			int hour = (int)values[0];
			BM8701Helper.getInstance(this.getContext()).dataUploadConfigSet(device.getAddress(),device.getDeviceId(),DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(),(byte)hour,(manager,cb)->{
				if(cb.isSuccess()){
					device.setReportTime(hour);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initReportTime();
						}
					});
				}else {
					toast("报告上传时间设置失败："+cb.getStatus());
					SdkLog.log("set_report_time fail:"+cb.getStatus());
				}
			});
		} else if(v == set_bed_config){
			int thicknessV = (Integer)values[0];
			int materialV = (Integer)values[1];

			String thickness = this.thickness.get(thicknessV);
			String material = this.material.get(materialV);

			BM8701Helper.getInstance(this.getContext()).sleepConfigSet(device.getAddress(),device.getDeviceId(), DeviceType.DEVICE_TYPE_BM8701_2_BLE.getType(), (byte)0,(byte)0,(byte)0,(byte)thicknessV,(byte)materialV,(manager, cd)->{
				if(cd.isSuccess()){
					device.setBedThickness(thicknessV);
					device.setMaterial(materialV);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							initBedInfo();
						}
					});
				}else {
					toast("床垫信息设置失败："+cd.getStatus());
					SdkLog.log("set_bed_config fail:"+cd.getStatus());
				}
			});

		}
	}
}
