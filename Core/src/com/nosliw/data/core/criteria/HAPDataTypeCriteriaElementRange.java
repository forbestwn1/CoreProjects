package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementRange extends HAPDataTypeCriteriaImp{
	//general data type
	private HAPDataTypeId m_from;
	
	//more specific data type
	private HAPDataTypeId m_to;

	public HAPDataTypeCriteriaElementRange(HAPDataTypeId from, HAPDataTypeId to){
		this.m_from = from;
		this.m_to = to;
	}
	
	public HAPDataTypeId getFromDataTypeId(){  return this.m_from;  }
	public HAPDataTypeId getToDataTypeId(){  return this.m_to;  }
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPERANGE;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		return dataTypeHelper.getAllDataTypeInRange(m_from, m_to);
	}

	@Override
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
		HAPDataTypeCriteria out = null;
		if(this.m_from!=null){
			HAPDataTypeId dataTypeId = this.m_from;
			out = new HAPDataTypeCriteriaElementId(dataTypeId);
		}
		else if(this.m_to!=null){
			List<HAPDataTypeId> dataTypeIds = new ArrayList(dataTypeHelper.getRootDataTypeId(m_to));
			if(dataTypeIds.size()==1)		out = new HAPDataTypeCriteriaElementId(dataTypeIds.get(0));
			else out = new HAPDataTypeCriteriaElementIds(new HashSet(dataTypeIds));
		}
		else{
			out = new HAPDataTypeCriteriaAny();
		}
		return out;
	}
}
