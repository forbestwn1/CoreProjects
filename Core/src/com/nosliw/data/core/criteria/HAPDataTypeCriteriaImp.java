package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public abstract class HAPDataTypeCriteriaImp extends HAPSerializableImp implements HAPDataTypeCriteria{

	public HAPDataTypeCriteriaImp(){
	}
	
	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		return this.getValidDataTypeId(dataTypeHelper).containsAll(criteria.getValidDataTypeId(dataTypeHelper));
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		return this.getValidDataTypeId(dataTypeHelper).contains(dataTypeId);
	}
	
	protected HAPDataTypeCriteria buildCriteriaByIds(Set<HAPDataTypeId> ids){
		HAPDataTypeCriteria out = null;
		if(ids.size()==1){
			for(HAPDataTypeId id : ids){
				out = new HAPDataTypeCriteriaElementId(id);
			}
		}
		else{
			out = new HAPDataTypeCriteriaElementIds(ids);
		}
		return out;
	}
}
