package com.nosliw.servlet.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayOutput;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.servlet.HAPBaseServlet;
import com.nosliw.servlet.HAPRequestInfo;

public class HAPLoadLibServlet  extends HAPBaseServlet{

	private static final long serialVersionUID = -5596373369377828141L;
	public final static String REQUEST_LOADLIBRARYRESOURCES = "requestLoadLibraryResources";

	private String m_libraryTempFile = null;
	
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

		if(HAPSystemUtility.getConsolidateLib()) {
			if(this.m_libraryTempFile==null) {
				this.m_libraryTempFile = "temp/libs/"+System.currentTimeMillis()+"/library.js";
				HAPGatewayOutput gatewayOutput = (HAPGatewayOutput)serviceData.getData();
				List<String> fileNames = (List<String>)gatewayOutput.getData();
				StringBuffer libraryContent = new StringBuffer();
				for(String fileName : fileNames) {
					libraryContent.append(HAPFileUtility.readFile(HAPFileUtility.getJSFolder()+fileName));
				}
				HAPFileUtility.writeFile(HAPFileUtility.getJSFolder()+""+m_libraryTempFile, libraryContent.toString());
			}
			List<String> tempNames = new ArrayList<String>();
			tempNames.add(this.m_libraryTempFile);
			serviceData = HAPServiceData.createSuccessData(new HAPGatewayOutput(null, tempNames));
		}
		this.printContent(serviceData, response);
	}
}
