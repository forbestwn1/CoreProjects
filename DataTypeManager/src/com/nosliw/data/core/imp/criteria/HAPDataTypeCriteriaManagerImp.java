package com.nosliw.data.core.imp.criteria;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;
import com.nosliw.data.core.imp.HAPDataTypeFamilyImp;
import com.nosliw.data.core.imp.HAPDataTypeIdImp;
import com.nosliw.data.core.imp.HAPDataTypePictureImp;
import com.nosliw.data.core.imp.io.HAPDBAccess;

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

	@Override
	public HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compatibleWith(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HAPDataTypeId getRootDataTypeId(HAPDataTypeId dataTypeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPDataTypeId> normalize(Set<HAPDataTypeId> dataTypeIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPRelationship buildConvertor(HAPDataTypeCriteria from, HAPDataTypeCriteria to) {
		// TODO Auto-generated method stub
		return null;
	}

}
