package com.sleepace.sdkdemo.bm8701_2_ble;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sleepace.sdk.bm8701_2_ble.domain.HistoryData;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdkdemo.bm8701_2_ble.view.AnalysisChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ReportActivity extends BaseActivity {

	private AnalysisChart chart;
	private TextView tvDuration, tvAwakeDuration, tvOutOfBedDuration, tvNotMonitorDuration, tvAvgHeartRate, tvAvgBreathRate;
	private LinearLayout layoutBreathPause, layoutOutOfBed;
	private TextView tvReportDate;

	public static final int REPORT_TYPE_24 = 1;
	public static final int REPORT_TYPE_HISOTRY = 2;
	public static final int REPORT_TYPE_DEMO = 3;
	public static final String EXTRA_REPORT_TYPE = "extra_report_type";
	public static final String EXTRA_DATA = "extra_data";
	private int reportType;
	private HistoryData historyData;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		findView();
		initListener();
		initUI();
	}

	public void findView() {
		super.findView();
		tvReportDate = findViewById(R.id.tv_report_date);
		chart = (AnalysisChart) findViewById(R.id.chart);
		tvDuration = (TextView) findViewById(R.id.tv_sleep_duration);
		tvAwakeDuration = (TextView) findViewById(R.id.tv_awake_duration);
		tvOutOfBedDuration = (TextView) findViewById(R.id.tv_out_of_bed_duration);
		tvNotMonitorDuration = (TextView) findViewById(R.id.tv_no_monitor_duration);
		tvAvgHeartRate = (TextView) findViewById(R.id.tv_avg_heartrate);
		tvAvgBreathRate = (TextView) findViewById(R.id.tv_avg_breathrate);
		layoutBreathPause = findViewById(R.id.layout_breath_pause);
		layoutOutOfBed = findViewById(R.id.layout_out_of_bed);
	}

	public void initListener() {
		super.initListener();
	}

	public void initUI() {
		reportType = getIntent().getIntExtra(EXTRA_REPORT_TYPE, REPORT_TYPE_24);
		historyData = (HistoryData) getIntent().getSerializableExtra(EXTRA_DATA);

		switch (reportType) {
		case REPORT_TYPE_24:
			tvTitle.setText(R.string.data24);
			break;
		case REPORT_TYPE_HISOTRY:
			tvTitle.setText(R.string.Daily_Report);
			break;
		case REPORT_TYPE_DEMO:
			tvTitle.setText(R.string.simulation_data);
			break;
		}
	}

	private int getNotMonitorDuration() {
		int duration = 0;
		return duration;
	}

	private String getDuration(int duration) {
		int hour = duration / 60;
		int min = duration % 60;
		return hour + getString(R.string.hour) + min + getString(R.string.min);
	}

	private String getTimes(int times, boolean isHeartRate) {
		if (isHeartRate) {
			return times + getString(R.string.beats_min);
		} else {
			return times + getString(R.string.times_min);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

}
