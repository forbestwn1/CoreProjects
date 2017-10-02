package com.nosliw.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSLibrary;

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
		HAPServiceData serviceData = null;
		
		try{
			HAPRequestInfo requestInfo = this.getRequestInfo(request);
			String content = null;
			
			switch(requestInfo.getCommand()){
			case REQUEST_LOADLIBRARYRESOURCES:
				JSONArray libraryIdsArray = (JSONArray)requestInfo.getData();
				List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
				for(int i=0; i<libraryIdsArray.length(); i++){
					resourceIds.add(new HAPResourceIdJSLibrary(libraryIdsArray.getString(i)));
				}
				List<HAPResource> resources = this.getRuntimeEnvironment().getResourceManager().getResources(resourceIds).getLoadedResources();
				List<String> fileNames = new ArrayList<String>();
				for(HAPResource resource : resources){
					List<URI> uris = ((HAPResourceDataJSLibrary)resource.getResourceData()).getURIs();
					for(URI uri : uris){
						fileNames.add(this.getLibraryPath(uri.toString()));
					}
				}
				serviceData = HAPServiceData.createSuccessData(fileNames);
				content = serviceData.toStringValue(HAPSerializationFormat.LITERATE);
				break;
			}
			
			response.setContentType("text/javascript");
		    PrintWriter writer = response.getWriter();
		    writer.println(content);		
		}
		catch(Exception e){
			e.printStackTrace();
		}
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

	private HAPRuntimeEnvironmentImpRhino getRuntimeEnvironment(){		return (HAPRuntimeEnvironmentImpRhino)this.getServletContext().getAttribute("runtime");  }

	
	private String getLibraryPath(String fileName){
		String keyword = "WebContent";
		return fileName.substring(fileName.indexOf(keyword)+keyword.length()+1);
	}
}
