package ar.edu.ungs.census;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@Configuration
@ConfigurationProperties()
@EnableCaching
public class Application{ // extends SpringBootServletInitializer {
    
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("basicCache")));
        return cacheManager;

    }   
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

	   @Value("${spring.datasource.username}")
	   private String user;
	 
	   @Value("${spring.datasource.password}")
	   private String password;
	 
	   @Value("${spring.datasource.url}")
	   private String dataSourceUrl;
	 
//	   @Value("${spring.datasource.dataSourceClassName}")
//	   private String dataSourceClassName;
	   
	   @Value("${spring.datasource.driver-class-name}")
	   private String driverClassName;
	   
	   @Value("${spring.datasource.poolName}")
	   private String poolName;
	 
	   @Value("${spring.datasource.connectionTimeout}")
	   private int connectionTimeout;
	 
	   @Value("${spring.datasource.maxLifetime}")
	   private int maxLifetime;
	 
	   @Value("${spring.datasource.maximumPoolSize}")
	   private int maximumPoolSize;
	 
	   @Value("${spring.datasource.minimumIdle}")
	   private int minimumIdle;
	 
	   @Value("${spring.datasource.idleTimeout}")
	   private int idleTimeout;
	 
	   @Bean
	   public DataSourceDrill dataSource() {
	      return new DataSourceDrill();
	   }
	   
	   

}