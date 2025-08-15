package com.nosliw.servlet.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.nosliw.data.core.imp.io.HAPDBSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	public static final String NAME_APP_CONTEXT = "APP_CONTEXT";
	
	@Override
	public void init() throws ServletException
	   {
			//create runtime
			ApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);

//			runtimeEnvironment.getGatewayManager().registerGateway(HAPConstantShared.GATEWAY_OPTIONS, new HAPGatewayOptions());
			
			//set runtime object to context
			this.getServletContext().setAttribute(NAME_APP_CONTEXT, appContext);
			
//			HAPAppManager appManager = new HAPAppManager();
//			this.getServletContext().setAttribute("minAppMan", appManager);
//			runtimeEnvironment.getGatewayManager().registerGateway(HAPGatewayAppData.GATEWAY_APPDATA, new HAPGatewayAppData(appManager.getAppDataManager()));
	   }
	
	@Override
    public void destroy() {
		HAPDBSource.getDefaultDBSource().destroy();
    }
}

@Configuration
@ComponentScan(basePackages = "com.nosliw") // Specify the base package to scan
class AppConfig {
    // You can define @Bean methods here if needed for explicit bean creation
}

