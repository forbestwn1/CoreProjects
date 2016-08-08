package com.nosliw.entity.definition;

import java.util.Set;

/*
 * class to load attribute definition into EntityDefinitionManager
 */

public abstract class HAPAttributeDefinitionLoader{

	private String m_name = null;
	
	public HAPAttributeDefinitionLoader(String name) {
		this.m_name = name;
	}

	/*
	 * do the loading job
	 * return : all the attribute definition loaded
	 */
	abstract public Set<HAPAttributeDefinition> loadEntityAttributeDefinition();

	public String getName() {return this.m_name;}
	
}
