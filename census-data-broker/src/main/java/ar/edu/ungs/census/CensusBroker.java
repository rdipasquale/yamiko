package ar.edu.ungs.census;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    
    @Cacheable("basicCache")
    @RequestMapping("/getCount")
    public Integer getCount(@RequestParam(value="sql")String sql)
    {
    	try {
			Integer salida=0;
			Connection con=jdbcTemplate.borrowConnection();
			Statement stmt=con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) salida=rs.getInt(1);
		    salida=rs.getInt(1);
            rs.close();
            stmt.close();	
//            System.out.println(salida + " - " + sql);
            jdbcTemplate.returnConnection(con);
            return salida;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
    }
    
    

}
