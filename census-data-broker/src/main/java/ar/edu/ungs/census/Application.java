package ar.edu.ungs.census;

import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


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
	   public DataSource dataSource() {
	       Properties dsProps = new Properties();
	       dsProps.put("jdbcUrl", dataSourceUrl);
	       dsProps.put("url", dataSourceUrl);
	       dsProps.put("user", user);
	       dsProps.put("password", password);
	 
	       Properties configProps = new Properties();
	       	  configProps.put("jdbcUrl", dataSourceUrl);
	          configProps.put("poolName",poolName);
	          configProps.put("maximumPoolSize",maximumPoolSize);
	          configProps.put("minimumIdle",minimumIdle);
	          configProps.put("minimumIdle",minimumIdle);
	          configProps.put("connectionTimeout", connectionTimeout);
	          configProps.put("idleTimeout", idleTimeout);
	          configProps.put("dataSourceProperties", dsProps);
	          
	 
	      HikariConfig hc = new HikariConfig(configProps);
	      HikariDataSource ds = new HikariDataSource(hc);
	      return ds;
	   }
	   
//	    @Bean
//	    public DataSourceInitializer dataSourceInitializer(DataSource dataSource)
//	    {
//	        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();    
//	        dataSourceInitializer.setDataSource(dataSource);
//	        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//	        databasePopulator.addScript(new ClassPathResource("data.sql"));
//	        dataSourceInitializer.setDatabasePopulator(databasePopulator);
//	        dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
//	        return dataSourceInitializer;
//	    }	
	   
	    @Bean
	    public JdbcTemplate jdbcTemplate(DataSource dataSource)
	    {
	        return new JdbcTemplate(dataSource);
	    }
	 
	    @Bean
	    public PlatformTransactionManager transactionManager(DataSource dataSource)
	    {
	        return new DataSourceTransactionManager(dataSource);
	    }	   

}