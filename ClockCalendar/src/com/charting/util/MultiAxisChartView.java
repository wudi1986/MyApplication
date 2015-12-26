/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.charting.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.chart.StackBarChart;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.axis.DataAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

import com.yktx.check.R;
import com.yktx.check.bean.JobStatsBean;
import com.yktx.check.util.Tools;

/**
 * @ClassName MultiAxisChart02View
 * @Description 柱形图与折线图的结合例子,主要演示右轴
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */

public class MultiAxisChartView extends DemoView {

	private String TAG = "MultiAxisChart02View";
	private// 标签轴
	List<String> chartLabels = new LinkedList<String>();
	//标签轴显示 去掉年
	List<String> chartLabelsText = new LinkedList<String>();
	List<BarData> chartData = new LinkedList<BarData>();

	// 标签轴
	// List<String> chartLabelsLn = new LinkedList<String>();
	LinkedList<LineData> chartDataLn = new LinkedList<LineData>();

	StackBarChart chart = new StackBarChart();
	LineChart lnChart = new LineChart();
	int maxJobCount;
	int maxQuantityCount;
	double maxQuantity;

	ArrayList<JobStatsBean> JobStatsBeanList = new ArrayList<JobStatsBean>(7);

	public MultiAxisChartView(Context context,
			ArrayList<JobStatsBean> JobStatsBeanList) {
		super(context);
		// TODO Auto-generated constructor stub
		this.JobStatsBeanList = JobStatsBeanList;
		getDayMaxNum();
		initChart();
	}

	/**
	 * 获取一天中最多打卡的次数
	 */
	private void getDayMaxNum() {
		int Max = 0;
		for (int i = 0; i < JobStatsBeanList.size(); i++) {

			if (JobStatsBeanList.get(i).getJobCount() > Max) {
				Max = JobStatsBeanList.get(i).getJobCount();
			}
		}
		maxJobCount = Max;

		for (int i = 0; i < JobStatsBeanList.size(); i++) {

			if (JobStatsBeanList.get(i).getQuantity() != null) {
				String array[] = JobStatsBeanList.get(i).getQuantity().split(",");
				int length = array.length;
				if (array.length > maxQuantityCount) {
					maxQuantityCount = length;
				}
				double curMaxQuantity= 0.0;
				for(int j = 0; j < length; j++){
					curMaxQuantity = curMaxQuantity + Double.parseDouble(array[j]);
				}
				if(curMaxQuantity > maxQuantity){
					maxQuantity = curMaxQuantity;
				}
			}
		}

	}

