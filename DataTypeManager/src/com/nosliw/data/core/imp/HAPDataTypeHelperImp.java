package com.nosliw.data.core.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeFamily;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaIds;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaRange;
import com.nosliw.data.core.criteria.HAPDataTypeSubCriteriaGroup;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaOr;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPDataTypeHelperImp implements HAPDataTypeHelper{

	private HAPDataAccessDataType m_dataAccess = null;
	
	public HAPDataTypeHelperImp(HAPDataAccessDataType dataAccess){
		this.m_dataAccess = dataAccess;
	}
	
	@Override
	public HAPDataTypeId getTrunkDataType(HAPDataTypeCriteria criteria) {
		List<HAPDataTypeId> dataTypeIds = new ArrayList<HAPDataTypeId>(criteria.getValidDataTypeId(this));
		HAPDataTypeId firstDataTypeId = dataTypeIds.get(0);
		HAPDataTypeFamily firstDataTypeFamily = this.m_dataAccess.getDataTypeFamily(firstDataTypeId);
		
		List<HAPRelationship> candidates = new ArrayList<HAPRelationship>();
		
		Set<HAPRelationship> fistRelationships = (Set<HAPRelationship>)firstDataTypeFamily.getRelationships();
		for(HAPRelationship firstRelationship : fistRelationships){
			boolean isCandidate = true;
			for(int i=1; i<dataTypeIds.size(); i++){
				HAPDataTypeId otherDataTypeId = dataTypeIds.get(i);
				if(this.convertable(otherDataTypeId, firstRelationship.getTarget())==null){
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
				if(this.convertable(out, candidateTarget)!=null){
					
				}
				else if(this.convertable(candidateTarget, out)!=null){
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
		HAPDataTypeCriteria out = null;
		
		Set<HAPDataTypeId> dataTypeIds = criteria.getValidDataTypeId(this);
		Set<HAPDataTypeId> normalizedDataTypeIds = this.normalize(dataTypeIds);
		
		if(normalizedDataTypeIds.size()==1){
			//one element, use range
			out = new HAPDataTypeCriteriaRange(normalizedDataTypeIds.iterator().next(), null);
		}
		else{
			//multiple, use or
			List<HAPDataTypeCriteria> criterias = new ArrayList<HAPDataTypeCriteria>();
			for(HAPDataTypeId normalizedDataTypeId : normalizedDataTypeIds){
				criterias.add(new HAPDataTypeCriteriaRange(normalizedDataTypeId, null));
			}
			out = new HAPDataTypeCriteriaOr(criterias);
		}
		return out;
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
					if(this.convertable(dataTypeIds.get(i), dataTypeIds.get(j))!=null){
						removes.add(dataTypeIds.get(i));
					}
					else if(this.convertable(dataTypeIds.get(j), dataTypeIds.get(i))!=null){
						removes.add(dataTypeIds.get(j));
					}
				}
			}
			out.removeAll(removes);
		}
		return out;
	}

	@Override
	public HAPMatchers buildMatchers(HAPDataTypeCriteria from, HAPDataTypeCriteria to) {
		Set<HAPDataTypeId> toDataTypeIds = this.normalize(to.getValidDataTypeId(this));
		Set<HAPDataTypeId> fromDataTypeIds = from.getValidDataTypeId(this);
		
		HAPMatchers out = new HAPMatchers();
		for(HAPDataTypeId fromDataTypeId : fromDataTypeIds){
			boolean found = false;
			for(HAPDataTypeId toDataTypeId : toDataTypeIds){
				HAPRelationship relationship = this.convertable(fromDataTypeId, toDataTypeId);
				if(relationship!=null){
					out.addItem(fromDataTypeId, relationship);
					found = true;
					break;
				}					
			}
			if(found==false){
				//if one data type cannot convert to any destination, then return null, that mean it fails
				return null;
			}
		}
		return out;
	}

	
	@Override
	public HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		if(criteria1==null || criteria2==null){
			return null;
		}
		else if(criteria1.equals(HAPDataTypeCriteriaAny.getCriteria()) && criteria2.equals(HAPDataTypeCriteriaAny.getCriteria())){
			return HAPDataTypeCriteriaAny.getCriteria();
		}
		if(criteria1.equals(HAPDataTypeCriteriaAny.getCriteria())){
			return criteria2;
		}
		else if(criteria2.equals(HAPDataTypeCriteriaAny.getCriteria())){
			return criteria1;
		}
		else{
			Set<HAPDataTypeCriteriaId> dataTypesIdCriteria1 = criteria1.getValidDataTypeCriteriaId(this);
			Set<HAPDataTypeCriteriaId> dataTypesIdCriteria2 = criteria2.getValidDataTypeCriteriaId(this);
			Set<HAPDataTypeCriteriaId> andDataTypeIdCriterias = Sets.intersection(dataTypesIdCriteria1, dataTypesIdCriteria2);
			return this.buildDataTypeCriteria(andDataTypeIdCriterias);
		}
	}

	@Override
	public HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeCriteriaId> dataTypeCriterias) {
		HAPDataTypeCriteria out = null;
		if(dataTypeCriterias.size()==1){
			out = dataTypeCriterias.iterator().next();
		}
		else{
			out = new HAPDataTypeCriteriaIds(dataTypeCriterias);
		}
		return out;
	}

	@Override
	public boolean convertable(HAPDataTypeCriteria sourceCriteria, HAPDataTypeCriteria targetCriteria) {
		if(targetCriteria==null){
			return sourceCriteria==null;
		}
		else if(targetCriteria==HAPDataTypeCriteriaAny.getCriteria()){
			return true;
		}
		else{
			Set<HAPDataTypeCriteriaId> sourceIdCriteriaSet = sourceCriteria.getValidDataTypeCriteriaId(this);
			Set<HAPDataTypeCriteriaId> targetIdCriteriaSet = targetCriteria.getValidDataTypeCriteriaId(this);
			
			List<HAPDataTypeCriteriaId> sourceLeaveIdCriterias = this.getLeafCriteriaIds(new ArrayList(sourceIdCriteriaSet));
			List<HAPDataTypeCriteriaId> targetLeaveIdCriterias = this.getLeafCriteriaIds(new ArrayList(targetIdCriteriaSet));
			
			for(HAPDataTypeCriteriaId sourceLeaveIdCriteria : sourceLeaveIdCriterias){
				for(HAPDataTypeCriteriaId targetLeaveIdCriteria : targetLeaveIdCriterias){
					if(!this.convertableIdCriteria(sourceLeaveIdCriteria, targetLeaveIdCriteria)){
						return false;
					}
				}
			}
			return true;
		}
	}

	@Override
	public HAPDataTypeCriteria merge(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2) {
		
		List<HAPDataTypeCriteriaId> criterias1 = new ArrayList(criteria1.getValidDataTypeCriteriaId(this));
		List<HAPDataTypeCriteriaId> leaves1 = this.getLeafCriteriaIds(criterias1);
		
		List<HAPDataTypeCriteriaId> criterias2 = new ArrayList(criteria2.getValidDataTypeCriteriaId(this));
		List<HAPDataTypeCriteriaId> leaves2 = this.getLeafCriteriaIds(criterias2);

		Set<HAPDataTypeCriteriaId> out = new HashSet<HAPDataTypeCriteriaId>();
		for(int i=0; i<leaves1.size(); i++){
			for(int j=0; j<leaves2.size(); j++){
				if(this.convertableIdCriteria(leaves1.get(i), leaves2.get(i))){
					out.add(leaves2.get(i));
					break;
				}
				else if(this.convertableIdCriteria(leaves2.get(i), leaves1.get(i))){
					out.add(leaves1.get(i));
					break;
				}
			}
		}
		if(out.size()==0)   return null;
		else	return new HAPDataTypeCriteriaIds(out);
	}

	
	@Override
	public boolean convertableIdCriteria(HAPDataTypeCriteriaId sourceIdCriteria, HAPDataTypeCriteriaId targetIdCriteria){
		boolean out = true;
		if(this.convertable(sourceIdCriteria.getDataTypeId(), targetIdCriteria.getDataTypeId())!=null){
			HAPDataTypeSubCriteriaGroup sourceSubCriterias = sourceIdCriteria.getSubCriteria();
			HAPDataTypeSubCriteriaGroup targetSubCriterias = targetIdCriteria.getSubCriteria();
			
			Set<String> targetSubNames = targetSubCriterias.getSubCriteriaNames();
			for(String targetSubName : targetSubNames){
				HAPDataTypeCriteria targetSubCriteria = targetSubCriterias.getSubCriteria(targetSubName);
				HAPDataTypeCriteria sourceSubCriteria = sourceSubCriterias.getSubCriteria(targetSubName);
				if(sourceSubCriteria==null){
					//no sub criteria by same name in source, fail
					out = false;
					break;
				}
				else if(targetSubCriteria==HAPDataTypeCriteriaAny.getCriteria()){
					
				}
				else if(sourceSubCriteria==HAPDataTypeCriteriaAny.getCriteria()){
					out = false;
					break;
				}
				else{
					if(!this.convertable(sourceSubCriteria, targetSubCriteria)){
						out = false;
						break;
					}
				}
			}
		}
		else{
			out = false;
		}
		return out;
	}

	
	
	
	private List<HAPDataTypeCriteriaId> getLeafCriteriaIds(List<HAPDataTypeCriteriaId> criteriaIds){
		List<HAPDataTypeCriteriaId> out = new ArrayList(criteriaIds);
		
		int i = 0; 
		while(i<out.size()){
			int j = i+1;
			boolean increasI = true;
			while(j<out.size()){
				if(this.convertableIdCriteria(out.get(j), out.get(i))){
					out.remove(i);
					increasI = false;
					break;
				}
				else if(this.convertableIdCriteria(out.get(i), out.get(j))){
					out.remove(j);
				}
				else{
					j++;
				}
			}
			if(increasI)  i++;
		}
		return out;
	}

	
	@Override
	public Set<HAPRelationship> getRootDataTypeRelationship(HAPDataTypeId dataTypeId){
		List<HAPRelationshipImp> rootRelationships = this.m_dataAccess.getRelationships(dataTypeId, HAPConstant.DATATYPE_RELATIONSHIPTYPE_ROOT);
		return new HashSet<HAPRelationship>(rootRelationships);
	}
	
	@Override
	public HAPDataTypeOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		return this.m_dataAccess.getDataTypeOperation(dataTypeInfo, name);
	}
	
	@Override
	public Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to) {
		Set<HAPDataTypeId> out = null;
		Set<HAPDataTypeId> toSet = null;
		Set<HAPDataTypeId> fromSet = null;
		
		if(to!=null){
			toSet = new HashSet<HAPDataTypeId>();
			HAPDataTypePictureImp toPic = this.m_dataAccess.getDataTypePicture(to);
			Set<HAPRelationship> relationships = (Set<HAPRelationship>)toPic.getRelationships();
			for(HAPRelationship relationship : relationships){
				toSet.add(relationship.getTarget());
			}
		}

		if(from!=null){
			fromSet = new HashSet<HAPDataTypeId>();
			HAPDataTypeFamilyImp fromFamily = this.m_dataAccess.getDataTypeFamily(from);
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
	public HAPRelationship convertable(HAPDataTypeId sourceDataTypeId, HAPDataTypeId targetDataTypeId){
		return this.m_dataAccess.getRelationship(sourceDataTypeId, targetDataTypeId);
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

	/**
	 * From a set of data type ids, find all the data type ids that :
	 * 	1. they are not convertable to each other
	 *  2. they are able to conver to all the original data types
	 */
	private List<HAPDataTypeId> getLeafDataTypeIds(List<HAPDataTypeId> dataTypeIds){
		List<HAPDataTypeId> out = new ArrayList(dataTypeIds);
		
		int i = 0; 
		while(i<out.size()){
			int j = i+1;
			boolean increasI = true;
			while(j<out.size()){
				if(this.convertable(out.get(j), out.get(i))!=null){
					out.remove(i);
					increasI = false;
					break;
				}
				else if(this.convertable(out.get(i), out.get(j))!=null){
					out.remove(j);
				}
				else{
					j++;
				}
			}
			if(increasI)  i++;
		}
		return out;
	}


}
