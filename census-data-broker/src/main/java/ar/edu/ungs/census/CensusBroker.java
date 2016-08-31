package ar.edu.ungs.census;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CensusBroker {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public CensusBroker() {
		// TODO Auto-generated constructor stub
	}
    
    @Cacheable("basicCache")
    @RequestMapping("/getCount")
    public Integer getCount(@RequestParam(value="sql")String sql)
    {
    	try {
			Integer salida=0;
			salida=jdbcTemplate.query(sql,new IntRowMapper()).get(0);
			return salida;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    

}
