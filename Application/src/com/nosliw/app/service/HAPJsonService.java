package com.nosliw.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.app.instance.HAPApplicationClientContext;
import com.nosliw.app.instance.HAPApplicationInstance;
import com.nosliw.app.utils.HAPAttributeConstant;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.log.HAPLogger;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.entity.data.HAPEntityID;

public class HAPJsonService {
	
	static public HAPServiceData service(String command, JSONObject jsonParms, HAPApplicationClientContext clientContext){
		HAPLogger logger = HAPApplicationInstance.getApplicationInstantce().getLogger();
		String jsonOut = ""; 
		HAPServiceData serviceData = null;
		try{
			StringBuffer out = new StringBuffer();

			boolean logData = false;
			boolean logAppending = true;
			String categary = command;
			String title = command;
			
			if(command!=null){
				if(HAPConstant.CONS_REMOTESERVICE_GETUIRESOURCE.equals(command)){
					String name = jsonParms.optString(HAPAttributeConstant.ATTR_REQUEST_GETUIRESOURCE_NAME);
					serviceData = clientContext.getUIResource(name);
				}
				else if(HAPConstant.CONS_REMOTESERVICE_GETDATATYPES.equals(command)){
					JSONArray existingArray = jsonParms.optJSONArray(HAPAttributeConstant.ATTR_REQUEST_GETDATATYPES_EXISTINGARRAY);
					JSONArray requestArray = jsonParms.optJSONArray(HAPAttributeConstant.ATTR_REQUEST_GETDATATYPES_REQUESTARRAY);

					Set<String> existingDataTypes = new HashSet<String>(); 
					if(existingArray!=null){
						for(int i=0; i<existingArray.length(); i++){
							JSONObject existingJson = existingArray.getJSONObject(i);
							HAPDataTypeInfo dataTypeInfo = HAPDataTypeInfo.parse(existingJson);
							existingDataTypes.add(dataTypeInfo.getKey());
						}
					}
					
					Map<String, List<HAPDataType>> outDataTypes = new LinkedHashMap<String, List<HAPDataType>>(); 
					for(int i=0; i<requestArray.length(); i++){
						JSONObject requestJson = requestArray.getJSONObject(i);
						HAPDataTypeInfo dataTypeInfo = HAPDataTypeInfo.parse(requestJson);
						if(!existingDataTypes.contains(dataTypeInfo.getKey())){
							Map<String, List<HAPDataType>> dataTypes = (Map<String, List<HAPDataType>>)clientContext.getRelatedDataType(dataTypeInfo).getData();
							for(String key : dataTypes.keySet()){
								if(!existingDataTypes.contains(key)){
									outDataTypes.put(key, dataTypes.get(key));
									existingDataTypes.add(key);
								}
							}
						}
					}
					serviceData = HAPServiceData.createSuccessData(outDataTypes);
				}
				else if(HAPConstant.CONS_REMOTESERVICE_EXECUTEEXPRESSION.equals(command)){
					
				}
				else if("getEntityDefinitionsByGroup".equals(command)){
					String group = jsonParms.optString("group");
					serviceData = clientContext.getEntityDefinitionsByGroup(group);
				}
				else if(HAPConstant.CONS_REMOTESERVICE_GETALLENTITYDEFINITIONS.equals(command)){
					logAppending = false;
					serviceData = clientContext.getAllEntityDefinitions();
				}
				else if(HAPConstant.CONS_REMOTESERVICE_GETENTITYDEFINITIONBYNAMES.equals(command)){
					Map<String, Object> outData = new LinkedHashMap<String, Object>();
					JSONArray names = jsonParms.optJSONArray(HAPAttributeConstant.ATTR_REQUEST_GETENTITYDEFINITIONBYNAMES_NAMES);
					for(int i=0; i<names.length(); i++){
						String name = names.optString(i);
						HAPServiceData s = clientContext.getEntityDefinitionByName(name);
						if(s.isSuccess())  outData.put(name, s.getData());
					}
					serviceData = HAPServiceData.createSuccessData(outData);
				}
				
				else if("getEntityWrapers".equals(command)){
					JSONArray jsonIDs = jsonParms.optJSONArray("IDs");
					List<HAPEntityID> entityIDs = new ArrayList<HAPEntityID>();
					for(int i=0; i<jsonIDs.length(); i++)		entityIDs.add(new HAPEntityID(jsonIDs.optString(i)));
//					serviceData = clientContext.getEntityWrapers(entityIDs.toArray(new HAPEntityID[0]));
				}
				
				else if("operate".equals(command)){
//					categary = "operate";
//					logData = true;
//					HAPEntityOperationInfo operation = HAPEntityOperationInfo.parseJson(new JSONObject(jsonParms.optString("operation")), this.getApplicationContext());
//					title = operation.getOperation().getName();
//					serviceData = clientContext.operate(operation);
				}
				else if("operateEntity".equals(command)){
//					categary = "operate";
//					logData = true;
//					JSONArray operationsJsonArray = jsonParms.getJSONArray("operations");
//					List<HAPEntityOperationInfo> operations = new ArrayList<HAPEntityOperationInfo>();
//					for(int i=0; i<operationsJsonArray.length(); i++){
//						JSONObject operationJson = operationsJsonArray.getJSONObject(i);
//						HAPEntityOperationInfo operation = HAPEntityOperationInfo.parseJson(operationJson, this.getApplicationContext());
//
//						title = "[";
//						if(i!=0)		title = title + "-" + operation.getOperation().getName();
//						else   title = title + "" + operation.getOperation().getName();
//						title = title + "]";
//
//						operations.add(operation);
//					}
//					serviceData = clientContext.operateEntity(operations.toArray(new HAPEntityOperationInfo[0]));
				}
				
				else if("startTransaction".equals(command)){
					logData = true;
					serviceData = clientContext.startTransaction();
				}
				else if("commit".equals(command)){
					categary = "operate";
					logData = true;
					serviceData = clientContext.commit();
				}
				else if("rollback".equals(command)){
					categary = "operate";
					logData = true;
					serviceData = clientContext.rollBack();
				}
				
				else if("query".equals(command)){
					logData = true;
					JSONObject query = new JSONObject(jsonParms.optString("query"));
//					serviceData = clientContext.query(HAPQueryInfo.parse(query, this.getApplicationContext().getDataTypeManager()), transactionId, userContextInfo);
				}
				else if("remvoeQuery".equals(command)){
					logData = true;
				}

				
				else if("getAllUIResources".equals(command)){
					logAppending = false;
					serviceData = clientContext.getAllUIResources();
				}
				else if("getAllDataTypes".equals(command)){
					logAppending = false;
					Map<String, String> dataTypesJson = new LinkedHashMap<String, String>();
					serviceData = clientContext.getAllDataTypes();
				}
				else if("getAttributeOptions".equals(command)){
					HAPEntityID entityID = new HAPEntityID(jsonParms.optString("ID"));
					String attrPath = jsonParms.optString("attrPath", "");
					serviceData = clientContext.getAttributeOptionsData(entityID, attrPath);
				}
			}
			
			if(serviceData==null){
				serviceData = HAPServiceData.createFailureData(null, "No command exist : " + command + "  !!!!!");
			}
			
			out.append(serviceData.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
			
			jsonOut = HAPJsonUtility.formatJson(out.toString());

			Map<String, String> logJsonMap = new LinkedHashMap<String, String>();
			logJsonMap.put("command", command);
			logJsonMap.put("parms", jsonParms.toString());
			logJsonMap.put("output", out.toString());
			logJsonMap.put("clientId", clientContext.getClientContextInfo().clientId);
			if(logData)			logJsonMap.put("data", clientContext.getDataContext().toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
			String logContent = HAPJsonUtility.getMapJson(logJsonMap);
			logger.log(title, categary, logContent, logAppending);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return serviceData;
	}
	
}
