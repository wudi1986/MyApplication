//package com.charting.util;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//import com.github.mikephil.charting.charts.CombinedChart;
//import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.XAxis.XAxisPosition;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.CombinedData;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.yktx.check.R;
//import com.yktx.check.bean.JobStatsBean;
//
//@SuppressLint("SimpleDateFormat")
//public class CombinedChartView extends LinearLayout {
//
//	public CombinedChartView(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
//
//	private CombinedChart mChart;
//	private int itemcount;
//
//	LinearLayout homeSale;
//
//	public LinearLayout getBestView(Activity mContext,
//			ArrayList<JobStatsBean> dateList) {
//		LayoutInflater mInflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		homeSale = (LinearLayout) mInflater.inflate(R.layout.activity_combined,
//				null);
//
//		mChart = (CombinedChart) homeSale.findViewById(R.id.chart1);
//		mChart.setDescription("");
//		mChart.setDrawGridBackground(false);
//		mChart.setDrawBarShadow(false);
//
//		// draw bars behind lines
//		mChart.setDrawOrder(new DrawOrder[] { DrawOrder.BAR, DrawOrder.LINE });
//
//		YAxis rightAxis = mChart.getAxisRight();
//		rightAxis.setDrawGridLines(false);
//
//		YAxis leftAxis = mChart.getAxisLeft();
//		leftAxis.setDrawGridLines(false);
//
//		XAxis xAxis = mChart.getXAxis();
//		xAxis.setPosition(XAxisPosition.BOTH_SIDED);
//
//		Calendar cal = Calendar.getInstance();
//		String start = dateList.get(0).getCheck_date();
//		ArrayList<String> list = findDates(start);
//		String mMonths[] = new String[list.size()];
//		ArrayList<Entry> entries = new ArrayList<Entry>();
//		for (int i = 0; i < list.size(); i++) {
//			mMonths[i] = list.get(i);
//			
//		}
//		itemcount = mMonths.length;
//		CombinedData data = new CombinedData(mMonths, false);
//		data.setData(generateLineData());
//		data.setData(generateBarData());
//		// data.setData(generateScatterData());
//		// data.setData(generateCandleData());
//		mChart.setData(data);
//		mChart.invalidate();
//		return homeSale;
//	}
//
//	public static ArrayList<String> findDates(String start) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date dBegin = null;
//		Date dEnd = new Date(System.currentTimeMillis());
//		try {
//			dBegin = sdf.parse(start);
//			// dEnd = sdf.parse(end);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ArrayList<String> lDate = new ArrayList<String>();
//		lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(dBegin));
//		Calendar calBegin = Calendar.getInstance();
//		// 使用给定的 Date 设置此 Calendar 的时间
//		calBegin.setTime(dBegin);
//		Calendar calEnd = Calendar.getInstance();
//		// 使用给定的 Date 设置此 Calendar 的时间
//		calEnd.setTime(dEnd);
//		// 测试此日期是否在指定日期之后
//		while (dEnd.after(calBegin.getTime())) {
//			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
//			calBegin.add(Calendar.DAY_OF_MONTH, 1);
//			lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(calBegin
//					.getTime()));
//		}
//		if (lDate.size() < 7) {
//			int size = 7 - lDate.size();
//			for (int i = 0; i < size; i++) {
//				calBegin.add(Calendar.DAY_OF_MONTH, 1);
//				lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(calBegin
//						.getTime()));
//			}
//		}
//		return lDate;
//	}
//
//	private LineData generateLineData() {
//
//		LineData d = new LineData();
//
//		ArrayList<Entry> entries = new ArrayList<Entry>();
//
//		for (int index = 0; index < itemcount; index++)
//			entries.add(new Entry(getRandom(1, 1.2f), index));
//
//		LineDataSet set = new LineDataSet(entries, "Line DataSet");
//		set.setColor(Color.rgb(240, 238, 70));
//		set.setLineWidth(2.5f);
//		set.setCircleColor(Color.rgb(240, 238, 70));
//		set.setCircleSize(5f);
//		set.setFillColor(Color.rgb(240, 238, 70));
//		set.setDrawCubic(true);
//		set.setDrawValues(true);
//		set.setValueTextSize(10f);
//		set.setValueTextColor(Color.rgb(240, 238, 70));
//		set.setAxisDependency(YAxis.AxisDependency.RIGHT);
//		d.addDataSet(set);
//		
//		return d;
//	}
//
//	private BarData generateBarData() {
//
//		BarData d = new BarData();
//		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
//		for (int index = 0; index < itemcount; index++)
//			entries.add(new BarEntry(getRandom(15, 30), index));
//		BarDataSet set = new BarDataSet(entries, "Bar DataSet");
//		set.setColor(Color.rgb(60, 220, 78));
//		set.setValueTextColor(Color.rgb(60, 220, 78));
//		set.setValueTextSize(10f);
//		d.addDataSet(set);
//		set.setAxisDependency(YAxis.AxisDependency.LEFT);
//		return d;
//	}
//
//
//	private float getRandom(float range, float startsfrom) {
//		return (float) (Math.random() * range) + startsfrom;
//	}
//
//}
