package com.nosliw.entity.utils;

import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.dataaccess.HAPDataContext;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.query.HAPQueryDefinitionManager;

/*
 * Hub class that provide all relevant object reference within one object
 */
public class HAPEntityEnvironment {

	private HAPDataTypeManager m_dataTypeMan;
	private HAPEntityDefinitionManager m_entityDefMan;
	private HAPOptionsDefinitionManager m_optionsMan;
	private HAPQueryDefinitionManager m_queryDefManager;
	private HAPDataContext m_entityMan;

	public HAPEntityEnvironment(HAPDataTypeManager dataTypeMan, 
								HAPEntityDefinitionManager entityDefMan, 
								HAPOptionsDefinitionManager optionsMan,
								HAPQueryDefinitionManager queryDefManager,
								HAPDataContext entityMan){
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefMan = entityDefMan;
		this.m_optionsMan = optionsMan;
		this.m_queryDefManager = queryDefManager;
		this.m_entityMan = entityMan;
	}
	
	public HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	public HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefMan;}
	public HAPOptionsDefinitionManager getOptionsManager(){return this.m_optionsMan;}
	public HAPQueryDefinitionManager getQueryDefinitionManager(){return this.m_queryDefManager;}

	public HAPDataContext getEntityManager(){return this.m_entityMan;}
	
	/*
	 * clear up : clear all the reference 
	 */
	public void clearUp(){
		this.m_dataTypeMan = null;
		this.m_entityDefMan = null;
		this.m_optionsMan = null;
		this.m_queryDefManager = null;
		this.m_entityMan = null;
	}
}
