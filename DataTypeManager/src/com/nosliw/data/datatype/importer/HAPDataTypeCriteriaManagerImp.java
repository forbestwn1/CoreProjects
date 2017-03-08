package com.nosliw.data.datatype.importer;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPDataTypeCriteriaManagerImp implements HAPDataTypeCriteriaManager{

	private HAPDBAccess m_dbAccess;
	
	@Override
	public Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to) {
		Set<HAPDataTypeId> toSet = null;
		Set<HAPDataTypeId> fromSet = null;
		
		if(to!=null){
			toSet = new HashSet<HAPDataTypeId>();
			HAPDataTypePictureImp toPic = this.m_dbAccess.getDataTypePicture((HAPDataTypeIdImp)to);
			Set<HAPRelationship> relationships = (Set<HAPRelationship>)toPic.getRelationships();
			for(HAPRelationship relationship : relationships){
				toSet.add(relationship.getTargetDataTypeName());
			}
		}
		
		return null;
	}

	@Override
	public HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds) {
		return null;
	}

}
