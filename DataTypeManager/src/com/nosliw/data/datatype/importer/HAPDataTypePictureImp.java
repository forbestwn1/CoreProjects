package com.nosliw.data.datatype.importer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeOperation;
import com.nosliw.data.HAPDataTypePicture;
import com.nosliw.data.HAPRelationship;

public class HAPDataTypePictureImp implements HAPDataTypePicture{

	private HAPDataTypeImp m_sourceDataType;
	
	private Map<HAPDataTypeId, HAPRelationshipImp> m_relationships;
	
	public HAPDataTypePictureImp(HAPDataTypeImp mainDataType){
		this.m_relationships = new LinkedHashMap<HAPDataTypeId, HAPRelationshipImp>();
	}
	
	@Override
	public HAPDataType getSourceDataType(){		return this.m_sourceDataType;  }

	@Override
	public HAPRelationshipImp getRelationship(HAPDataTypeId dataTypeInfo){
		return this.m_relationships.get(dataTypeInfo);
	}

	@Override
	public Set<? extends HAPRelationship> getRelationships(){
		return new HashSet( this.m_relationships.values());
	}

	public void addRelationship(HAPRelationshipImp relationship){
		this.m_relationships.put(relationship.getTargetDataType().getName(), relationship);
	}

	@Override
	public List<HAPDataTypeOperation> getOperations() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
