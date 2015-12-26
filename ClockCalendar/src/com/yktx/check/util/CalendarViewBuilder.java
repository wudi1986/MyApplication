package com.yktx.check.util;

import android.content.Context;

import com.yktx.check.bean.CustomDate;
import com.yktx.check.widget.CalendarView;
import com.yktx.check.widget.CalendarView.CallBack;

/**
 * CalendarView的辅助类
 * @author huang
 *
 */
public class CalendarViewBuilder {
		private CalendarView[] calendarViews;
		/**
		 * 生产多个CalendarView
		 * @param context
		 * @param count
		 * @param style
		 * @param callBack
		 * @return
		 */
		public  CalendarView[] createMassCalendarViews(Context context,int count,int style,CallBack callBack){
			calendarViews = new CalendarView[count];
			for(int i = 0; i < count;i++){
				calendarViews[i] = new CalendarView(context, style,callBack);
			}
			return calendarViews;
		}
		
		public  CalendarView[] createMassCalendarViews(Context context,int count,CallBack callBack){
			
			return createMassCalendarViews(context, count, CalendarView.MONTH_STYLE,callBack);
		}
		/**
		 * 切换CandlendarView的样式
		 * @param style
		 */
		public void swtichCalendarViewsStyle(int style, CustomDate mShowDate){
			
			Tools.getLog(Tools.i, "kkk", "swtichCalendarViewsStyle ============== "+style);
			if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].switchStyle(style, mShowDate);
			}
		}
		/**
		 * CandlendarView回到当前日期
		 */
		
		public void backTodayCalendarViews(){
			if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].backToday();
			}
		}
}
