package com.nosliw.entity.query;

import java.util.Set;

import com.nosliw.entity.definition.HAPEntityDefinitionManager;

/*
 * this class describe the secondary entity that may affect query result
 * 
 */
public class HAPSecondaryEntityInfo {

	private Set<String> m_entityGroups;
	private Set<String> m_entitys;
	private Set<String> m_allValideEntityTypes;

	private String m_pathToMajorEntity;
	
	private HAPEntityDefinitionManager m_entityDefMan;

	public HAPSecondaryEntityInfo(String pathToMajorEntity, HAPEntityDefinitionManager entityDefMan){
		this.m_pathToMajorEntity = pathToMajorEntity;
		this.m_entityDefMan = entityDefMan;
	}
	
	public String getPathToMajorEntity(){return this.m_pathToMajorEntity;}

	public Set<String> getAllValidEntityTypes(){return this.m_allValideEntityTypes;	}
	
	public void mergeWith(HAPSecondaryEntityInfo types){
		for(String group : types.m_entityGroups){
			this.addEntityGroup(group);
		}
		
		for(String type : types.m_entitys){
			this.addEntityType(type);
		}
	}
	
	public void addEntityType(String entityType){
		if(!this.m_entitys.contains(entityType)){
			this.m_entitys.add(entityType);
			this.m_allValideEntityTypes.add(entityType);
		}
	}
	
	public void addEntityGroup(String group){
		if(!this.m_entityGroups.contains(group)){
			this.m_entityGroups.add(group);
			Set<String> entitys = this.getEntityDefinitionManager().getEntityNamesByGroup(group);
			this.m_allValideEntityTypes.addAll(entitys);
		}
	}
	
	public boolean isValidEntityType(String entityType){return this.m_allValideEntityTypes.contains(entityType);}
	
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefMan;}
}
