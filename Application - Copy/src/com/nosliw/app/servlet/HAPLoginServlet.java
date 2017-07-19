package com.nosliw.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nosliw.app.instance.HAPApplicationClientInfo;
import com.nosliw.app.instance.HAPApplicationInstance;
import com.nosliw.app.utils.HAPAttributeConstant;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.entity.dataaccess.HAPClientContext;

public class HAPLoginServlet  extends HttpServlet{

	private static final long serialVersionUID = 7816697210347287099L;

	public void doPost (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		try{
			String content = "";
			String command = request.getParameter(HAPAttributeConstant.SERVLETPARMS_COMMAND);
			String clientId = request.getParameter(HAPAttributeConstant.SERVLETPARMS_CLIENTID);
			if(HAPConstant.SERVICENAME_LOGIN.equals(command)){
				HAPApplicationClientInfo clientContextInfo = new HAPApplicationClientInfo();
				HAPClientContext clientContext = HAPApplicationInstance.getApplicationInstantce().getClientContextManager().newClientContext(clientContextInfo);
				HAPServiceData out = HAPServiceData.createSuccessData(new HAPServiceData[]{HAPServiceData.createSuccessData(clientContext.getClientContextInfo().clientId)});
				content = out.toStringValue(HAPSerializationFormat.JSON);
				
				System.out.println("*********************** Start Login ************************");
				System.out.println("login " + content);
				System.out.println("*********************** End Login ************************");
			}
			
			response.setContentType("application/json");
		    PrintWriter writer = response.getWriter();
		    
		    writer.println(HAPJsonUtility.formatJson(content));		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
