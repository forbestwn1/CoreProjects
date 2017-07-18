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

import com.nosliw.app.instance.HAPApplicationInstance;
import com.nosliw.app.utils.HAPAttributeConstant;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSLibrary;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;
import com.nosliw.data1.HAPDataTypeInfo;

public class HAPBrowserGatewayServlet  extends HttpServlet{

	public final static String REQUEST_LOADLIBRARYRESOURCES = "requestLoadLibraryResources";

	
	public void doPost (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
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

	private HAPRuntimeImpJSBroswer getRuntime(){
		
	}
	
	private String getLibraryPath(String fileName){
		
	}
	
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String content = null;
		
		try{
			HAPRequestInfo requestInfo = this.getRequestInfo(request);
			
			switch(requestInfo.getCommand()){
			case REQUEST_LOADLIBRARYRESOURCES:
				JSONArray libraryIdsArray = (JSONArray)requestInfo.getData();
				List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
				for(int i=0; i<libraryIdsArray.length(); i++){
					resourceIds.add(new HAPResourceIdJSLibrary(libraryIdsArray.getString(i)));
				}
				List<HAPResource> resources = this.getRuntime().getResourceManager().getResources(resourceIds);
				List<String> fileNames = new ArrayList<String>();
				for(HAPResource resource : resources){
					HAPResourceDataJSLibrary aa;
					List<URI> uris = ((HAPResourceDataJSLibrary)resource.getResourceData()).getURIs();
					for(URI uri : uris){
						fileNames.add(this.getLibraryPath(uri.toString()));
					}
				}
				content = HAPJsonUtility.buildArrayJson(fileNames.toArray(new String[0]));
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
	
}
