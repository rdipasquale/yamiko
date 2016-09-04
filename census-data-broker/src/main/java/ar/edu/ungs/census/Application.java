package ar.edu.ungs.census;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;




@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@Configuration
@ConfigurationProperties()
public class Application{ // extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
	    SpringApplication.run(Application.class, args);
	}
	 
   @Bean
   public DrillPool dataSource() {
	   System.out.println("Inicializando Pool: " + System.currentTimeMillis());
	   DrillPool salida=new DrillPool();
	   System.out.println("Pool Inicializado: " + System.currentTimeMillis());
	   return salida;
   }
	   
	   

}