package com.nosliw.data.core.imp;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.core.data.HAPDataType;
import com.nosliw.core.data.HAPDataTypeFamily;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPRelationship;

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
		relationship.setSourceDataType(this.m_targetDataType);
		this.m_relationships.put(relationship.getSource(), relationship);
	}
}
