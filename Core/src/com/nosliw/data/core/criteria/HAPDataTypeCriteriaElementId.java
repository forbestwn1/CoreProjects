package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementId  extends HAPDataTypeCriteriaImp{

	private HAPDataTypeId m_dataTypeId;

	public HAPDataTypeCriteriaElementId(HAPDataTypeId dataTypeId, HAPDataTypeCriteriaManager criteriaMan){
		super(criteriaMan);
		this.m_dataTypeId = dataTypeId;
	}

	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;  }
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEID;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(){
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		out.add(m_dataTypeId);
		return out;
	}

}
