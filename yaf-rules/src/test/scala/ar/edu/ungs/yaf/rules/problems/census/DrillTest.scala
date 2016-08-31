package ar.edu.ungs.yaf.rules.problems.census

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.sql.DriverManager

@Test
class DrillTest {

  	@Before
  	def setUp()=
  	{
  	} 
  	
  	@Test
  	def testDrill = {

            Class.forName("org.apache.drill.jdbc.Driver");
            val conn = DriverManager.getConnection("jdbc:drill:drillbit=localhost") //USER,PASS);
            val stmt = conn.createStatement();
            val sql = "select * from dfs.pum.`/parquet/pum/` LIMIT 1"
            val rs = stmt.executeQuery(sql);

            while(rs.next()) {
                println(rs.getInt(1));
                println(rs.getInt(2));
                println(rs.getInt(3));
            }

            rs.close();
            stmt.close();
            conn.close();
  	}	  	
}


