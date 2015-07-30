package ar.edu.ungs.yamiko.problems.vrp.entities.dto;

import java.io.Serializable;
import java.util.Calendar;

import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;

public class TimeWindowDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8183764491315470651L;
	private int hourStart;
	private int minuteStart;
	private int hourEnd;
	private int minuteEnd;
	
	public TimeWindowDto() {
		// TODO Auto-generated constructor stub
	}

	public TimeWindowDto(TimeWindow t) {
		if (t!=null)
		{
			hourStart=t.getC1().get(Calendar.HOUR_OF_DAY);
			hourEnd=t.getC2().get(Calendar.HOUR_OF_DAY);
			minuteStart=t.getC1().get(Calendar.MINUTE);
			minuteEnd=t.getC2().get(Calendar.MINUTE);
		}
	}

	public int getHourStart() {
		return hourStart;
	}

	public void setHourStart(int hourStart) {
		this.hourStart = hourStart;
	}

	public int getMinuteStart() {
		return minuteStart;
	}

	public void setMinuteStart(int minuteStart) {
		this.minuteStart = minuteStart;
	}

	public int getHourEnd() {
		return hourEnd;
	}

	public void setHourEnd(int hourEnd) {
		this.hourEnd = hourEnd;
	}

	public int getMinuteEnd() {
		return minuteEnd;
	}

	public void setMinuteEnd(int minuteEnd) {
		this.minuteEnd = minuteEnd;
	}

	public TimeWindowDto(int hourStart, int minuteStart, int hourEnd,
			int minuteEnd) {
		super();
		this.hourStart = hourStart;
		this.minuteStart = minuteStart;
		this.hourEnd = hourEnd;
		this.minuteEnd = minuteEnd;
	}
	
	
}
