package com.nosliw.app.instance;

import java.io.InputStream;

import com.nosliw.app.log.HAPFileLogger;
import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.log.HAPLogger;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.dataaccess.HAPClientContextManager;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.query.HAPQueryDefinitionManager;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUITagManager;

public class HAPApplicationInstance{
	
	private HAPApplicationDataTypeManager m_dataTypeMan;
	
	private HAPApplicationEntityDefinitionManager m_entityDefMan;
	
	private HAPApplicationOptionsDefinitionManager m_optionsDefMan;
	
	private HAPApplicationUIResourceManager m_uiResourceMan;
	
	private HAPApplicationUITagManager m_uiTagMan;
	
	private HAPApplicationQueryDefinitionManager m_queryDefMan;
	
	private HAPConfigure m_configure;
	
	private HAPApplicationClientContextManager m_clientContextMan;
	
	private HAPLogger m_logger;
	
	private HAPApplicationInstance(HAPConfigure configure){
		if(configure!=null){
			//if input is not null, use it as configure
			this.m_configure = configure;
		}
		else{
			//try to read from file in classpath : nosliw.property
			String filename = "nosliw.properties";
    		InputStream input = HAPApplicationInstance.class.getClassLoader().getResourceAsStream(filename);
    		if(input!=null){
    			this.m_configure = new HAPConfigureImp().importFromFile(input);
    		}
    		else			this.m_configure = new HAPConfigureImp();
		}

		this.m_logger = new HAPFileLogger(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_LOGGER));
		
		this.m_dataTypeMan = new HAPApplicationDataTypeManager(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_DATATYPE));
		this.m_dataTypeMan.init();
		this.m_optionsDefMan = new HAPApplicationOptionsDefinitionManager(this.getDataTypeManager());
		this.m_entityDefMan = new HAPApplicationEntityDefinitionManager(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_ENTITYDEFINITION), this.m_dataTypeMan, this.m_optionsDefMan);
		this.m_queryDefMan = new HAPApplicationQueryDefinitionManager(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_QUERYDEFINITION), this.getDataTypeManager(), this.getEntityDefinitionManager());
		
		this.m_uiResourceMan = new HAPApplicationUIResourceManager(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_UIRESOURCE), this.getDataTypeManager());
		this.m_uiTagMan = new HAPApplicationUITagManager(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_UITAG));
		
		this.m_clientContextMan = new HAPApplicationClientContextManager(this.m_configure.getConfigurableValue(HAPConstant.APPLICATION_CONFIGURE_USERENV),
															this.getDataTypeManager(),
															this.getEntityDefinitionManager(),
															this.getQueryDefinitionManager(),
															this.getOptionsDefinitionManager());
	}
	
	public void init() {}

	public HAPDataTypeManager getDataTypeManager() {	return this.m_dataTypeMan;	}
	public HAPEntityDefinitionManager getEntityDefinitionManager() {  return this.m_entityDefMan;	}
	public HAPUIResourceManager getUIResourceManager(){	return this.m_uiResourceMan;	}
	public HAPUITagManager getUITagManager(){	return this.m_uiTagMan;	}
	public HAPQueryDefinitionManager getQueryDefinitionManager() { return this.m_queryDefMan; }
	public HAPOptionsDefinitionManager getOptionsDefinitionManager() { return this.m_optionsDefMan;	}
	public HAPClientContextManager getClientContextManager(){ return this.m_clientContextMan;}
	public HAPLogger getLogger(){return this.m_logger;}
	
	private static HAPApplicationInstance m_appInstance;
	public static HAPApplicationInstance getApplicationInstantce(){
		if(m_appInstance==null){
			m_appInstance = new HAPApplicationInstance(null);
		}
		return m_appInstance;
	}
	
}

