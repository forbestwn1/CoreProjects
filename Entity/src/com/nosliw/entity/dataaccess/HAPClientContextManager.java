package com.nosliw.entity.dataaccess;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.query.HAPQueryDefinitionManager;

public abstract class HAPClientContextManager implements HAPSerializable{

	private int m_nextId = 100;
	
	protected Map<String, HAPClientContext> m_clientContexts;
	
	private HAPConfigure m_configures;

	private HAPDataTypeManager m_dataTypeMan; 
	private HAPEntityDefinitionManager m_entityDefMan; 
	private HAPQueryDefinitionManager m_queryDefMan;
	private HAPOptionsDefinitionManager m_optionsDefMan;
	
	public HAPClientContextManager(HAPConfigure configures,
							HAPDataTypeManager dataTypeMan, 
							HAPEntityDefinitionManager entityDefMan, 
							HAPQueryDefinitionManager queryDefMan,
							HAPOptionsDefinitionManager optionsDefMan){
		this.m_configures = configures;
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefMan = entityDefMan;
		this.m_queryDefMan = queryDefMan;
		this.m_optionsDefMan = optionsDefMan;
		this.m_clientContexts = new LinkedHashMap<String, HAPClientContext>();
	}
	
	public HAPClientContext newClientContext(HAPClientContextInfo clientContextInfo) {
		//create clientId and set clientContextInfo
		clientContextInfo.clientId = this.getNextId();
		
		HAPDataContextInfo dataContextInfo = this.getDataContextInfo(clientContextInfo);
		HAPEntityPersistent entityPersistent = this.createHAPEntityPersistent(dataContextInfo);
		HAPDataContext dataContext = new HAPDataContext(dataContextInfo,
						entityPersistent, 
						this.getConfigure(),
						this.getDataTypeManager(), 
						this.getEntityDefinitionManager(), 
						this.getQueryDefinitionManager(),
						this.getOptionsManager());
		entityPersistent.setDataContext(dataContext);
		HAPClientContext out = new HAPClientContext(clientContextInfo, dataContext);
		
		this.m_clientContexts.put(clientContextInfo.clientId, out);
		return out;
	}

	public HAPClientContext getClientContext(String clientId){
		return this.m_clientContexts.get(clientId);
	}
	
	protected HAPDataContextInfo getDataContextInfo(HAPClientContextInfo clientInfo){
		return null;
	}
	
	protected abstract HAPEntityPersistent createHAPEntityPersistent(HAPDataContextInfo userEnvInfo);
	
	protected String getNextId(){
		return this.m_nextId++ + "";
	}
	
	@Override
	public String toStringValue(String format) {
		return null;
	}
	
	protected HAPDataTypeManager getDataTypeManager() {	return this.m_dataTypeMan;	}
	protected HAPEntityDefinitionManager getEntityDefinitionManager() {  return this.m_entityDefMan;	}
	protected HAPQueryDefinitionManager getQueryDefinitionManager() { return this.m_queryDefMan; }
	protected HAPOptionsDefinitionManager getOptionsManager() { return this.m_optionsDefMan;	}
	protected HAPConfigure getConfigure(){ return this.m_configures; }
}
