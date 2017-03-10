package com.nosliw.data.datatype.importer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPDataType;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPDataTypePicture;
import com.nosliw.data.core.HAPRelationship;

public class HAPDataTypePictureImp implements HAPDataTypePicture{

	private HAPDataTypeImp m_sourceDataType;
	
	private Map<HAPDataTypeId, HAPRelationshipImp> m_relationships;
	
	public HAPDataTypePictureImp(HAPDataTypeImp mainDataType){
		this.m_relationships = new LinkedHashMap<HAPDataTypeId, HAPRelationshipImp>();
		this.m_sourceDataType = mainDataType;
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
		relationship.setSource(this.m_sourceDataType);
		this.m_relationships.put(relationship.getTargetDataTypeName(), relationship);
	}

	@Override
	public List<HAPDataTypeOperation> getOperations() {
		return new ArrayList(this.m_relationships.values());
	}
	
}
