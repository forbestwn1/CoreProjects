package com.nosliw.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;
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
		
		HAPRequestInfo requestInfo = this.getRequestInfo(request);
		HAPServiceData serviceData = this.getRuntimeEnvironment().getGatewayManager().executeGateway(HAPRuntimeEnvironmentImpBrowser.GATEWAY_LOADLIBRARIES, HAPGatewayBrowserLoadLibrary.COMMAND_LOADLIBRARY, requestInfo.getData());
		String content = serviceData.toStringValue(HAPSerializationFormat.JSON);
		content = HAPJsonUtility.formatJson(content);
		
		response.setContentType("text/javascript");
	    PrintWriter writer = response.getWriter();
	    writer.println(content);		
	}
	

	private HAPRequestInfo getRequestInfo(HttpServletRequest request){
		HAPRequestInfo out = null;
		
		try {
			String requestDataStr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			JSONObject requestJson = new JSONObject(requestDataStr);
			
			String command = requestJson.optString(HAPRequestInfo.COMMAND);
			String clientId = requestJson.optString(HAPRequestInfo.CLIENTID);
			Object data = requestJson.opt(HAPRequestInfo.DATA);
			out = new HAPRequestInfo(clientId, command, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	private HAPRuntimeEnvironmentImpBrowser getRuntimeEnvironment(){		return (HAPRuntimeEnvironmentImpBrowser)this.getServletContext().getAttribute("runtime");  }

	
}
