package ar.edu.ungs.census;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CensusBroker {

    @Autowired
    private DrillPool jdbcTemplate;
    
    public CensusBroker() {
		// TODO Auto-generated constructor stub
	}
    
    @RequestMapping("/getCount")
    public Integer getCount(@RequestParam(value="sql")String sql)
    {
    	//System.out.println(System.currentTimeMillis() + " - Entra " + sql);
    	Integer cach=DrillCache.getCache(sql);
    	if (cach>-1)
    	{
//        	System.out.println(System.currentTimeMillis() + " - Cache " + sql);            
    		return cach;
    	}
    	
    	try {
			Integer salida=0;
			Connection con=jdbcTemplate.borrowConnection();
//	    	System.out.println(System.currentTimeMillis() + " - Borrow Connection " + sql);
			Statement stmt=con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) salida=rs.getInt(1);
		    salida=rs.getInt(1);
            rs.close();
            stmt.close();	
            jdbcTemplate.returnConnection(con);
            DrillCache.addCache(sql, salida);
//        	System.out.println(System.currentTimeMillis() + " - Sale " + sql);            
            return salida;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
    }
    
    

}
