package com.nosliw.app.instance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.app.service.HAPEntityService;
import com.nosliw.app.service.HAPRequestInfo;
import com.nosliw.app.service.HAPServices;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPClientContext;
import com.nosliw.entity.dataaccess.HAPClientContextInfo;
import com.nosliw.entity.dataaccess.HAPDataContext;
import com.nosliw.entity.dataaccess.HAPEntityRequestInfo;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryDefinitionManager;
import com.nosliw.uiresource.HAPUIResource;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUITagManager;

public class HAPApplicationClientContext extends HAPClientContext  implements HAPServices, HAPEntityService{

	//store all the history result by request Id
	private Map<String, Object> m_requestResultHistory;
	
	public HAPApplicationClientContext(HAPClientContextInfo clientContextInfo,
			HAPDataContext dataContext) {
		super(clientContextInfo, dataContext);
		this.m_requestResultHistory = new LinkedHashMap<String, Object>();
	}

	public Object getRequestResultHistory(HAPRequestInfo reqInfo){  return this.m_requestResultHistory.get(reqInfo.requestId);	}
	public Object removeRequestResultHistory(HAPRequestInfo reqInfo){ return this.m_requestResultHistory.remove(reqInfo.requestId); }
	public void addReqeustResultHistory(HAPRequestInfo reqInfo, Object result){  this.m_requestResultHistory.put(reqInfo.requestId, result); }
	
	@Override
	public HAPServiceData getRelatedDataType(HAPDataTypeInfo info){
		Map<String, List<HAPDataType>> dataTypes = this.getDataTypeManager().getRelatedAllVersionsDataType(info);
		return HAPServiceData.createSuccessData(dataTypes);
	}
	
	@Override
	public HAPServiceData getDataTypeOperationScript(HAPDataTypeInfo info){
		String script = this.getDataTypeManager().getDataTypeOperationScript(info);
		return HAPServiceData.createSuccessData(script);
	}
	
	
	@Override
	public HAPServiceData getAllDataTypes(){
		HAPDataType[] data =  this.getDataTypeManager().getAllDataTypes();
		Map<String, HAPDataType> map = new LinkedHashMap<String, HAPDataType>();
		for(HAPDataType d : data){
			map.put(d.getDataTypeInfo().toString(), d);
		}
		return HAPServiceData.createSuccessData(map);
	}
	
	@Override
	public HAPServiceData getAllEntityDefinitions() {
		Map<String, HAPEntityDefinitionCritical> data = this.getEntityDefinitionManager().getAllEntityDefinitions();
		Map<String, HAPEntityDefinitionCritical> map = new LinkedHashMap<String, HAPEntityDefinitionCritical>();
		map.putAll(data);
		return HAPServiceData.createSuccessData(map);
	}

	@Override
	public HAPServiceData getEntityDefinitionsByGroup(String group) {
		HAPEntityDefinitionCritical[] data = this.getEntityDefinitionManager().getEntityDefinitionsByGroup(group);
		Map<String, HAPEntityDefinitionCritical> map = new LinkedHashMap<String, HAPEntityDefinitionCritical>();
		for(HAPEntityDefinitionCritical d : data){
			map.put(d.getEntityName(), d);
		}
		return HAPServiceData.createSuccessData(map);
	}

	@Override
	public HAPServiceData getEntityDefinitionByName(String name) {
		HAPEntityDefinitionCritical data = this.getEntityDefinitionManager().getEntityDefinition(name);
		return HAPServiceData.createSuccessData(data);
	}

	@Override
	public HAPServiceData startTransaction() {
		return this.getDataContext().startTransactionRequest();
	}

	@Override
	public HAPServiceData commit() {
		return this.getDataContext().commitRequest();
	}

	@Override
	public HAPServiceData rollBack() {
		return this.getDataContext().rollBackRequest();
	}

	@Override
	public HAPServiceData query(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo) {
		HAPQueryComponent data = this.getDataContext().queryRequest(query, queryParms, pageInfo); 
		return HAPServiceData.createSuccessData(data);
	}

