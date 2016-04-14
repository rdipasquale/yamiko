package ar.edu.ungs.yaf.vrp.entities

import java.util.Calendar

@SerialVersionUID(40035L)
class TimeWindow(hourStart:Int,minuteStart:Int, hourEnd:Int, minuteEnd:Int) extends Serializable {
  
	private val c1:Calendar=Calendar.getInstance();
	private val c2:Calendar=Calendar.getInstance();
	
	c1.set(Calendar.HOUR_OF_DAY, hourStart);
	c1.set(Calendar.MINUTE, minuteStart);
	c1.set(Calendar.SECOND, 0);
	c1.set(Calendar.MILLISECOND, 0);
	c2.set(Calendar.HOUR_OF_DAY, hourEnd);
	c2.set(Calendar.MINUTE, minuteEnd);
	c2.set(Calendar.SECOND, 0);
	c2.set(Calendar.MILLISECOND, 0);		
	if (c1.after(c2)) c2.add(Calendar.DATE, 1)
	
	def length():Int=((c2.getTimeInMillis()-c1.getTimeInMillis())/60000).toInt
	def from():Calendar=c1
	def to():Calendar=c2

	def minGap(t2:TimeWindow,marginInMinutes:Int,timeTravel:Double , timeServe:Int):Int=
	{
		if (t2==null) return 0; // Algo con restricción intersecta a algo sin restricción
		val travelF=Calendar.getInstance();
		val travelT=Calendar.getInstance();
		travelF.setTime(c1.getTime());
		travelT.setTime(c2.getTime());
		travelF.add(Calendar.MINUTE, (timeTravel.round+timeServe-marginInMinutes).intValue())
		travelT.add(Calendar.MINUTE, (timeTravel.round+timeServe+marginInMinutes).intValue())
		
		if ( 
				(travelF.after(t2.from()) && travelT.before(t2.to())) ||
				(travelF.before(t2.from()) && travelT.after(t2.from())) ||
				(travelF.before(t2.to()) && travelT.after(t2.to()) ||				
				travelF.equals(t2.from()) ||
				travelT.equals(t2.to())  || 
				travelF.equals(t2.to()) ||
				travelT.equals(t2.from()) ) 
			)
			return 0;
		val diff1 = math.abs(travelT.getTimeInMillis() - t2.from().getTimeInMillis())/60000;
		val diff2 = math.abs(travelF.getTimeInMillis() - t2.to().getTimeInMillis())/60000;
		return (math.min(diff1, diff2)).intValue();		
	}
	
	def intersects(t2:TimeWindow,marginInMinutes:Int,timeTravel:Double , timeServe:Int):Boolean=
	{
		if (t2==null) return true; // Algo con restricción intersecta a algo sin restricción
		val travelF=Calendar.getInstance();
		val travelT=Calendar.getInstance();
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
	
	override def toString = "[" + c1.get(Calendar.HOUR_OF_DAY) + ":" + c1.get(Calendar.MINUTE) + "hs ;" +  c2.get(Calendar.HOUR_OF_DAY) + ":" + c2.get(Calendar.MINUTE) + "hs ]"

	def getC1()=c1
	def getC2()=c2


	def minGap(t:Calendar ,marginInMinutes:Int):Int=
	{
		if (t==null) return 0; // Algo con restricción intersecta a algo sin restricción
		if (c2.after(t) && c1.before(t)) return 0;
		val diff1 = math.abs(c1.getTimeInMillis() - t.getTimeInMillis())/60000;
		val diff2 = math.abs(c2.getTimeInMillis() - t.getTimeInMillis())/60000;
		val diff=(math.min(diff1, diff2)).intValue();
		if (diff<marginInMinutes) return 0;
		return diff;		
	}

}