	public static ArrayList<String> findDates(String start) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dBegin = null;
		Date dEnd = new Date(System.currentTimeMillis());
		try {
			dBegin = sdf.parse(start);
			// dEnd = sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> lDate = new ArrayList<String>();
		lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(calBegin
					.getTime()));
		}
		if (lDate.size() < 7) {
			int size = 7 - lDate.size();
			for (int i = 0; i < size; i++) {
				calBegin.add(Calendar.DAY_OF_MONTH, 1);
				lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(calBegin
						.getTime()));
			}
		}
		return lDate;
	}

	/**
	 * 用于初始化
	 */
	private void initChart() {
		
		if(JobStatsBeanList.size() > 0){
			chartLabels = findDates(JobStatsBeanList.get(0).getCheck_date());
		}
		for(int i = 0; i < chartLabels.size(); i++){
			chartLabelsText.add(chartLabels.get(i).substring(5));
		}
		chartDataLnSet();
		
		chartLnDataSet();
		
		chartRender();
		chartLnRender();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
		lnChart.setChartRange(w, h);
	}
	
	private void chartRender() {
		try {
			
			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
			
			chart.setChartDirection(XEnum.Direction.VERTICAL);
			// 标题
			chart.setTitle("Virtual vs Native Oracle RAC Performance");
			chart.addSubtitle("(XCL-Charts Demo)");
			chart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
			// 因为太长缩小标题字体
			// chart.getPlotTitle().getChartTitlePaint().setTextSize(20);
			// 图例
			// chart.getAxisTitle().setLeftTitle("Orders Per Minute (OPM)");
			// chart.getAxisTitle().setRightTitle("Average Response Time (RT)");
			
			// 标签轴
			
			chart.setCategories(chartLabelsText);
			// 数据轴
			chart.setDataSource(chartData);
			
			
			
			int steps = (int) (maxQuantity / 5);
			int index = (int) (maxQuantity % steps == 0 ? maxQuantity / steps
					: maxQuantity / steps + 1);
			chart.getDataAxis().setAxisMax(steps * index);
			chart.getDataAxis().setAxisSteps(steps);

//			chart.getDataAxis().setAxisMax(maxQuantity);
//			chart.getDataAxis().setAxisSteps(100000);
			chart.getDataAxis().setHorizontalTickAlign(Align.RIGHT);
			chart.getDataAxis().getTickLabelPaint().setTextAlign(Align.LEFT);
			
			// 定制数据轴上的标签格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
				
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub

					double label = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					return df.format(label).toString();
				}
				
			});

			// 定制标签轴标签的标签格式
			CategoryAxis categoryAxis = chart.getCategoryAxis();
			// categoryAxis.setTickLabelRotateAngle(-15f);
			categoryAxis.getTickLabelPaint().setTextSize(25);
			categoryAxis.getTickLabelPaint().setTextAlign(Align.CENTER);

			categoryAxis.setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					// String tmp = "c-["+value+"]";
					return value;
				}

			});
			// 定制柱形顶上的标签格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					// DecimalFormat df=new DecimalFormat("#0.00");
					DecimalFormat df = new DecimalFormat("#0");
					return df.format(value).toString();
				}
			});

			// 网格背景
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showEvenRowBgColor();
			chart.getPlotGrid().showOddRowBgColor();
			// 隐藏Key值
			chart.getPlotLegend().hide();

			chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);
