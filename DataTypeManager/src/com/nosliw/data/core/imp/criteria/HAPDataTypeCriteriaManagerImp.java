package com.nosliw.data.core.imp.criteria;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementIds;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;
import com.nosliw.data.core.imp.HAPDataTypeFamilyImp;
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
			HAPDataTypePictureImp toPic = this.m_dbAccess.getDataTypePicture(to);
			Set<HAPRelationship> relationships = (Set<HAPRelationship>)toPic.getRelationships();
			for(HAPRelationship relationship : relationships){
				toSet.add(relationship.getTarget());
			}
		}

		if(from!=null){
			fromSet = new HashSet<HAPDataTypeId>();
			HAPDataTypeFamilyImp fromFamily = this.m_dbAccess.getDataTypeFamily(from);
			Set<HAPRelationship> relationships = (Set<HAPRelationship>)fromFamily.getRelationships();
			for(HAPRelationship relationship : relationships){
				fromSet.add(relationship.getSource());
			}
		}
		
		if(to==null)  out = fromSet;
		else if(from==null)  out = toSet;
		else out = Sets.intersection(fromSet, toSet);
		return out;
	}

	@Override
	public boolean compatibleWith(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		Set<HAPDataTypeId> dataTypeIdSet1 = criteria1.getValidDataTypeId();
		Set<HAPDataTypeId> dataTypeIdSet2 = criteria2.getValidDataTypeId();
		return dataTypeIdSet2.containsAll(dataTypeIdSet1);
	}

	@Override
	public HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		Set<HAPDataTypeId> dataTypesId1 = criteria1.getValidDataTypeId();
		Set<HAPDataTypeId> dataTypesId2 = criteria2.getValidDataTypeId();
		Set<HAPDataTypeId> andDataTypeIds = Sets.intersection(dataTypesId1, dataTypesId2);
		return this.buildDataTypeCriteria(andDataTypeIds);
	}

	@Override
	public HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds) {
		return new HAPDataTypeCriteriaElementIds(dataTypeIds, this);
	}

	@Override
	public HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPDataTypeId> getRootDataTypeId(HAPDataTypeId dataTypeId) {
		HAPDataTypeFamilyImp dataTypeFamily = this.m_dbAccess.getDataTypeFamily(dataTypeId);
		dataTypeFamily.get
		for(HAPDAta)
		
		
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		Set<HAPDataTypeId> processed  = new HashSet<HAPDataTypeId>();
		this.getRootDataTypeId(dataTypeId, out, processed);
		
		return null;
	}

	private void getRootDataTypeId(HAPDataTypeId dataTypeId, Set<HAPDataTypeId> out, Set<HAPDataTypeId> processed){
		if(!processed.contains(dataTypeId)){
			
		}
	}
	
	
	@Override
	public Set<HAPDataTypeId> normalize(Set<HAPDataTypeId> dataTypeIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<HAPDataTypeId, HAPRelationship> buildConvertor(HAPDataTypeCriteria from, HAPDataTypeCriteria to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDataTypeId getTrunkDataType(HAPDataTypeCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
}
