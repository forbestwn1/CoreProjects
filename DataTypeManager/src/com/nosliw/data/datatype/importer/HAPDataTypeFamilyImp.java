package com.nosliw.data.datatype.importer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPDataType;
import com.nosliw.data.core.HAPDataTypeFamily;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;

public class HAPDataTypeFamilyImp implements HAPDataTypeFamily{

	private HAPDataTypeImp m_targetDataType;
	
	private Map<HAPDataTypeId, HAPRelationshipImp> m_relationships;
	
	public HAPDataTypeFamilyImp(HAPDataTypeImp mainDataType){
		this.m_relationships = new LinkedHashMap<HAPDataTypeId, HAPRelationshipImp>();
		this.m_targetDataType = mainDataType;
	}
	
	@Override
	public HAPDataType getTargetDataType(){		return this.m_targetDataType;  }

	@Override
	public HAPRelationshipImp getRelationship(HAPDataTypeId dataTypeInfo){
		return this.m_relationships.get(dataTypeInfo);
	}

	@Override
	public Set<? extends HAPRelationship> getRelationships(){
		return new HashSet( this.m_relationships.values());
	}

	public void addRelationship(HAPRelationshipImp relationship){
		relationship.setSource(this.m_targetDataType);
		this.m_relationships.put(relationship.getSourceDataTypeName(), relationship);
	}
}
