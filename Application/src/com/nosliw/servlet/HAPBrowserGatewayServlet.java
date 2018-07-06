package com.nosliw.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;

public class HAPBrowserGatewayServlet  extends HttpServlet{

	private static final long serialVersionUID = -5596373369377828141L;
	public final static String REQUEST_LOADLIBRARYRESOURCES = "requestLoadLibraryResources";

	
	public void doPost (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		HAPRequestInfo requestInfo = new HAPRequestInfo(request);
		HAPServiceData serviceData = this.getRuntimeEnvironment().getGatewayManager().executeGateway(
				HAPRuntimeEnvironmentImpBrowser.GATEWAY_LOADLIBRARIES, 
				HAPGatewayBrowserLoadLibrary.COMMAND_LOADLIBRARY, 
				new JSONObject(requestInfo.getParms()));
		String content = serviceData.toStringValue(HAPSerializationFormat.JSON);
		content = HAPJsonUtility.formatJson(content);
		
		response.setContentType("text/javascript");
		response.addHeader("Access-Control-Allow-Origin", "*");
	    PrintWriter writer = response.getWriter();
	    writer.println(content);		
	}
	
	 @Override
	  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
		 resp.addHeader("Access-Control-Allow-Origin", "*");
		 resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
	      resp.setStatus(HttpServletResponse.SC_OK);
	  }
	 
	private HAPRuntimeEnvironmentImpBrowser getRuntimeEnvironment(){		return (HAPRuntimeEnvironmentImpBrowser)this.getServletContext().getAttribute("runtime");  }

}
