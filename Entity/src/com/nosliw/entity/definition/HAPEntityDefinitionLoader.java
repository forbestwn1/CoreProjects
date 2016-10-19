package com.nosliw.entity.definition;

import java.util.Set;

import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;

/*
 * class to load entity definition into EntityDefinitionManager
 */

abstract public class HAPEntityDefinitionLoader{

	private String m_name = null;

	private HAPDataTypeManager m_dataTypeMan;
	private HAPOptionsDefinitionManager m_optionsMan;
	private HAPEntityDefinitionManager m_entityDefinitionMan;
	
	public HAPEntityDefinitionLoader(HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefinitionMan = entityDefMan;
		this.m_optionsMan = optionsMan;
	}

	/*
	 * do the loading job
	 * return : all the entity definition loaded
	 */
	abstract public Set<HAPEntityDefinitionCritical> loadEntityDefinition();

	public String getName() {	return this.m_name;	}
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefinitionMan;}
	protected HAPOptionsDefinitionManager getOptionsManager(){return this.m_optionsMan;}
}
