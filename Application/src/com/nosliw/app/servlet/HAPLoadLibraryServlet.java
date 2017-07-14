package com.nosliw.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.app.instance.HAPApplicationInstance;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPDataTypeInfo;

public class HAPLoadLibraryServlet  extends HttpServlet{

	public void doPost (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		try{
			String type = request.getParameter("type");
			String infoStr = request.getParameter("info");
			JSONObject infoJson = new JSONObject(infoStr);
			String content = null;
			
			System.out.println("*********************** Start Script Load ************************");
			System.out.println("type " + type);
			System.out.println("info " + infoStr);
			System.out.println("*********************** End Script Load ************************");
			
			if(HAPConstant.SCRIPTTYPE_UIRESOURCE.equals(type)){
				//for script block in uiresource
				String name = infoJson.getString("name");
				content = HAPApplicationInstance.getApplicationInstantce().getUIResourceManager().getUIResourceScript(name);
			}
			else if(HAPConstant.SCRIPTTYPE_DATAOPERATIONS.equals(type)){
				//for data opration script
				JSONArray requestArray = infoJson.optJSONArray("requestArray");
				StringBuffer out = new StringBuffer();
				for(int i=0; i<requestArray.length(); i++){
					JSONObject requestJson = requestArray.getJSONObject(i);
					HAPDataTypeInfo dataTypeInfo = HAPDataTypeInfo.parse(requestJson);
					out.append(HAPApplicationInstance.getApplicationInstantce().getDataTypeManager().getRelatedDataTypeOperationScript(dataTypeInfo));
				}
				content = out.toString();
			}
			else if(HAPConstant.SCRIPTTYPE_UITAGS.equals(type)){
				//for ui tag
				JSONArray requestArray = infoJson.optJSONArray("requestArray");
				StringBuffer out = new StringBuffer();
				for(int i=0; i<requestArray.length(); i++){
					String uiTagName = requestArray.getString(i);
					out.append(HAPApplicationInstance.getApplicationInstantce().getUITagManager().getUITag(uiTagName).getScript());
				}
				content = out.toString();
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
