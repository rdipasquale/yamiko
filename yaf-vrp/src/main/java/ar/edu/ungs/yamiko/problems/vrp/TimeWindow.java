package ar.edu.ungs.yamiko.problems.vrp;

import java.util.Calendar;
import java.util.Date;

public class TimeWindow {

	Calendar c1=Calendar.getInstance();
	Calendar c2=Calendar.getInstance();
	
	public TimeWindow(Date start,Date end) {
		c1.setTime(start);
		c2.setTime(end);
		if (c1.after(c2)) c2.add(Calendar.DATE, 1);
	
	}
	
	public TimeWindow(int hourStart,int minuteStart, int hourEnd, int minuteEnd) {
		c1.set(Calendar.HOUR_OF_DAY, hourStart);
		c1.set(Calendar.MINUTE, minuteStart);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		c2.set(Calendar.HOUR_OF_DAY, hourEnd);
		c2.set(Calendar.MINUTE, minuteEnd);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);		
		if (c1.after(c2)) c2.add(Calendar.DATE, 1);
	}
	
	/**
	 * Devuelve la longitud de la ventana en minutos
	 * @return
	 */
	public int length()
	{
		return (int)((c2.getTimeInMillis()-c1.getTimeInMillis())/60000);
	}

	/**
	 * Comienzo del intervalo
	 * @return
	 */
	public Calendar from()
	{
		return c1;
	}
	
	/**
	 * Fin del intervalo
	 * @return
	 */
	public Calendar to()
	{
		return c2;
	}	

}
