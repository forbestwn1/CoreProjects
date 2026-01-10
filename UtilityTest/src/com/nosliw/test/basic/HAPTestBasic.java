package com.nosliw.test.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.resource.HAPResourceIdSimple;

public class HAPTestBasic {

	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		HAPManagerApplicationBrick brickMan = context.getBean(HAPManagerApplicationBrick.class);
		
		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.TEST_COMPLEX_1_100.getBrickType(), "1.0.0", "dynamic_provider"), brickMan, null);
//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.TEST_COMPLEX_1_100.getBrickType(), "1.0.0", "dynamic_use"), brickMan, null);
		
//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.DATAEXPRESSIONLIB_100.getBrickType(), "1.0.0", "test1"), brickMan, null);

//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.UIPAGE_100.getBrickType(), "1.0.0", "test0"), brickMan, null);
	
//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.DATAEXPRESSIONLIB_100.getBrickType(), "1.0.0", "test2"), brickMan, null);

//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.TEST_COMPLEX_1_100.getBrickType(), "1.0.0", "event"), brickMan, null);

//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.TEST_COMPLEX_1_100.getBrickType(), "1.0.0", "basic"), brickMan, null);
	
//		HAPUtilityBundle.getBrickBundle(new HAPResourceIdSimple(HAPEnumBrickType.MODULE_100.getBrickType(), "1.0.0", "basic"), brickMan, null);
	}

}

@Configuration
@ComponentScan(basePackages = "com.nosliw") // Specify the base package to scan
class AppConfig {
    // You can define @Bean methods here if needed for explicit bean creation
}
