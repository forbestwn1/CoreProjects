package com.nosliw.app.servlet;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.app.utils.HAPApplicationErrorUtility;
import com.nosliw.app.utils.HAPAttributeConstant;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

@HAPEntityWithAttribute
public abstract class HAPServiceServlet  extends HttpServlet{

	@HAPAttribute
	public static final String SERVLETPARMS_COMMAND = "command";
	@HAPAttribute
	public static final String SERVLETPARMS_SERVICE = "service";
	@HAPAttribute
	public static final String SERVLETPARMS_CLIENTID = "clientId";
	@HAPAttribute
	public static final String SERVLETPARMS_PARMS = "parms";
	
	@HAPAttribute
	public static final String REQUEST_TYPE = "type";
	@HAPAttribute
	public static final String REQUEST_SERVICE = "service";
	@HAPAttribute
	public static final String REQUEST_MODE = "mode";
	@HAPAttribute
	public static final String REQUEST_CHILDREN = "children";
	
	public void doPost (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String content = "";
			String command = request.getParameter(SERVLETPARMS_COMMAND);
			String clientId = request.getParameter(SERVLETPARMS_CLIENTID);
			HAPServiceData out = null;
			if(HAPConstant.SERVICECOMMAND_GROUPREQUEST.equals(command)){
				String groupRequest = request.getParameter(SERVLETPARMS_PARMS);
				JSONArray jsonGroupReqs = new JSONArray(groupRequest);

				List<String> requestsResult = new ArrayList<String>();
				for(int i=0; i<jsonGroupReqs.length(); i++){
					JSONObject req = jsonGroupReqs.getJSONObject(i);
					HAPServiceData serviceData = processRequest(req);
					String requestResult = serviceData.toStringValue(HAPSerializationFormat.JSON);
					requestsResult.add(requestResult);
				}
				out = HAPServiceData.createSuccessData(requestsResult);
				content = out.toStringValue(HAPSerializationFormat.JSON);
			}
			
			response.setContentType("application/json");
		    PrintWriter writer = response.getWriter();
		    
//		    System.out.println(HAPJsonUtility.formatJson(content));
		    writer.println(HAPJsonUtility.formatJson(content));		
//		    writer.println(content);		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// process one request object
	private HAPServiceData processRequest(JSONObject req) throws Exception{
		HAPServiceData out = null;
		String reqType = req.getString(REQUEST_TYPE);
		if(HAPConstant.REMOTESERVICE_TASKTYPE_NORMAL.equals(reqType)){
			//for normal request
			JSONObject serviceJson = req.getJSONObject(REQUEST_SERVICE);
			HAPServiceInfo serviceInfo = new HAPServiceInfo(serviceJson);
			out = this.processRequest(serviceInfo); 
		}
		else if(HAPConstant.REMOTESERVICE_TASKTYPE_GROUP.equals(reqType)){
			//for group task, 
			boolean success = true;
			String mode = req.getString(REQUEST_MODE);
			List<HAPServiceData> serviceDatas = new ArrayList<HAPServiceData>();
			JSONArray jsonChildren = req.getJSONArray(REQUEST_CHILDREN);
			for(int j=0; j<jsonChildren.length(); j++){
				HAPServiceData serviceData = this.processRequest(jsonChildren.getJSONObject(j));
				serviceDatas.add(serviceData);
				if(serviceData.isFail()) {
					//if one child task fail, then stop processing 
					success = false;
					break;
				}
			}
			
			if(success==false){
				if(HAPConstant.REMOTESERVICE_GROUPTASK_MODE_ALWAYS.equals(mode)){
					//if group task mode is always, group task end with success 
					success = true;
				}
			}
			
			if(success)		out = HAPServiceData.createSuccessData(serviceDatas);
			else			out = HAPServiceData.createFailureData(serviceDatas, "");
		}
		return out;
	}
	
	private HAPServiceData processRequest(HAPServiceInfo serviceInfo){
		System.out.println("*********************** Start Service ************************");
		System.out.println(HAPServiceInfo.SERVICE_COMMAND + "  " + serviceInfo.getCommand());
		System.out.println(HAPServiceInfo.SERVICE_PARMS + "   " + serviceInfo.getParms().toString());
		
		HAPServiceData serviceData = processServiceRequest(serviceInfo.getCommand(), serviceInfo.getParms());
		
		String content = serviceData.toStringValue(HAPSerializationFormat.JSON);
		content = HAPJsonUtility.formatJson(content);
		
		System.out.println("return: \n" + content);
		System.out.println("*********************** End Service ************************");
		return serviceData;
	}
	
	abstract protected HAPServiceData processServiceRequest(String command, Map<String, Object> parms); 
}
