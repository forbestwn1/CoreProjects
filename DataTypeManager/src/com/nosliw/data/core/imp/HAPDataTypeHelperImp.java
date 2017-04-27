package com.nosliw.data.core.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeFamily;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementIds;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementRange;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaOr;
import com.nosliw.data.core.imp.io.HAPDBAccess;

public class HAPDataTypeHelperImp implements HAPDataTypeHelper{

	private HAPDBAccess m_dbAccess = HAPDBAccess.getInstance();
	
	@Override
	public HAPDataTypeOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		return this.m_dbAccess.getDataTypeOperation(dataTypeInfo, name);
	}
	
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
		if(criteria2==null){
			return criteria1==null;
		}
		else{
			Set<HAPDataTypeId> dataTypeIdSet1 = criteria1.getValidDataTypeId(this);
			Set<HAPDataTypeId> dataTypeIdSet2 = criteria2.getValidDataTypeId(this);
			return dataTypeIdSet2.containsAll(dataTypeIdSet1);
		}
	}

	@Override
	public HAPRelationship compatibleWith(HAPDataTypeId dataTypeId1, HAPDataTypeId dataTypeId2){
		return this.m_dbAccess.getRelationship(dataTypeId1, dataTypeId2);
	}
	
	@Override
	public HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		Set<HAPDataTypeId> dataTypesId1 = criteria1.getValidDataTypeId(this);
		Set<HAPDataTypeId> dataTypesId2 = criteria2.getValidDataTypeId(this);
		Set<HAPDataTypeId> andDataTypeIds = Sets.intersection(dataTypesId1, dataTypesId2);
		return this.buildDataTypeCriteria(andDataTypeIds);
	}

	@Override
	public HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds) {
		return new HAPDataTypeCriteriaElementIds(dataTypeIds);
	}

	@Override
	public Set<HAPDataTypeId> getRootDataTypeId(HAPDataTypeId dataTypeId) {
		Set<HAPRelationship> rootRelationships = this.getRootDataTypeRelationship(dataTypeId);
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		for(HAPRelationship rootRelationship : rootRelationships){
			out.add(rootRelationship.getTarget());
		}
		return out;
	}

	@Override
	public Set<HAPRelationship> getRootDataTypeRelationship(HAPDataTypeId dataTypeId){
		List<HAPRelationshipImp> rootRelationships = this.m_dbAccess.getRelationships(dataTypeId, HAPConstant.DATATYPE_RELATIONSHIPTYPE_ROOT);
		return new HashSet<HAPRelationship>(rootRelationships);
	}
	
	
	@Override
	public HAPDataTypeId getTrunkDataType(HAPDataTypeCriteria criteria) {
		List<HAPDataTypeId> dataTypeIds = new ArrayList<HAPDataTypeId>(criteria.getValidDataTypeId(this));
		HAPDataTypeId firstDataTypeId = dataTypeIds.get(0);
		HAPDataTypeFamily firstDataTypeFamily = this.m_dbAccess.getDataTypeFamily(firstDataTypeId);
		
		List<HAPRelationship> candidates = new ArrayList<HAPRelationship>();
		
		Set<HAPRelationship> fistRelationships = (Set<HAPRelationship>)firstDataTypeFamily.getRelationships();
		for(HAPRelationship firstRelationship : fistRelationships){
			boolean isCandidate = true;
			for(int i=1; i<dataTypeIds.size(); i++){
				HAPDataTypeId otherDataTypeId = dataTypeIds.get(i);
				if(this.compatibleWith(otherDataTypeId, firstRelationship.getTarget())==null){
					isCandidate = false;
					break;
				}
			}
			if(isCandidate)   candidates.add(firstRelationship);
		}
		
		if(candidates.size()==0)  return null;
		else if(candidates.size()==1)  return candidates.get(0).getTarget();
		else{
			HAPDataTypeId out = candidates.get(0).getTarget();
			for(int i=1; i<candidates.size(); i++){
				HAPDataTypeId candidateTarget = candidates.get(i).getTarget();
				if(this.compatibleWith(out, candidateTarget)!=null){
					
				}
				else if(this.compatibleWith(candidateTarget, out)!=null){
					out = candidateTarget;
				}
				else{
					return null;
				}
			}
			return out;
		}
	}

	@Override
	public HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria) {
		Set<HAPDataTypeId> dataTypeIds = criteria.getValidDataTypeId(this);
		Set<HAPDataTypeId> normalizedDataTypeIds = this.normalize(dataTypeIds);
		
		List<HAPDataTypeCriteria> criterias = new ArrayList<HAPDataTypeCriteria>();
		for(HAPDataTypeId normalizedDataTypeId : normalizedDataTypeIds){
			criterias.add(new HAPDataTypeCriteriaElementRange(normalizedDataTypeId, null));
		}
		return new HAPDataTypeCriteriaOr(criterias);
	}

	@Override
	public Set<HAPDataTypeId> normalize(Set<HAPDataTypeId> dataTypeIds1) {
		List<HAPDataTypeId> dataTypeIds = new ArrayList<HAPDataTypeId>(dataTypeIds1);
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		if(dataTypeIds.size()==0){}
		else if(dataTypeIds.size()==1)  out.add(dataTypeIds.get(0));
		else{
			out.addAll(dataTypeIds1);
			Set<HAPDataTypeId> removes = new HashSet<HAPDataTypeId>();
			for(int i=0; i< dataTypeIds.size()-1; i++){
				for(int j=i+1; j<dataTypeIds.size(); j++){
					if(this.compatibleWith(dataTypeIds.get(i), dataTypeIds.get(j))!=null){
						removes.add(dataTypeIds.get(i));
					}
					else if(this.compatibleWith(dataTypeIds.get(j), dataTypeIds.get(i))!=null){
						removes.add(dataTypeIds.get(j));
					}
				}
			}
			out.removeAll(removes);
		}
		return out;
	}

	@Override
	public Map<HAPDataTypeId, HAPRelationship> buildConvertor(HAPDataTypeCriteria from, HAPDataTypeCriteria to) {
		Set<HAPDataTypeId> toDataTypeIds = this.normalize(to.getValidDataTypeId(this));
		Set<HAPDataTypeId> fromDataTypeIds = from.getValidDataTypeId(this);
		
		Map<HAPDataTypeId, HAPRelationship> out = new LinkedHashMap<HAPDataTypeId, HAPRelationship>();
		for(HAPDataTypeId fromDataTypeId : fromDataTypeIds){
			boolean found = false;
			for(HAPDataTypeId toDataTypeId : toDataTypeIds){
				HAPRelationship relationship = this.compatibleWith(fromDataTypeId, toDataTypeId);
				if(relationship!=null){
					out.put(fromDataTypeId, relationship);
					found = true;
					break;
				}					
			}
		}
		return out;
	}
}
