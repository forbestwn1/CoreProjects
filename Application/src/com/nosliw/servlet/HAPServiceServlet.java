package com.nosliw.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public abstract class HAPServiceServlet extends HAPBaseServlet{

	@HAPAttribute
	public static final String REQUEST_TYPE = "type";
	@HAPAttribute
	public static final String REQUEST_SERVICE = "service";
	@HAPAttribute
	public static final String REQUEST_MODE = "mode";
	@HAPAttribute
	public static final String REQUEST_CHILDREN = "children";

	private final static Logger LOGGER = Logger.getLogger(HAPServiceServlet.class.getName());
	
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HAPServiceData out = null;
		HAPRequestInfo requestInfo = new HAPRequestInfo(request);
		if(HAPConstant.SERVICECOMMAND_GROUPREQUEST.equals(requestInfo.getCommand())){
			JSONArray jsonGroupReqs = new JSONArray(requestInfo.getParms());

			List<String> requestsResult = new ArrayList<String>();
			for(int i=0; i<jsonGroupReqs.length(); i++){
				JSONObject req = jsonGroupReqs.getJSONObject(i);
				HAPServiceData serviceData = processRequest(req);
				String requestResult = serviceData.toStringValue(HAPSerializationFormat.JSON_FULL);
				requestsResult.add(requestResult);
			}
			out = HAPServiceData.createSuccessData(requestsResult);
		}

		//build response
		this.printContent(out, response);
	}
	
	// process one request object
	private HAPServiceData processRequest(JSONObject req){
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
		StringBuffer logContent = new StringBuffer();
		
		logContent.append("\n");
		logContent.append("*********************** Start Service ************************");
		logContent.append("\n");
		logContent.append(HAPServiceInfo.SERVICE_COMMAND + "  " + serviceInfo.getCommand());
		logContent.append("\n");
		logContent.append(HAPServiceInfo.SERVICE_PARMS + "   " + serviceInfo.getParms().toString());
		logContent.append("\n");
		
		HAPServiceData serviceData = null;
		try {
			serviceData = processServiceRequest(serviceInfo.getCommand(), serviceInfo.getParms());
		}
		catch(Exception e) {
			serviceData = HAPServiceData.createFailureData(e, "Exceptione during process gateway service request!!!!");
			LOGGER.severe(HAPBasicUtility.toString(e));
		}
		
		String content = serviceData.toStringValue(HAPSerializationFormat.JSON_FULL);
		content = HAPJsonUtility.formatJson(content);
		
		logContent.append("return: \n" + content);
		logContent.append("\n");
		logContent.append("*********************** End Service ************************");
		logContent.append("\n");
		
//		LOGGER.info(logContent.toString());
		return serviceData;
	}
	
	abstract protected HAPServiceData processServiceRequest(String command, JSONObject parms)  throws Exception;
	
}
