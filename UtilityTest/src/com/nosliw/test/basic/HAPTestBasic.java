package com.nosliw.test.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;

public class HAPTestBasic {

	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		HAPRuntimeEnvironment runtimeEnv = context.getBean(HAPRuntimeEnvironment.class);
		int kkk = 555;
		
	}

}

@Configuration
@ComponentScan(basePackages = "com.nosliw") // Specify the base package to scan
class AppConfig {
    // You can define @Bean methods here if needed for explicit bean creation
}
