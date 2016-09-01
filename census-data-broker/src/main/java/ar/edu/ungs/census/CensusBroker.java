package ar.edu.ungs.census;

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
    private DataSourceDrill jdbcTemplate;
    
    public CensusBroker() {
		// TODO Auto-generated constructor stub
	}
    
    @Cacheable("basicCache")
    @RequestMapping("/getCount")
    public Integer getCount(@RequestParam(value="sql")String sql)
    {
    	try {
			Integer salida=0;
			Statement stmt=jdbcTemplate.getConnection().createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) salida=rs.getInt(1);
		    salida=rs.getInt(1);
            rs.close();
            stmt.close();				
            return salida;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    

}
