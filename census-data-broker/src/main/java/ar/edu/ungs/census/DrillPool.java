package ar.edu.ungs.census;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.drill.jdbc.impl.DriverImpl;

public class DrillPool {

	private final int INIT=400;
   private long expirationTime;   
   private Hashtable<Connection, Long> locked, unlocked; 
	   
	public DrillPool() {
		try {
			DriverManager.registerDriver(new DriverImpl());
			Class.forName("org.apache.drill.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	   expirationTime = 3000000; // 30 seconds
	   locked = new Hashtable<Connection, Long>();         
	   unlocked = new Hashtable<Connection, Long>();
	   for (int i=0;i<INIT;i++)
		   returnConnection(borrowConnection());
	}

	synchronized Connection checkOut()
	{
	   long now = System.currentTimeMillis();
	   Connection o;        
	   if( unlocked.size() > 0 )
	   {
	      Enumeration<Connection> e = unlocked.keys();  
	      while( e.hasMoreElements() )
	      {
	         o = e.nextElement();           
	         if( ( now - ( ( Long ) unlocked.get( o ) ).longValue() ) > expirationTime )
	         {
	            // object has expired
	            unlocked.remove( o );
	            expire( o );
	            o = null;
	         }
	         else
	         {
	            if( validate( o ) )
	            {
	               unlocked.remove( o );
	               locked.put( o, new Long( now ) );                
	               return( o );
	            }
	            else
	            {
	               // object failed validation
	               unlocked.remove( o );
	               expire( o );
	               o = null;
	            }
	         }
	      }
	   }        
	   // no objects available, create a new one
	   o = create();        
	   locked.put( o, new Long( now ) ); 
	   return( o );
	}	
	
	synchronized void checkIn( Connection o )
	{
	   locked.remove( o );
	   unlocked.put( o, new Long( System.currentTimeMillis() ) );
	}
	
	Connection create()
	{
	   try
	   {
	      return( DriverManager.getConnection("jdbc:drill:drillbit=localhost"));
	   }
	   catch( SQLException e )
	   {
	      e.printStackTrace();
	      return( null );
	   }
	}

	void expire( Connection  o )
	{
	   try
	   {
	      o.close();
	   }
	   catch( SQLException e )
	   {
	      e.printStackTrace();
	   }
	}
	
	boolean validate( Connection o )
	{
	   try
	   {
	      return( !   o.isClosed() );
	   }
	   catch( SQLException e )
	   {
	      e.printStackTrace();
	      return( false );
	   }
	}
	
	public Connection borrowConnection()
	{
	   return( checkOut() );
	}
	
	public void returnConnection( Connection c )
	{
	   checkIn(c);
	}
	
	
}
