package com.nosliw.app.instance;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.dataaccess.HAPClientContext;
import com.nosliw.entity.dataaccess.HAPClientContextInfo;
import com.nosliw.entity.dataaccess.HAPDataContext;
import com.nosliw.entity.dataaccess.HAPEntityPersistent;
import com.nosliw.entity.dataaccess.HAPDataContextInfo;
import com.nosliw.entity.dataaccess.HAPClientContextManager;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.persistent.mango.HAPPersistentDataAccess;
import com.nosliw.entity.query.HAPQueryDefinitionManager;

public class HAPApplicationClientContextManager extends HAPClientContextManager{

	public HAPApplicationClientContextManager(HAPConfigure configures,
			HAPDataTypeManager dataTypeMan, 
			HAPEntityDefinitionManager entityDefMan, 
			HAPQueryDefinitionManager queryDefMan,
			HAPOptionsDefinitionManager optionsDefMan){
		super(configures, dataTypeMan, entityDefMan, queryDefMan, optionsDefMan);
		
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
		HAPClientContext out = new HAPApplicationClientContext(clientContextInfo, dataContext);
		
		this.m_clientContexts.put(clientContextInfo.clientId, out);
		return out;
	}
	
	protected HAPEntityPersistent createHAPEntityPersistent(HAPDataContextInfo userEnvInfo){
		HAPPersistentDataAccess out = new HAPPersistentDataAccess(this.getConfigure());
		return out;
	}
	
}
