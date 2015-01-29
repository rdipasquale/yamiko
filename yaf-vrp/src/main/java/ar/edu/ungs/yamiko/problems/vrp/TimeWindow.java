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
	
	public int minGap(TimeWindow t2,int marginInMinutes,Double timeTravel, int timeServe)
	{
		if (t2==null) return 0; // Algo con restricción intersecta a algo sin restricción
		Calendar travelF=Calendar.getInstance();
		Calendar travelT=Calendar.getInstance();
		travelF.setTime(c1.getTime());
		travelT.setTime(c2.getTime());
		travelF.add(Calendar.MINUTE, timeTravel.intValue()+timeServe-marginInMinutes);
		travelT.add(Calendar.MINUTE, timeTravel.intValue()+timeServe+marginInMinutes);
		
		if ( 
				(travelF.before(t2.from()) && travelT.after(t2.from())) ||
				(travelF.before(t2.to()) && travelT.after(t2.to()) ||
				travelF.equals(t2.from()) ||
				travelT.equals(t2.to())  || 
				travelF.equals(t2.to()) ||
				travelT.equals(t2.from()) ) 
			)
			return 0;
		long diff1 = Math.abs(travelT.getTimeInMillis() - t2.from().getTimeInMillis())/60000;
		long diff2 = Math.abs(travelF.getTimeInMillis() - t2.to().getTimeInMillis())/60000;
		return new Long(Math.min(diff1, diff2)).intValue();		
	}
	
	public boolean intersects(TimeWindow t2,int marginInMinutes,Double timeTravel, int timeServe)
	{
		if (t2==null) return true; // Algo con restricción intersecta a algo sin restricción
		Calendar travelF=Calendar.getInstance();
		Calendar travelT=Calendar.getInstance();
		travelF.setTime(c1.getTime());
		travelT.setTime(c2.getTime());
		travelF.add(Calendar.MINUTE, timeTravel.intValue()+timeServe-marginInMinutes);
		travelT.add(Calendar.MINUTE, timeTravel.intValue()+timeServe+marginInMinutes);
		
		if ( 
				(travelF.before(t2.from()) && travelT.after(t2.from())) ||
				(travelF.before(t2.to()) && travelT.after(t2.to()) ||
				travelF.equals(t2.from()) ||
				travelT.equals(t2.to())  || 
				travelF.equals(t2.to()) ||
				travelT.equals(t2.from()) ) 
			)
			return true;
		return false;

	}

	@Override
	public String toString() {
		return "[" + c1.get(Calendar.HOUR_OF_DAY) + ":" + c1.get(Calendar.MINUTE) + "hs ;" + 
				c2.get(Calendar.HOUR_OF_DAY) + ":" + c2.get(Calendar.MINUTE) + "hs ]";
	}

	
}
