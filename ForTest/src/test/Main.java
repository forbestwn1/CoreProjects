package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

	}

}

@Configuration
@ComponentScan(basePackages = "test") // Specify the base package to scan
class AppConfig {
    // You can define @Bean methods here if needed for explicit bean creation
}
