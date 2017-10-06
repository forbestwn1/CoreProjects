package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaWrapperLiterate;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDiscovered;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeGatewayJS;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;
import com.nosliw.data.core.runtime.js.browser.HAPResponseGatewayLoadTestExpression;

@HAPEntityWithAttribute
public class HAPGatewayServlet1 extends HAPServiceServlet{

	private static final long serialVersionUID = 3449216679929442927L;

	
//	@Override
	protected HAPServiceData processServiceRequest(String command, Map<String, Object> parms) {
		HAPServiceData out = null;
		switch(command){
		case HAPRuntimeGatewayJS.REQUEST_GETEXPRESSIONS:
		{
			List<HAPResponseGatewayLoadTestExpression> expressions = new ArrayList<HAPResponseGatewayLoadTestExpression>();
			JSONArray expressionsRequest = (JSONArray)parms.get(HAPRuntimeGatewayJS.REQUEST_GETEXPRESSIONS_EXPRESSIONS);
			for(int i=0; i<expressionsRequest.length(); i++){
				JSONObject expressionRequest = expressionsRequest.optJSONObject(i);
				
				String suite = expressionRequest.optString(HAPRuntimeGatewayJS.REQUEST_GETEXPRESSIONS_ELEMENT_SUITE);
				String expressionName = expressionRequest.optString(HAPRuntimeGatewayJS.REQUEST_GETEXPRESSIONS_ELEMENT_EXPRESSIONNAME);
				JSONObject variablesObject = (JSONObject)expressionRequest.optJSONObject(HAPRuntimeGatewayJS.REQUEST_GETEXPRESSIONS_ELEMENT_VARIABLES);
				
				Map<String, HAPDataTypeCriteria> variableCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
				if(variablesObject!=null){
					Iterator varNamesIt = variablesObject.keys();
					while(varNamesIt.hasNext()){
						String varName = (String)varNamesIt.next();
						String criteria = variablesObject.optString(varName);
						HAPDataTypeCriteriaWrapperLiterate criteriaObj = new HAPDataTypeCriteriaWrapperLiterate();
						criteriaObj.buildObject(criteria, HAPSerializationFormat.LITERATE);
						variableCriterias.put(varName, criteriaObj);
					}
				}
				
				
				Map<String, HAPData> varDatas = ((HAPExpressionDefinitionSuiteImp)this.getRuntimeEnvironment().getExpressionManager().getExpressionDefinitionSuite(suite)).getVariableData();
				HAPExpression expression = this.getRuntimeEnvironment().getExpressionManager().processExpression(null, suite, expressionName, variableCriterias);
				expressions.add(new HAPResponseGatewayLoadTestExpression(expression, varDatas));
			}
			out = HAPServiceData.createSuccessData(expressions);
			break;
		}
		case HAPRuntimeGatewayJS.REQUEST_LOADRESOURCES:
		{
			//parpare data
			JSONArray resourceInfosArray = (JSONArray)parms.get(HAPRuntimeGatewayJS.REQUEST_LOADRESOURCES_RESOURCEINFOS);
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			Map<HAPResourceId, HAPResourceInfo> resourcesInfo = new LinkedHashMap<HAPResourceId, HAPResourceInfo>();
			for(int i=0; i<resourceInfosArray.length(); i++){
				HAPResourceInfo resourceInfo = new HAPResourceInfo();
				resourceInfo.buildObject(resourceInfosArray.opt(i), HAPSerializationFormat.JSON);
				resourceIds.add(resourceInfo.getId());
				resourcesInfo.put(resourceInfo.getId(), resourceInfo);  
			}
			//load resources
			HAPLoadResourceResponse loadResourceResponse = this.getRuntimeEnvironment().getResourceManager().getResources(resourceIds);
			if(loadResourceResponse.isSuccess()){
				//build script info according to resoruces
				List<HAPJSScriptInfo> scriptsInfo = new ArrayList<HAPJSScriptInfo>();
				for(HAPResource resource : loadResourceResponse.getLoadedResources()){
					HAPResourceInfo resourceInfo = resourcesInfo.get(resource.getId());
					scriptsInfo.addAll(HAPRuntimeJSScriptUtility.buildScriptForResource(resourceInfo, resource));
				}
				
				//build response data
				List<HAPJSScriptInfo> outScriptInfos = new ArrayList<HAPJSScriptInfo>(); 
				HAPJSScriptInfo runnableScript = HAPJSScriptInfo.buildByScript("", "");
				for(HAPJSScriptInfo scriptInfo : scriptsInfo){
					String fileName = scriptInfo.isFile();
					if(fileName!=null){
						
					}
					else{
						//put all script into one 
						String escaptedScript = StringEscapeUtils.escapeJavaScript(scriptInfo.getScript());
						runnableScript.appendScript(escaptedScript);
					}
				}
				outScriptInfos.add(runnableScript);
				
				out = HAPServiceData.createSuccessData(outScriptInfos);
			}
			else	out = HAPServiceData.createFailureData(loadResourceResponse.getFailedResourcesId(), "");
			break;
		}	
		case HAPRuntimeGatewayJS.REQUEST_DISCOVERRESOURCES:
		{
			JSONArray resourceIdsArray = (JSONArray)parms.get(HAPRuntimeGatewayJS.REQUEST_DISCOVERRESOURCES_RESOURCEIDS);
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			for(int i=0; i<resourceIdsArray.length(); i++){
				HAPResourceId resourceId = new HAPResourceId();
				resourceId.buildObject(resourceIdsArray.opt(i), HAPSerializationFormat.JSON);
				resourceIds.add(resourceId);
			}
			List<HAPResourceInfo> resourceInfos = this.getRuntimeEnvironment().getResourceDiscovery().discoverResource(resourceIds);
			out = HAPServiceData.createSuccessData(resourceInfos);
			break;
		}
		case HAPRuntimeGatewayJS.REQUEST_DISCOVERANDLOADRESOURCES:
		{
			JSONArray resourceIdsArray = (JSONArray)parms.get(HAPRuntimeGatewayJS.REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS);
			List<HAPResourceId> discoveryResourceIds = new ArrayList<HAPResourceId>();
			for(int i=0; i<resourceIdsArray.length(); i++){
				HAPResourceId resourceId = new HAPResourceId();
				resourceId.buildObject(resourceIdsArray.opt(i), HAPSerializationFormat.JSON);
				discoveryResourceIds.add(resourceId);
			}
			List<HAPResourceInfo> resourceInfos = this.getRuntimeEnvironment().getResourceDiscovery().discoverResource(discoveryResourceIds);

			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			Map<HAPResourceId, HAPResourceInfo> resourceInfoMap = new LinkedHashMap<HAPResourceId, HAPResourceInfo>();
			for(HAPResourceInfo resourceInfo : resourceInfos){  
				resourceIds.add(resourceInfo.getId());  
				resourceInfoMap.put(resourceInfo.getId(), resourceInfo);
			} 

			HAPLoadResourceResponse loadResourceResponse = this.getRuntimeEnvironment().getResourceManager().getResources(resourceIds);
			List<HAPResourceDiscovered> discoveredResource = new ArrayList<HAPResourceDiscovered>();
			if(loadResourceResponse.isSuccess()){
				for(HAPResource resource : loadResourceResponse.getLoadedResources())		discoveredResource.add(new HAPResourceDiscovered(resourceInfoMap.get(resource.getId()), resource));
				out = HAPServiceData.createSuccessData(discoveredResource);
			}
			else{
				out = HAPServiceData.createFailureData(loadResourceResponse.getFailedResourcesId(), "");
			}
			break;
		}
		}
		
		return out;
	}

	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
