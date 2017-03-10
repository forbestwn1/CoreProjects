package com.nosliw.data.datatype.importer;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPDataTypeCriteriaManagerImp implements HAPDataTypeCriteriaManager{

	private HAPDBAccess m_dbAccess;
	
	@Override
	public Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to) {
		Set<HAPDataTypeId> out = null;
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

		if(from!=null){
			fromSet = new HashSet<HAPDataTypeId>();
			HAPDataTypeFamilyImp fromFamily = this.m_dbAccess.getDataTypeFamily((HAPDataTypeIdImp)from);
			Set<HAPRelationship> relationships = (Set<HAPRelationship>)fromFamily.getRelationships();
			for(HAPRelationship relationship : relationships){
				fromSet.add(relationship.getSourceDataTypeName());
			}
		}
		
		if(to==null)  out = fromSet;
		else if(from==null)  out = toSet;
		else out = Sets.intersection(fromSet, toSet);
		return out;
	}

	@Override
	public HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds) {
		return null;
	}

}
