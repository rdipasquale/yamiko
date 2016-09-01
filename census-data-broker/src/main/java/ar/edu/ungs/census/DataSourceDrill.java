package ar.edu.ungs.census;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.drill.jdbc.impl.DriverImpl;

public class DataSourceDrill {

	private static final int CONN=2000;
	private static int pivot=0;
	private Connection[] conexiones=new Connection[CONN];

	public DataSourceDrill() {
        try {
        	System.out.println("Inicializando Drill.... " + System.currentTimeMillis());
			DriverManager.registerDriver(new DriverImpl());
            Class.forName("org.apache.drill.jdbc.Driver");
            for (int i=0;i<CONN;i++)
            	conexiones[i]=DriverManager.getConnection("jdbc:drill:drillbit=localhost");
        	System.out.println("Fin Drill.... " + System.currentTimeMillis());

        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException {
        pivot++;
        if (pivot>=CONN) pivot=0;
		return conexiones[pivot];
        
	}
	
}
