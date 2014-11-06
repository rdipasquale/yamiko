package ar.edu.ungs.yamiko.problems.vrp;

import java.util.Calendar;
import java.util.Date;

public class TimeWindow {

	private int hourStart;
	private int minuteStart;
	private int hourEnd;
	private int minuteEnd;
	
	public TimeWindow(Date start,Date end) {
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTime(start);
		c2.setTime(end);
		this.hourEnd=c2.get(Calendar.HOUR_OF_DAY);
		this.hourStart=c1.get(Calendar.HOUR_OF_DAY);
		this.minuteEnd=c2.get(Calendar.MINUTE);
		this.minuteStart=c1.get(Calendar.MINUTE);		
	}
	
	public TimeWindow(int hourStart,int minuteStart, int hourEnd, int minuteEnd) {
		this.hourEnd=hourEnd;
		this.hourStart=hourStart;
		this.minuteEnd=minuteEnd;
		this.minuteStart=minuteStart;
	}
	

}
