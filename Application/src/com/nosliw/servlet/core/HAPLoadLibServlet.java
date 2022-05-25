package com.nosliw.servlet.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayOutput;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.data.core.system.HAPSystemUtility;
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
		HAPServiceData serviceData = null;
		try {
			JSONObject parmsJson = new JSONObject(requestInfo.getParms());
			serviceData = this.getRuntimeEnvironment().getGatewayManager().executeGateway(
					HAPRuntimeEnvironmentImpBrowser.GATEWAY_LOADLIBRARIES, 
					HAPGatewayBrowserLoadLibrary.COMMAND_LOADLIBRARY, 
					parmsJson, 
					new HAPRuntimeInfo(HAPConstantShared.RUNTIME_LANGUAGE_JS, HAPConstantShared.RUNTIME_ENVIRONMENT_BROWSER));

			if(HAPSystemUtility.getConsolidateLib()) {
				if(this.m_libraryTempFile==null) {
					this.m_libraryTempFile = System.currentTimeMillis()+"/library.js";
					HAPGatewayOutput gatewayOutput = (HAPGatewayOutput)serviceData.getData();
					List<String> fileNames = (List<String>)gatewayOutput.getData();
					StringBuffer libraryContent = new StringBuffer();
					for(String fileName : fileNames) {
						//remove version part in file url first
						String file1 = fileName; 
						int i = fileName.indexOf("?");
						if(i!=-1) {
							file1 = fileName.substring(0, i);
						}
						libraryContent.append(HAPUtilityFile.readFile(HAPSystemFolderUtility.getJSFolder()+file1));
					}
					HAPUtilityFile.writeFile(HAPSystemUtility.getJSTempFolder()+"libs/"+m_libraryTempFile, libraryContent.toString());
				}
				List<String> tempNames = new ArrayList<String>();
				String libUrl = "temp/libs/"+this.m_libraryTempFile;
				String libUrlWithVersion = HAPBasicUtility.addVersionToUrl(libUrl, parmsJson.optString(HAPGatewayBrowserLoadLibrary.COMMAND_LOADLIBRARY_VERSION));
				tempNames.add(libUrlWithVersion);
				serviceData = HAPServiceData.createSuccessData(new HAPGatewayOutput(null, tempNames));
			}
		}
		catch(Exception e) {
			serviceData = HAPServiceData.createFailureData(e, "Exceptione during load library service request!!!!");
//			LOGGER.throwing(this.getClass().getName(), "service", e);
		}
		
		this.printContent(serviceData, response);
	}
}
