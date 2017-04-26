package ar.edu.ungs.census;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CensusBroker {

    public CensusBroker() {
		// TODO Auto-generated constructor stub
	}

    @Value("${drill.url}")
    private String url;
    
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
			
			RestTemplate res=new RestTemplate();
			DrillRequest req=new DrillRequest("SQL",sql);
			DrillResponse resp= res.postForObject(url, req, DrillResponse.class);
			salida=resp.getRows()[0].getCount();
			DrillCache.addCache(sql, salida);
            return salida;
            
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
    }
    
    

}
