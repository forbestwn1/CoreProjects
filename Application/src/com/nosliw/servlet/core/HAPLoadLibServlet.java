package com.nosliw.servlet.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.servlet.HAPBaseServlet;
import com.nosliw.servlet.HAPRequestInfo;

public class HAPLoadLibServlet  extends HAPBaseServlet{

	private static final long serialVersionUID = -5596373369377828141L;
	public final static String REQUEST_LOADLIBRARYRESOURCES = "requestLoadLibraryResources";

	@Override
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		HAPRequestInfo requestInfo = new HAPRequestInfo(request);
		HAPServiceData serviceData = this.getRuntimeEnvironment().getGatewayManager().executeGateway(
				HAPRuntimeEnvironmentImpBrowser.GATEWAY_LOADLIBRARIES, 
				HAPGatewayBrowserLoadLibrary.COMMAND_LOADLIBRARY, 
				new JSONObject(requestInfo.getParms()), 
				new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_BROWSER));
		this.printContent(serviceData, response);
	}
	
}
