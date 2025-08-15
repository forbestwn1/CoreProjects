package com.nosliw.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.context.ApplicationContext;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.gateway.HAPGatewayManager;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.servlet.core.HAPInitServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HAPBaseServlet  extends HttpServlet{

	@Override
	protected void doPost (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	 @Override
	  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
		 resp.addHeader("Access-Control-Allow-Origin", "*");
		 resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
	      resp.setStatus(HttpServletResponse.SC_OK);
	  }
	
	 protected void printContent(HAPServiceData serviceData, HttpServletResponse response) throws IOException {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			//cross domain
			response.addHeader("Access-Control-Allow-Origin", "*");
			PrintWriter writer = response.getWriter();
			try {
				String content = serviceData.toStringValue(HAPSerializationFormat.JSON_FULL);
//			    System.out.println(HAPJsonUtility.formatJson(content));
			    writer.println(HAPUtilityJson.formatJson(content));		
//			    writer.println(content);		
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
			finally {
				writer.flush();
				writer.close();
	        }  
	 }
	 
	 protected ApplicationContext getAppContext(){		return (ApplicationContext)this.getServletContext().getAttribute(HAPInitServlet.NAME_APP_CONTEXT);  }
	 protected HAPGatewayManager getGatewayManager() {    return this.getAppContext().getBean(HAPGatewayManager.class);    }
	 protected HAPManagerResource getResourceManager() {     return this.getAppContext().getBean(HAPManagerResource.class);     }
}
