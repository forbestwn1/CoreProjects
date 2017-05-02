package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaAny extends HAPDataTypeCriteriaImp{

	private static HAPDataTypeCriteriaAny m_instance;
	
	private HAPDataTypeCriteriaAny(){}
	
	public static HAPDataTypeCriteriaAny getCriteria(){
		if(m_instance==null){
			m_instance = new HAPDataTypeCriteriaAny();
		}
		return m_instance;
	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_ANY;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {	return true; }

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {		return true;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {		
		throw new IllegalStateException();	
	}

	@Override
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {		return this;	}

	@Override
	public boolean equals(Object obj){
		return obj instanceof HAPDataTypeCriteriaAny;
	}
}