	@Override
	public HAPServiceData remvoeQuery(String queryId) {
		this.getDataContext().removeQueryRequest(queryId);
		return HAPServiceData.createSuccessData();
	}
	
	@Override
	public HAPServiceData getEntityWrapers(HAPEntityRequestInfo requestInfo) {
		Map<String, HAPServiceData> wrapers = this.getDataContext().getEntitysRequest(requestInfo); 
		return HAPServiceData.createSuccessData(wrapers);
	}

	@Override
	public HAPServiceData operate(HAPEntityOperationInfo[] operations, HAPRequestInfo reqInfo) {
//		return this.getDataContext().operateRequest(operations);
//		return this.getApplicationContext().getUserContext(userContextInfo).operateRequest(operations, transactionId);
		return null;
	}

	@Override
	public HAPServiceData operate(HAPEntityOperationInfo operation, HAPRequestInfo reqInfo){
		return this.getDataContext().operateRequest(operation);
	}

	@Override
	public HAPServiceData getUIResource(String name) {
		HAPUIResource data = this.getUIResourceManager().getUIResource(name);
		return HAPServiceData.createSuccessData(data);
	}

	@Override
	public HAPServiceData getAllUIResources() {
		HAPUIResource[] data = this.getUIResourceManager().getAllUIResource(); 
		Map<String, HAPUIResource> map = new LinkedHashMap<String, HAPUIResource>();
		for(HAPUIResource d : data){
			map.put(d.getId(), d);
		}
		return HAPServiceData.createSuccessData(HAPJsonUtility.getMapObjectJson(map));
	}

	@Override
	public HAPServiceData getAttributeOptionsData(HAPEntityID ID, String attrPath){
		/*
		HAPEntityWraper[] wrapers = this.getApplicationContext().getUserContext(userContextInfo).getEntityWrapersRequest(new HAPEntityID[]{ID}, transactionId);
		if(wrapers==null || wrapers.length<1)  return null;
		HAPEntityWraper wraper = wrapers[0];
		
		HAPDataWraper childWraper = wraper.getChildWraperByPath(attrPath);
		HAPAttributeDefinition attrDef = childWraper.getAttributeDefinition();
		if(attrDef==null)  return null;
		
		HAPOptionsDefinition optionsDef = attrDef.getOptionsDefinition();
		Map<String, HAPDataWraper> wraperParms = new LinkedHashMap<String, HAPDataWraper>();
		wraperParms.put("this", wraper);
		HAPWraper[] optionsWrapers = optionsDef.getOptions(null, wraperParms);
		
		HAPContainerOptionsData out = this.getApplicationInstance().getOptionsDefinitionManager().createOptionsContainerData();
		out.setEleDataTypeInfo(optionsDef.getDataTypeInfo());
		for(HAPWraper optionsWraper : optionsWrapers){
			out.addOptionsData(optionsWraper);
		}
		
		HAPContainerOptionsWraper optionsContainerWraper = new HAPContainerOptionsWraper(out, this.getApplicationInstance().getDataTypeManager());
		return HAPServiceData.createSuccessData(optionsContainerWraper);
		*/
		return null;
	}
	
	protected HAPDataTypeManager getDataTypeManager() {	return this.getAppInstance().getDataTypeManager();	}
	protected HAPEntityDefinitionManager getEntityDefinitionManager() {  return this.getAppInstance().getEntityDefinitionManager();	}
	protected HAPQueryDefinitionManager getQueryDefinitionManager() { return this.getAppInstance().getQueryDefinitionManager(); }
	protected HAPOptionsDefinitionManager getOptionsManager() { return this.getAppInstance().getOptionsDefinitionManager();	}
	protected HAPUIResourceManager getUIResourceManager(){	return this.getAppInstance().getUIResourceManager();	}
	protected HAPUITagManager getUITagManager(){	return this.getAppInstance().getUITagManager();	}
	protected HAPApplicationInstance getAppInstance(){ return HAPApplicationInstance.getApplicationInstantce();}
}
