package ar.edu.ungs.yamiko.problems.vrp.entities.dto;

import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;

public class TimeWindowAdapter {

	public TimeWindowAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	public static TimeWindow adapt(TimeWindowDto d)
	{ 
		if (d==null) return null;
		if (d.getHourStart()==0 & d.getMinuteStart()==0 & d.getHourEnd()==0 & d.getMinuteEnd()==0 )return null;
		TimeWindow c=new TimeWindow(d.getHourStart(),d.getMinuteStart(),d.getHourEnd(),d.getMinuteEnd());
		return c;
	}
		
}