//			chart.setTranslateXY(-4000, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataLnSet() {
		// 标签1对应的柱形数据集
		LinkedList[] listArray = new LinkedList[maxQuantityCount];
		
		for(int i = 0;i < maxQuantityCount ;i++){
			listArray[i] = new LinkedList<Double>();
		}
		
		for (int i = 0; i < chartLabels.size(); i++) {
			String date = chartLabels.get(i);
			JobStatsBean bean = idHaveBean(date);
			if (bean == null) {
				for (int j = 0; j < maxQuantityCount; j++) {
					listArray[j].add(0d);
				}
			} else {
				if (bean.getQuantity() != null) {
					String array[] = bean.getQuantity().split(",");
					for (int j = 0; j < maxQuantityCount; j++) {
						
						if (j < array.length) {
							listArray[j].add(Double.parseDouble(array[j]));
						} else {
							listArray[j].add(0d);
						}
					}
				} else {
					for (int j = 0; j < maxQuantityCount; j++) {
						listArray[j].add(0d);
					}
				}

			}
		}
		// List<Double> dataSeries1 = new LinkedList<Double>();
		// dataSeries1.add(40000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// dataSeries1.add(73000d);
		// // dataSeries1.add(400d);
		//
		// List<Double> dataSeries2 = new LinkedList<Double>();
		// dataSeries2.add(45000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(0d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(85000d);
		// dataSeries2.add(450d);
		int color1 = getResources().getColor(R.color.meibao_color_1);
		int color2 = getResources().getColor(R.color.meibao_color_2);
		BarData[] BarDataArray = new BarData[maxQuantityCount];
		for (int j = 0; j < maxQuantityCount; j++) {
			int color = j % 2 == 0 ? color1 : color2;
			BarDataArray[j] = new BarData("", listArray[j], color);
			chartData.add(BarDataArray[j]);
		}
		// BarData BarDataA = new BarData("Virtual OPM", dataSeries1,
		// Color.rgb(0,
		// 221, 177));
		// BarData BarDataB = new BarData("Physical OPM", dataSeries2,
		// Color.rgb(
		// 238, 28, 161));

		// chartData.add(BarDataA);
		// chartData.add(BarDataB);
	}

	private void chartLnDataSet() {
		// 标签1对应的数据集
		LinkedList<Double> virtual = new LinkedList<Double>();
		for (int i = 0; i < chartLabels.size(); i++) {
			String date = chartLabels.get(i);
			JobStatsBean bean = idHaveBean(date);
			if (bean == null) {
				virtual.add(0d);
			} else {
				virtual.add((double) bean.getJobCount());
				Tools.getLog(Tools.i, "aaa",
						" bean.getJobCount() =========== " + bean.getJobCount());
			}
		}

		// virtual.add(0d);

		// // 标签2对应的数据集
		// LinkedList<Double> physical = new LinkedList<Double>();
		// // physical.add(0d);
		// physical.add(110d);
		// physical.add(120d);
		// // physical.add(0d);

		// 将标签与对应的数据集分别绑定
		LineData lineData1 = new LineData("", virtual, Color.rgb(250,115,80));
		// LineData lineData2 = new LineData("", physical, 0x00000000);

		lineData1.setDotStyle(XEnum.DotStyle.DOT);
		lineData1.getDotPaint().setColor(Color.rgb(250,115,80));

		// lineData2.setDotStyle(XEnum.DotStyle.DOT);
		// lineData2.getDotPaint().setColor(Color.rgb(
		// 75, 166, 51));

		chartDataLn.add(lineData1);
		// chartDataLn.add(lineData2);
	}

	private JobStatsBean idHaveBean(String date) {
		for (int i = 0; i < JobStatsBeanList.size(); i++) {
			if (JobStatsBeanList.get(i).getCheck_date().equals(date)) {
				return JobStatsBeanList.get(i);
			}
		}
		return null;
	}

	private void chartLnRender() {
		try {
			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			lnChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

			renderLnAxis();

			lnChart.getPlotLegend().show();
			lnChart.getPlotLegend().setType(XEnum.LegendType.COLUMN);
			lnChart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.TOP);
			lnChart.getPlotLegend().setHorizontalAlign(
					XEnum.HorizontalAlign.LEFT);
			lnChart.getPlotLegend().hideBackground();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * 折线图轴相关
	 */
	private void renderLnAxis() {
		// 标签轴
		lnChart.setCategories(chartLabelsText);
		lnChart.getCategoryAxis().hide();

		// 设定数据源
		lnChart.setDataSource(chartDataLn);
		// 数据轴
		// lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
		DataAxis dataAxis = lnChart.getDataAxis();

		int steps = maxJobCount / 5 == 0 ? 1 : maxJobCount/5;
		int index = maxJobCount % steps == 0 ? 5
				: maxJobCount / steps + 1;
		dataAxis.setAxisMax(steps * index);
		dataAxis.setAxisSteps(steps);

		dataAxis.setAxisMin(0);
		// int steps = maxJobCount%5 == 0 ? maxJobCount/5 :maxJobCount/6;
		// Tools.getLog(Tools.i, "aaa", "steps ============"+steps);

		dataAxis.getAxisPaint().setColor(Color.rgb(51, 204, 204));
		dataAxis.getAxisPaint().setStrokeWidth(5);
		dataAxis.getTickMarksPaint().setColor(Color.rgb(51, 204, 204));
		dataAxis.getTickMarksPaint().setStrokeWidth(2);

		// 定制数据轴上的标签格式
		lnChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

			@Override
			public String textFormatter(String value) {
				// TODO Auto-generated method stub
				double label = Double.parseDouble(value);
				DecimalFormat df = new DecimalFormat("#0");
				return df.format(label).toString();
			}
			
		});

		// 允许线与轴交叉时，线会断开
		lnChart.setLineAxisIntersectVisible(false);

		// 调整右轴显示风格
		lnChart.getDataAxis().setHorizontalTickAlign(Align.LEFT);
		lnChart.getDataAxis().getTickLabelPaint().setTextAlign(Align.RIGHT);
		lnChart.setXCoordFirstTickmarksBegin(true);
		//
		lnChart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);
//		lnChart.setTranslateXY(-4000, 0);
	}

	@Override
	protected int[] getBarLnDefaultSpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = 100; // left
		ltrb[1] = 10; // top
		ltrb[2] = 100; // right
		ltrb[3] = 100; // bottom
		return ltrb;
	}

	@Override
	public void render(Canvas canvas) {
		try {
			
			chart.render(canvas);
			lnChart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		lst.add(lnChart);
		return lst;
	}

}
